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
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
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

    private val newChatsMutableState =
        MutableStateFlow<ArrayList<UserNewChatItemModel>>(ArrayList(emptyList()))
    val newChatsState: StateFlow<ArrayList<UserNewChatItemModel>> = newChatsMutableState

    private val _isChatCreatedSharedFlow = MutableSharedFlow<Boolean>()
    val isChatCreatedSharedFlow: SharedFlow<Boolean> = _isChatCreatedSharedFlow


    private val showImageBackgroundMutableState = MutableStateFlow(true)
    val showImageBackgroundState: StateFlow<Boolean> = showImageBackgroundMutableState

    private val loadingMutableState = MutableStateFlow(false)
    val loadingState: StateFlow<Boolean> = loadingMutableState

    val colorMap = mutableMapOf<String, Int>()

    fun getOpenChats() {
        viewModelScope.launch {
            loadingMutableState.value = true
            chatUseCases.getOpenChats().collect {
                when (it) {
                    is BaseResponse.Error -> {
                        Log.d("TAG", "l> Error: ${it.error.message}")
                    }

                    is BaseResponse.Success -> {
                        Log.d("TAG", "l> Success ${it.data.size}")
                        Log.d("TAG", "l> Success ${it.data}")
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

    fun getUserList() {
        viewModelScope.launch {
            userUseCases.userList().collect { result ->
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

    fun filterUsers(idUser: String) {
        viewModelScope.launch {
            openChatsState.collect { list ->
                val openChatUserIds = list.flatMap { listOfNotNull(it.idTargetUser) }
                updateUserNewChatState(idUser, openChatUserIds)
            }
        }
    }

    private fun updateUserNewChatState(idUser: String, openChatUserIds: List<String>) {
        viewModelScope.launch {
            newChatsState.collect { list ->
                getFilteredList(list.filterNot { user ->
                    user.id in openChatUserIds || user.id != idUser
                })
            }
        }
    }

    fun getFilteredList(filteredList: List<UserNewChatItemModel>) =
        filteredList.sortedBy { user -> user.nick }

    fun createChat(idUser: String, idTarget: String) {
        val newChatRequest = NewChatRequest(
            idSource = idUser,
            idTarget = idTarget
        )
        viewModelScope.launch {
            loadingMutableState.value = true
            chatUseCases.newChat(newChatRequest).collect {
                when (it) {
                    is BaseResponse.Error -> {
                        loadingMutableState.value = false
                    }

                    is BaseResponse.Success -> {
                        loadingMutableState.value = false
                        _isChatCreatedSharedFlow.emit(true)
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
                        Log.d("TAG", "Error: ${it.error.message}")
                        Log.d("TAG", "Error: ${it.error.token}")
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
}