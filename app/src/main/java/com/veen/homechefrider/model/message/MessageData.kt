package com.veen.homechefrider.model.message

data class MessageData(
    val created_at: String,
    val `file`: String,
    val id: String,
    val message: String,
    val reply_message: Any,
    val reply_status: String,
    val subject: String
)