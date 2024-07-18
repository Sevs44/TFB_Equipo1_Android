package com.saulhervas.easychat.ui.settings

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.saulhervas.easychat.data.repository.response.profile.UserProfileResponse
import com.saulhervas.easychat.domain.model.BaseResponse
import com.saulhervas.easychat.domain.usecases.UserUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ProfileSettingViewModel @Inject constructor(
    private val profileUseCase: UserUseCases
) : ViewModel() {
    private val _userProfile = MutableStateFlow<UserProfileResponse?>(null)
    val userProfile: StateFlow<UserProfileResponse?> = _userProfile


    fun getUserProfile(token: String) {
        viewModelScope.launch {
            profileUseCase.userProfile(token).collect {
                _userProfile.value
                Log.d("Profile", it.toString())
                when (it) {
                    is BaseResponse.Success -> {
                        _userProfile.value = it.data
                    }

                    is BaseResponse.Error -> {
                        _userProfile.value = null
                    }
                }
            }
        }

    }
}