package com.veen.homechefrider.model.complain

data class ComplainReq(
    val base64_img: String,
    val message: String,
    val subject: String,
    val user_id: Int
)