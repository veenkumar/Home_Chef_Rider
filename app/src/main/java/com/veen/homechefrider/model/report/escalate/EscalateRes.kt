package com.veen.homechefrider.model.report.escalate

data class EscalateRes(
        val `data`: EscalateData,
        val msg: String,
        val status: Boolean
)