package com.veen.homechefrider.model.registration

data class RegisReq(
    val address: String,
    val email: String,
    val name: String,
    val password: String,
    val phone_no: String
)