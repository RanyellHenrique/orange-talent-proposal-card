package br.com.zupedu.ranyell.proposal.card.wallet

import br.com.zupedu.ranyell.proposal.card.Card
import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank

class WalletRequest(
    @field:NotBlank @field:Email
    val email: String,
    @field:NotBlank
    val issuer: String,
    @field:NotBlank
    val cardNumber: String
) {

    fun toWallet(card: Card): Wallet {
        return Wallet(email, issuer, card)
    }
}