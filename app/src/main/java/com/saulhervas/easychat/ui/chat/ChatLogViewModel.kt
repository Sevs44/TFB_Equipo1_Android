package com.saulhervas.easychat.ui.chat

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
    private val messagesMutableState =
        MutableStateFlow<MessagesModel>(MessagesModel(0, ArrayList(emptyList())))
    val messagesState: StateFlow<MessagesModel> = messagesMutableState

    fun getOpenChats(token: String) {
        viewModelScope.launch {
            messageUseCases.getMessagesList(token).collect {
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

}