package com.example.share_money_now.data_classes

data class Group(
    val id: String = "",
    val ownerId: String = "",
    val name: String = "",
    val members: List<Person?> = emptyList(),
    val description: String = "",
    val totalAmount: Double = 0.0,
    var paidAmount: Map<String, Double> = emptyMap(),
    val payments: List<Payment> = emptyList()
)

fun calculateDebts(group: Group): Map<Person?, Double> {
    val balances = mutableMapOf<Person?, Double>()

    for (payment in group.payments) {
        val perPersonShare = payment.cost / payment.members.size

        balances[payment.payer] = balances.getOrDefault(payment.payer, 0.0) - payment.cost

        for (member in payment.members) {
            balances[member] = balances.getOrDefault(member, 0.0) + perPersonShare
        }
    }

    return balances
}

fun calculateDebtForPerson(group: Group, targetPerson: Person): Double {
    var debt = 0.0

    for (payment in group.payments) {
        val perPersonShare = payment.cost / payment.members.size

        if (targetPerson == payment.payer) {
            // The target person made the payment
            debt -= payment.cost
        }

        if (targetPerson in payment.members) {
            // The target person is one of the participants
            debt += perPersonShare
        }
    }

    return debt
}

