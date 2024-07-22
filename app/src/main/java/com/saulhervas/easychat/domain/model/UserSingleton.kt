package com.saulhervas.easychat.domain.model

import javax.inject.Singleton

@Singleton
data class UserSingleton (
    var id: String = "",
    var token: String = ""
)