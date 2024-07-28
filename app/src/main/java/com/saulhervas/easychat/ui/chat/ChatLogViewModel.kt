package com.saulhervas.easychat.ui.chat

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.saulhervas.easychat.data.repository.response.new_message.NewMessageRequest
import com.saulhervas.easychat.domain.model.BaseResponse
import com.saulhervas.easychat.domain.model.messages_list.MessageItemModel
import com.saulhervas.easychat.domain.usecases.MessageUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val LIMIT_MESSAGES = 100
private const val OFFSET_0_MESSAGE_SENT = 0

@HiltViewModel
class ChatLogViewModel @Inject constructor(
    private val messageUseCases: MessageUseCases
) : ViewModel() {

    private val messagesCountMutableState = MutableStateFlow(0)
    val messagesCountState: StateFlow<Int> = messagesCountMutableState

    private val messagesMutableState = MutableStateFlow<ArrayList<MessageItemModel>>(ArrayList(emptyList()))
    val messagesState: StateFlow<ArrayList<MessageItemModel>> = messagesMutableState

    private val messageSentMutableShared = MutableSharedFlow<Boolean>()
    val messagesSentShared: SharedFlow<Boolean> = messageSentMutableShared

    private val loadingMutableShared = MutableSharedFlow<Boolean>()
    val loadingShared: SharedFlow<Boolean> = loadingMutableShared

    fun getMessages(id: String, offset: Int, limit: Int) {
        viewModelScope.launch {
            loadingMutableShared.emit(true)
            messageUseCases.getMessagesList(id, offset, limit).collect {
                when (it) {
                    is BaseResponse.Error -> {
                        Log.d("TAG", "Error: ${it.error.message}")
                        loadingMutableShared.emit(false)
                    }
                    is BaseResponse.Success -> {
                        Log.d("TAG", "Success ${it.data}")
                        messagesCountMutableState.value = it.data.nMessages!!
                        mergeAndSortMessages(it.data.messageList ?: emptyList())
                        loadingMutableShared.emit(false)
                    }
                }
            }
        }
    }

    private fun mergeAndSortMessages(newMessages: List<MessageItemModel>) {
        val combinedMessages = (messagesMutableState.value + newMessages).distinctBy { it.idMessage }

        val sortedMessages = combinedMessages.sortedByDescending { it.idMessage }

        messagesMutableState.value = ArrayList(sortedMessages)
    }

    fun sendMessage(newMessageRequest: NewMessageRequest) {
        if (newMessageRequest.messageContent != "") {
            viewModelScope.launch {
                loadingMutableShared.emit(true)
                messageUseCases.newMessage(newMessageRequest).collect {
                    when (it) {
                        is BaseResponse.Error -> {
                            Log.d("TAG", "Error: ${it.error.message}")
                            loadingMutableShared.emit(false)
                        }

                        is BaseResponse.Success -> {
                            Log.d("TAG", "Success ${it.data}")
                            messageSentMutableShared.emit(it.data.success.toBoolean())
                            loadingMutableShared.emit(false)
                        }
                    }
                }
            }
        }
    }

    fun startPeriodicRefresh(id: String, offset: Int, limit: Int) {
        viewModelScope.launch() {
            while (true) {
                delay(5000)
                getMessages(id, offset, limit)
            }
        }
    }
}