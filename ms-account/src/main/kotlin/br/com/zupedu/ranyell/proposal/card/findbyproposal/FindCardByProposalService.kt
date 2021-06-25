package br.com.zupedu.ranyell.proposal.card.findbyproposal

import br.com.zupedu.ranyell.proposal.card.CardRepository
import br.com.zupedu.ranyell.proposal.shared.exception.ResourceNotFoundException
import io.micronaut.validation.Validated
import javax.inject.Inject
import javax.inject.Singleton
import javax.transaction.Transactional
import javax.validation.Valid

@Validated
@Singleton
class FindCardByProposalService(
    @Inject private val repository: CardRepository
) {

    @Transactional
    fun find(@Valid request: FindByProposalRequest) = repository.findByProposalId(request.proposalId)
        ?.let { FindCardByProposalResponseConverter.converter(it) }
        ?: run { throw ResourceNotFoundException("Card not found") }

}