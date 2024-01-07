package com.example.share_money_now.data_classes

data class Group(
    val id: String = "",
    val ownerId: String = "",
    val name: String = "",
    val members: List<Person?> = emptyList(),
    val description: String = "",
    val totalAmount: Double = 0.0,
    var paidAmount: Map<String, Double> = emptyMap()
)