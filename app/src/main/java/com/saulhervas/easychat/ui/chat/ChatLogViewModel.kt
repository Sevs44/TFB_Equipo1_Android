package com.saulhervas.easychat.ui.chat

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.saulhervas.easychat.data.repository.response.new_message.NewMessageRequest
import com.saulhervas.easychat.domain.model.BaseResponse
import com.saulhervas.easychat.domain.model.messages_list.MessageItemModel
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

    private val messagesCountMutableState = MutableStateFlow(0)
    val messagesCountState: StateFlow<Int> = messagesCountMutableState

    private val messagesMutableState = MutableStateFlow<ArrayList<MessageItemModel>>(ArrayList(emptyList()))
    val messagesState: StateFlow<ArrayList<MessageItemModel>> = messagesMutableState

    private val messageSentMutableState = MutableStateFlow("")
    val messagesSentState: StateFlow<String> = messageSentMutableState


    fun getOpenChats(id: String, offset: Int, limit: Int) {
        viewModelScope.launch {
            messageUseCases.getMessagesList(id, offset, limit).collect {
                when (it) {
                    is BaseResponse.Error -> {
                        Log.d("TAG", "Error: ${it.error.message}")
                        // Handle error
                    }
                    is BaseResponse.Success -> {
                        Log.d("TAG", "Success ${it.data}")
                        messagesCountMutableState.value = it.data.nMessages!!
                        messagesMutableState.value = (messagesMutableState.value + it.data.messageList!!) as ArrayList<MessageItemModel>
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
                        Log.d("TAG", "Error: ${it.error.message}")
                        // Handle error
                    }
                    is BaseResponse.Success -> {
                        Log.d("TAG", "Success ${it.data}")
                        messageSentMutableState.value = it.data.success
                    }
                }
            }
        }
    }
}