package com.veen.homechefrider.model.message

data class MessageRes(
    val `data`: MessageData,
    val msg: String,
    val status: Boolean
)