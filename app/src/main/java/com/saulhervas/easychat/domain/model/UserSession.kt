package com.saulhervas.easychat.domain.model

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
data class UserSession @Inject constructor (
    var id: String = "",
    var token: String = ""
)