package com.veen.homechefrider.model.report.received

data class ReceivedData(
    val currency: String,
    val grand_total: Int,
    val received_payment: List<ReceivedPayment>
)