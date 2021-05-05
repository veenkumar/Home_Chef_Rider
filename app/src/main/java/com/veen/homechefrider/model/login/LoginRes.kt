package com.veen.homechefrider.model.login

data class LoginRes(
    val `data`: Data,
    val msg: String,
    val status: Boolean,
    val token: String
)