package com.saulhervas.easychat.ui.user_logout

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.saulhervas.easychat.data.repository.response.logout.LogoutResponse
import com.saulhervas.easychat.domain.model.BaseResponse
import com.saulhervas.easychat.domain.usecases.UserUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserLogoutViewModel @Inject constructor(
    private val logoutUserCase: UserUseCases
) : ViewModel() {

    private val _logoutResultError = MutableSharedFlow<String>()
    val logoutResulterror: SharedFlow<String> get() = _logoutResultError

    private val _logoutResult = MutableSharedFlow<LogoutResponse>()
    val logoutResult: SharedFlow<LogoutResponse> get() = _logoutResult

    fun logoutUser() {
        viewModelScope.launch {
            logoutUserCase.logoutUser().collect { response ->
                when (response) {
                    is BaseResponse.Error -> {
                        Log.e("TAG", "Error: ${response.error.message}")
                        _logoutResultError.emit(response.error.message)
                    }

                    is BaseResponse.Success -> {
                        _logoutResult.emit(response.data)
                    }
                }
            }
        }
    }

}