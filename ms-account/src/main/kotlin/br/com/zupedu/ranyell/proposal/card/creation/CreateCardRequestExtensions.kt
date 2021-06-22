package br.com.zupedu.ranyell.proposal.card.creation

import br.com.zupedu.ranyell.proposal.CreateCardRequest

fun CreateCardRequest.toCreateCard() = CreateCard(
    document = document,
    name = name,
    proposalId = proposalId
)