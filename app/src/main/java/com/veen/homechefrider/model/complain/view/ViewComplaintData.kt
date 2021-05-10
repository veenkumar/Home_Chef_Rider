package com.veen.homechefrider.model.complain.view

data class ViewComplaintData(
    val created_at: String,
    val `file`: String,
    val id: String,
    val message: String,
    val reply_status: String,
    val subject: String
)