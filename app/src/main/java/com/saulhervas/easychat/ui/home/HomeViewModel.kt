package com.saulhervas.easychat.ui.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.saulhervas.easychat.data.repository.response.new_chat.NewChatRequest
import com.saulhervas.easychat.domain.model.BaseResponse
import com.saulhervas.easychat.domain.model.OpenChatItemModel
import com.saulhervas.easychat.domain.model.UserNewChatItemModel
import com.saulhervas.easychat.domain.usecases.ChatUseCases
import com.saulhervas.easychat.domain.usecases.UserUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val chatUseCases: ChatUseCases,
    private val userUseCases: UserUseCases
): ViewModel() {
    private val openChatsMutableState = MutableStateFlow<ArrayList<OpenChatItemModel>>(ArrayList(emptyList()))
    val openChatsState: StateFlow<ArrayList<OpenChatItemModel>> = openChatsMutableState

    private val newChatsMutableState =
        MutableStateFlow<ArrayList<UserNewChatItemModel>>(ArrayList(emptyList()))
    val newChatsState: StateFlow<ArrayList<UserNewChatItemModel>> = newChatsMutableState

    private val isChatCreatedMutableState = MutableStateFlow(false)
    val isChatCreatedState: StateFlow<Boolean> = isChatCreatedMutableState


    private val showImageBackgroundMutableState = MutableStateFlow(true)
    val showImageBackgroundState: StateFlow<Boolean> = showImageBackgroundMutableState

    fun getOpenChats() {
        viewModelScope.launch {
             chatUseCases.getOpenChats().collect {
                 when (it) {
                     is BaseResponse.Error -> {
                         Log.d("TAG", "l> Error: ${it.error.message}")
                         //loadingMutableSharedFlow.emit(false)
                         //errorMutableSharedFlow.emit(it.error)
                     }

                     is BaseResponse.Success -> {
                         //loadingMutableSharedFlow.emit(false)
                         Log.d("TAG", "l> Success ${it.data.size}")
                         Log.d("TAG", "l> Success ${it.data}")
                         if (it.data.isNotEmpty()) {
                             openChatsMutableState.value = it.data
                             showImageBackgroundMutableState.value = false
                         } else {
                             showImageBackgroundMutableState.value = true
                         }
                     }
                 }
             }
        }
    }

    private fun getUserList(token: String) {
        viewModelScope.launch {
            userUseCases.userList(token).collect { result ->
                when (result) {
                    is BaseResponse.Error -> {
                        Log.d("TAG", "l> Error: ${result.error.message}")
                    }

                    is BaseResponse.Success -> {
                        Log.d("TAG", "l> Success ${result.data.size}")
                        Log.d("TAG", "l> Success ${result.data}")
                        newChatsMutableState.value = result.data
                    }
                }
            }
        }
    }

    fun filterUsers(
        originalList: List<UserNewChatItemModel>,
        openChatList: MutableList<OpenChatItemModel>
    ): List<UserNewChatItemModel> {
        val openChatUserIds = openChatList.flatMap { listOfNotNull(it.idUser1, it.idUser2) }
        return originalList.filterNot { user -> user.id in openChatUserIds }
    }


    fun createChat(token: String, idUser: String, idTarget: String) {
        val newChatRequest = NewChatRequest(
            idSource = idUser,
            idTarget = idTarget
        )
        viewModelScope.launch {
            chatUseCases.newChat(token, newChatRequest).collect { result ->
                when (result) {
                    is BaseResponse.Error -> {
                        Log.d("TAG", "l> Error: ${result.error.message}")
                    }

                    is BaseResponse.Success -> {
                        Log.d("TAG", "l> Success = ${result.data.success}")
                        Log.d("TAG", "l> Chat Created = ${result.data.created}")
                        isChatCreatedMutableState.value = result.data.created
                    }
                }
            }
        }
    }
}