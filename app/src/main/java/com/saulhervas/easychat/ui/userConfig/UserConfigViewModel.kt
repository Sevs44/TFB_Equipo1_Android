package com.saulhervas.easychat.ui.userConfig

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
class UserConfigViewModel @Inject constructor(
    private val profileUseCase: UserUseCases
) : ViewModel() {

    private val _userConfig = MutableStateFlow<UserProfileResponse?>(null)
    val userConfig: StateFlow<UserProfileResponse?> = _userConfig

    fun getUserConfig() {
        viewModelScope.launch {
            profileUseCase.userProfile().collect {
                _userConfig.value
                when (it) {
                    is BaseResponse.Success -> {
                        _userConfig.value = it.data
                    }

                    is BaseResponse.Error -> {
                        _userConfig.value = null
                    }
                }
            }
        }
    }
}
