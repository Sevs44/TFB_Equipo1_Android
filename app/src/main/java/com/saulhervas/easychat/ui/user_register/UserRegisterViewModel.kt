package com.saulhervas.easychat.ui.user_register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.saulhervas.easychat.data.repository.response.register.RegisterRequest
import com.saulhervas.easychat.domain.model.BaseResponse
import com.saulhervas.easychat.domain.usecases.UserUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserRegisterViewModel @Inject constructor(
    private val registerUserCase: UserUseCases
) : ViewModel() {
    private val loadingMutableState = MutableStateFlow(false)
    val loadingState: StateFlow<Boolean> = loadingMutableState

    private val errorMutableState = MutableSharedFlow<String?>()
    val errorState: SharedFlow<String?> = errorMutableState

    private val registerUserMutableState = MutableStateFlow(false)
    val registerUserState: StateFlow<Boolean> = registerUserMutableState

    private val passwordVisibilityMutableState = MutableStateFlow(false)
    val passwordVisibilityState: StateFlow<Boolean> = passwordVisibilityMutableState

    private val passwordRepeatVisibilityMutableState = MutableStateFlow(false)
    val passwordRepeatVisibilityState: StateFlow<Boolean> = passwordRepeatVisibilityMutableState

    fun registerUser(username: String, password: String, nick: String) {
        val registerRequest = RegisterRequest(
            login = username,
            password = password,
            nick = nick
        )

        viewModelScope.launch {
            loadingMutableState.value = true
            registerUserCase.registerUser(registerRequest).collect { result ->
                loadingMutableState.value = false
                when (result) {
                    is BaseResponse.Error -> {
                        errorMutableState.emit(result.error.message)
                        registerUserMutableState.value = false
                    }
                    is BaseResponse.Success -> {
                        registerUserMutableState.value = true
                    }
                }
            }
        }
    }

    fun togglePasswordVisibility() {
        passwordVisibilityMutableState.value = !passwordVisibilityMutableState.value
    }

    fun togglePasswordRepeatVisibility() {
        passwordRepeatVisibilityMutableState.value = !passwordRepeatVisibilityMutableState.value
    }
}
