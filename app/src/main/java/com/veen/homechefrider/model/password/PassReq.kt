package com.veen.homechefrider.model.password

data class PassReq(
    val new_password: String,
    val old_password: String,
    val user_id: Int
)