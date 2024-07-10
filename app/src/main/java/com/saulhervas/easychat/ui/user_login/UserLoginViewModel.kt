package com.saulhervas.easychat.ui.user_login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.saulhervas.easychat.data.model.modelslogin.LoginRequest
import com.saulhervas.easychat.data.model.modelslogin.LoginResponse
import com.saulhervas.easychat.data.repository.backend.retrofit.ApiService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class UserLoginViewModel @Inject constructor(private val apiService: ApiService) : ViewModel() {

    private val _loginResult = MutableSharedFlow<Result<LoginResponse>>()
    val loginResult: SharedFlow<Result<LoginResponse>> get() = _loginResult

    fun loginUser(username: String, password: String) {
        val loginRequest = LoginRequest(username, password)
        viewModelScope.launch {
            try {
                val loginResponse = apiService.loginUser(loginRequest)
                _loginResult.emit(Result.success(loginResponse))
            } catch (e: HttpException) {
                _loginResult.emit(Result.failure(Exception("HTTP exception occurred", e)))
            } catch (e: IOException) {
                _loginResult.emit(Result.failure(Exception("Network error occurred", e)))
            } catch (e: Exception) {
                _loginResult.emit(Result.failure(Exception("Unknown error occurred", e)))
            }
        }
    }
}
