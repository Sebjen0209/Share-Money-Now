package com.example.share_money_now.data_classes

data class Group(val id: String, val ownerId: String, val name: String, val members: List<Person>)