package com.veen.homechefrider.model.report.complete

data class ReceivedPayment(
    val created_at: String,
    val order_id: String,
    val total_amount: Int
)