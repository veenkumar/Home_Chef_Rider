package com.veen.homechefrider.model.report.complete

data class CompleteData(
    val currency: String,
    val received_payment: List<ReceivedPayment>
)