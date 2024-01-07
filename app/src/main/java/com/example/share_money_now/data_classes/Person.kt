package com.example.share_money_now.data_classes

data class Person(
    val email: String = "",
    val name: String = "",
    val groups: List<Group> = emptyList()
)