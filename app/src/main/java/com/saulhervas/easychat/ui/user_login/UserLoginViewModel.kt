package com.saulhervas.easychat.ui.user_login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.saulhervas.easychat.data.repository.response.login.LoginRequest
import com.saulhervas.easychat.data.repository.response.login.LoginResponse
import com.saulhervas.easychat.domain.model.BaseResponse
import com.saulhervas.easychat.domain.model.UserSession
import com.saulhervas.easychat.domain.usecases.UserUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel

class UserLoginViewModel @Inject constructor(
    private val loginUserCase: UserUseCases,
    private val userSession: UserSession
) : ViewModel() {

    private val _loginResultError = MutableSharedFlow<String>()
    val loginResulterror: SharedFlow<String> get() = _loginResultError

    private val _loginResult = MutableSharedFlow<LoginResponse>()
    val loginResult: SharedFlow<LoginResponse> get() = _loginResult

    fun loginUser(username: String, password: String) {
        val loginRequest = LoginRequest(username, password)
        viewModelScope.launch {
            loginUserCase.loginUser(loginRequest).collect { response ->
                when (response) {
                    is BaseResponse.Error -> {
                        Log.e("TAG", "Error: ${response.error.message}")
                        if (response.error.message.contains("user not found", ignoreCase = true)) {
                            _loginResultError.emit("El usuario no existe. Verifique sus credenciales.")
                        } else {
                            _loginResultError.emit(response.error.message)
                        }
                    }

                    is BaseResponse.Success -> {
                        userSession.token = response.data.token
                        userSession.id = response.data.userLogin.id
                        _loginResult.emit(response.data)
                    }
                }
            }
        }
    }

    fun loginWithBiometrics() {
        viewModelScope.launch {
            loginUserCase.biometricUser().collect { response ->
                when (response) {
                    is BaseResponse.Error -> {
                        Log.e("TAG", "Error: ${response.error.message}")
                        _loginResultError.emit(response.error.message)
                    }

                    is BaseResponse.Success -> {
                        _loginResult.emit(response.data)
                    }
                }
            }
        }
    }
}
