package com.saulhervas.easychat.ui.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.saulhervas.easychat.domain.model.BaseResponse
import com.saulhervas.easychat.domain.model.OpenChatItemModel
import com.saulhervas.easychat.domain.usecases.OpenChatUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val openChatUseCases: OpenChatUseCases
): ViewModel() {
    private val openChatsMutableState = MutableSharedFlow<ArrayList<OpenChatItemModel>>()
    val openChatsState: SharedFlow<ArrayList<OpenChatItemModel>> = openChatsMutableState

    init {
        getOpenChats()
    }

    fun getOpenChats() {
        viewModelScope.launch {
             openChatUseCases.invoke().collect {
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
                         openChatsMutableState.emit(it.data)
                         //openChatsMutableState.value = ArrayList(it.data.map { model -> model.id })
                     }
                 }
             }
        }
    }
}