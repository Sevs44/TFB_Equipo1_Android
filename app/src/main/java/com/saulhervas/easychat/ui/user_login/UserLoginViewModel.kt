package com.saulhervas.easychat.ui.user_login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.saulhervas.easychat.data.repository.response.login.LoginRequest
import com.saulhervas.easychat.data.repository.response.login.LoginResponse
import com.saulhervas.easychat.data.repository.response.logout.LogoutResponse
import com.saulhervas.easychat.domain.model.BaseResponse
import com.saulhervas.easychat.domain.usecases.LoginUseCase
import com.saulhervas.easychat.domain.usecases.LogoutUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserLoginViewModel @Inject constructor(
    private val loginUserCase: LoginUseCase,
    private val logoutUserCase: LogoutUseCase
) : ViewModel() {

    private val _loginResultError = MutableSharedFlow<String>()
    val loginResulterror: SharedFlow<String> get() = _loginResultError

    private val _loginResult = MutableSharedFlow<LoginResponse>()
    val loginResult: SharedFlow<LoginResponse> get() = _loginResult

    private val _logoutResultError = MutableSharedFlow<String>()
    val logoutResulterror: SharedFlow<String> get() = _logoutResultError

    private val _logoutResult = MutableSharedFlow<LogoutResponse>()
    val logoutResult: SharedFlow<LogoutResponse> get() = _logoutResult

    fun loginUser(username: String, password: String) {
        val loginRequest = LoginRequest(username, password)
        viewModelScope.launch {
            loginUserCase.invoke(loginRequest).collect { response ->
                when (response) {
                    is BaseResponse.Error -> {
                        Log.d("TAG", "Error: ${response.error.message}")
                        _loginResultError.emit(response.error.message)
                    }

                    is BaseResponse.Success -> {
                        _loginResult.emit(response.data)
                    }
                }
            }
        }
    }
    fun logoutUser(token: String) {
        viewModelScope.launch {
            logoutUserCase.invoke(token).collect { response ->
                when (response) {
                    is BaseResponse.Error -> {
                        Log.d("TAG", "Error: ${response.error.message}")
                        _logoutResultError.emit(response.error.message)
                    }

                    is BaseResponse.Success -> {
                        Log.d("TAG", "LOGOUTEADO: ${response.data}")
                        _logoutResult.emit(response.data)
                    }
                }
            }
        }
    }

}
