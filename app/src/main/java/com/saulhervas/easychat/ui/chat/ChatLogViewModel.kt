package com.saulhervas.easychat.ui.chat

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.saulhervas.easychat.data.repository.response.new_message.NewMessageRequest
import com.saulhervas.easychat.domain.model.BaseResponse
import com.saulhervas.easychat.domain.model.messages_list.MessagesModel
import com.saulhervas.easychat.domain.usecases.MessageUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatLogViewModel @Inject constructor(
    private val messageUseCases: MessageUseCases
) : ViewModel() {
    private val messagesMutableState = MutableStateFlow(MessagesModel(0, ArrayList(emptyList())))
    val messagesState: StateFlow<MessagesModel> = messagesMutableState

    private val messageSentMutableState = MutableStateFlow("")
    val messagesSentState: StateFlow<String> = messageSentMutableState

    fun getOpenChats(id: String, offset: Int, limit: Int) {
        viewModelScope.launch {
            messageUseCases.getMessagesList(id, offset, limit).collect {
                when (it) {
                    is BaseResponse.Error -> {
                        Log.d("TAG", "l> Error: ${it.error.message}")
                        //loadingMutableSharedFlow.emit(false)
                        //errorMutableSharedFlow.emit(it.error)
                    }

                    is BaseResponse.Success -> {
                        //loadingMutableSharedFlow.emit(false)
                        Log.d("TAG", "l> Success ${it.data}")
                        messagesMutableState.value = it.data
                    }
                }
            }
        }
    }

    fun sendMessage(newMessageRequest: NewMessageRequest) {
        viewModelScope.launch {
            messageUseCases.newMessage(newMessageRequest).collect {
                when (it) {
                    is BaseResponse.Error -> {
                        Log.d("TAG", "l> Error: ${it.error.message}")
                        //loadingMutableSharedFlow.emit(false)
                        //errorMutableSharedFlow.emit(it.error)
                    }

                    is BaseResponse.Success -> {
                        //loadingMutableSharedFlow.emit(false)
                        Log.d("TAG", "l> Success ${it.data}")
                        //messagesMutableState.value = it.data
                        messageSentMutableState.value = it.data.success
                    }
                }
            }
        }
    }

}