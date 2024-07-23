package com.saulhervas.easychat.ui.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.saulhervas.easychat.domain.model.BaseResponse
import com.saulhervas.easychat.domain.model.OpenChatItemModel
import com.saulhervas.easychat.domain.usecases.ChatUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val chatUseCases: ChatUseCases
): ViewModel() {
    private val openChatsMutableState = MutableStateFlow<ArrayList<OpenChatItemModel>>(ArrayList(emptyList()))
    val openChatsState: StateFlow<ArrayList<OpenChatItemModel>> = openChatsMutableState

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
}