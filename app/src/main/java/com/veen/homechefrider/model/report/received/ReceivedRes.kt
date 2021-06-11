package com.veen.homechefrider.model.report.received

data class ReceivedRes(
        val `data`: ReceivedData,
        val msg: String,
        val status: Boolean
)