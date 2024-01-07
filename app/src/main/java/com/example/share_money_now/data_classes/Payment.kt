package com.example.share_money_now.data_classes

data class Payment(
    val name: String,
    val cost: Double,
    val members: List<Person?> = emptyList(),
    val payer: Person
)