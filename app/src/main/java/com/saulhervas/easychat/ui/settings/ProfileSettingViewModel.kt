package com.saulhervas.easychat.ui.settings

import android.annotation.SuppressLint
import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.saulhervas.easychat.data.repository.response.profile.UserProfileResponse
import com.saulhervas.easychat.domain.encryptedsharedpreference.SecurePreferences
import com.saulhervas.easychat.domain.model.BaseResponse
import com.saulhervas.easychat.domain.usecases.UserUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@SuppressLint("StaticFieldLeak")
@HiltViewModel
class ProfileSettingViewModel @Inject constructor(
    private val profileUseCase: UserUseCases,
    application: Application
) : AndroidViewModel(application) {

    private val context = application.applicationContext

    private val _userProfile = MutableStateFlow<UserProfileResponse?>(null)
    val userProfile: StateFlow<UserProfileResponse?> = _userProfile

    private val _keepSession = MutableSharedFlow<Boolean>(replay = 1)
    val keepSession: SharedFlow<Boolean> = _keepSession

    private val _onlineStatus = MutableSharedFlow<Boolean>(replay = 1)
    val onlineStatus: SharedFlow<Boolean> = _onlineStatus

    init {
        viewModelScope.launch {
            _keepSession.emit(SecurePreferences.getKeepSession(context))
            _onlineStatus.emit(SecurePreferences.getOnlineStatus(context))
        }
    }


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

    fun saveKeepSessionPreference(value: Boolean) {
        viewModelScope.launch {
            SecurePreferences.saveKeepSession(context, value)
            _keepSession.emit(value)
        }
    }

    fun saveShowOnlineStatusPreference(value: Boolean) {
        viewModelScope.launch {
            SecurePreferences.saveOnlineStatus(context, value)
            _onlineStatus.emit(value)
        }
    }


}