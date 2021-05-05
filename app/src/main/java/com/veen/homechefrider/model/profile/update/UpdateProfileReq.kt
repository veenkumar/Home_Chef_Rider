package com.veen.homechefrider.model.profile.update

data class UpdateProfileReq(
    val address: String,
    val name: String,
    val phone: String,
    val user_id: Int
)