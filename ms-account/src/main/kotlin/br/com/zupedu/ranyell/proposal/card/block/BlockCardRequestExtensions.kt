package br.com.zupedu.ranyell.proposal.card.block

import br.com.zupedu.ranyell.proposal.BlockCardRequest

fun BlockCardRequest.toBlockRequest(): BlockRequest {
    return BlockRequest(
        responsibleSystem = responsibleSystem,
        cardNumber = cardNumber
    )
}