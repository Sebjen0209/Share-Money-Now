package com.example.share_money_now.data_classes


data class PaymentList(
    var groupId: String,
    var paymentList: List<Payment> = emptyList(),
    var paidAmount: Map<Person, Double> = emptyMap(),
    var individualCostAmount: Map<Person, Double> = emptyMap()
)
