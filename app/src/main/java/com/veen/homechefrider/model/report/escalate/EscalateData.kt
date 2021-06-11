package com.veen.homechefrider.model.report.escalate

data class EscalateData(
    val currency: String,
    val received_payment: List<ReceivedPayment>
)