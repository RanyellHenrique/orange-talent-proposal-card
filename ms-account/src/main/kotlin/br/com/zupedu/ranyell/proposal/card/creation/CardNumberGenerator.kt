package br.com.zupedu.ranyell.proposal.card.creation

import javax.inject.Singleton

@Singleton
class CardNumberGenerator {

    fun generate(): String {
        val number = (1000_0000_0000_0000..9999_9999_9999_9999).random()
        return number.toString()
    }
}
