package com.saulhervas.easychat.domain.model

import javax.inject.Inject

data class UserSession @Inject constructor(
    var id: String = "",
    var token: String = ""
)