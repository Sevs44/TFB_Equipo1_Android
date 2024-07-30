package com.saulhervas.easychat.ui.home

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.saulhervas.easychat.domain.encryptedsharedpreference.SecurePreferences
import com.saulhervas.easychat.domain.model.BaseResponse
import com.saulhervas.easychat.domain.model.OpenChatItemModel
import com.saulhervas.easychat.domain.usecases.ChatUseCases
import com.saulhervas.easychat.domain.usecases.UserUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val chatUseCases: ChatUseCases,
    private val userUseCases: UserUseCases
) : ViewModel() {

    private val openChatsMutableState =
        MutableStateFlow<ArrayList<OpenChatItemModel>>(ArrayList(emptyList()))
    val openChatsState: StateFlow<ArrayList<OpenChatItemModel>> = openChatsMutableState

    private val showImageBackgroundMutableState = MutableStateFlow(true)
    val showImageBackgroundState: StateFlow<Boolean> = showImageBackgroundMutableState

    private val loadingMutableState = MutableStateFlow(false)
    val loadingState: StateFlow<Boolean> = loadingMutableState

    private val navigateMutableState = MutableStateFlow(false)
    val navigateState: StateFlow<Boolean> = navigateMutableState.asStateFlow()

    val colorMap = mutableMapOf<String, Int>()

    fun getOpenChats() {
        viewModelScope.launch {
            loadingMutableState.value = true
            chatUseCases.getOpenChats().collect {
                when (it) {
                    is BaseResponse.Error -> {
                        Log.e("TAG", "l> Error: ${it.error.message}")
                    }
                    is BaseResponse.Success -> {
                        if (it.data.isNotEmpty()) {
                            openChatsMutableState.value = it.data
                            showImageBackgroundMutableState.value = false
                        } else {
                            showImageBackgroundMutableState.value = true
                        }
                        loadingMutableState.value = false
                    }
                }
            }
        }
    }

    fun deleteChats(idChat: String) {
        viewModelScope.launch {
            loadingMutableState.value = true
            chatUseCases.deleteChat(idChat).collect {
                when (it) {
                    is BaseResponse.Error -> {
                        Log.e("TAG", "Error: ${it.error.message}")
                        Log.e("TAG", "Error: ${it.error.token}")
                        loadingMutableState.value = false
                    }

                    is BaseResponse.Success -> {
                        Log.d("TAG", "Success ${it.data}")
                        loadingMutableState.value = false
                    }
                }
            }
        }
    }

    fun onAddButtonClicked() {
        navigateMutableState.value = true
    }

    fun resetNavigation() {
        navigateMutableState.value = false
    }

    fun checkOnlineStatus(context: Context) {
        viewModelScope.launch {
            Log.d("ONLINE", "${SecurePreferences.getOnlineStatus(context)}")
            if (!SecurePreferences.getOnlineStatus(context))
                userUseCases.onlineFalse().collect { response ->
                    when (response) {
                        is BaseResponse.Success -> {
                            Log.d("ONLINE", "False: ${response.data}")
                        }

                        is BaseResponse.Error -> {
                            Log.e("ONLINE", "False call error")
                        }
                    }
                }
            else
                userUseCases.onlineTrue().collect { response ->
                    when (response) {
                        is BaseResponse.Success -> {
                            Log.d("ONLINE", "True: ${response.data}")
                        }

                        is BaseResponse.Error -> {
                            Log.e("ONLINE", "True call error")
                        }
                    }

                }
        }
    }

}