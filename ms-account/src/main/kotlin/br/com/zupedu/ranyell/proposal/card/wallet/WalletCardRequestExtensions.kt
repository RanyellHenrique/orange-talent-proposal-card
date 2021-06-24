package br.com.zupedu.ranyell.proposal.card.wallet

import br.com.zupedu.ranyell.proposal.WalletCardRequest

fun WalletCardRequest.toWalletRequest(): WalletRequest {
    return WalletRequest(email, issuer, cardNumber)
}