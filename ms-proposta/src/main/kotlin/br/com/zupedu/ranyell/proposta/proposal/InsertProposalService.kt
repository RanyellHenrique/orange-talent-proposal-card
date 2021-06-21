package br.com.zupedu.ranyell.proposta.proposal

import br.com.zupedu.ranyell.proposta.shared.exception.ResourceAlreadyExistingException
import br.com.zupedu.ranyell.proposta.shared.external.creditanalysis.CreditAnalysis
import io.micronaut.validation.Validated
import javax.inject.Inject
import javax.inject.Singleton
import javax.transaction.Transactional
import javax.validation.Valid

@Validated
@Singleton
class InsertProposalService(
    @Inject private val proposalRepository: ProposalRepository,
    @Inject private val creditAnalysis: CreditAnalysis
) {

    @Transactional
    fun insert(@Valid request: ProposalRequest) = request
        .also {
            if (proposalRepository.existsByDocument(it.document))
                throw ResourceAlreadyExistingException("This document already has a proposal")
        }
        .let { proposalRepository.save(it.toProposal()) }
        .apply { creditAnalysis.analyze(this) }

}