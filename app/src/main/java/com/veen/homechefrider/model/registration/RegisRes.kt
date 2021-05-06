package com.veen.homechefrider.model.registration

data class RegisRes(
    val `data`: RegisData,
    val msg: String,
    val status: Boolean,
    val token: String
)