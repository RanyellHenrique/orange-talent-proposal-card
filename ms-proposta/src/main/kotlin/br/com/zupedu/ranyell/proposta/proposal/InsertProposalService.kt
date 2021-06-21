package br.com.zupedu.ranyell.proposta.proposal

import br.com.zupedu.ranyell.proposta.integration.AddressClient
import br.com.zupedu.ranyell.proposta.integration.AddressResponse
import br.com.zupedu.ranyell.proposta.shared.exception.ResourceAlreadyExistingException
import br.com.zupedu.ranyell.proposta.shared.exception.ResourceNotFoundException
import io.micronaut.validation.Validated
import javax.inject.Inject
import javax.inject.Singleton
import javax.transaction.Transactional
import javax.validation.Valid

@Validated
@Singleton
class InsertProposalService(
    @Inject private val proposalRepository: ProposalRepository,
    @Inject private val addressClient: AddressClient
) {

    @Transactional
    fun insert(@Valid request: ProposalRequest) = request
        .also {
            if (proposalRepository.existsByDocument(it.document!!))
                throw ResourceAlreadyExistingException("This document already has a proposal")
        }
        .run {
            when (val address = addressClient.findByCep(cep!!).body()) {
                is AddressResponse -> toProposal(address)
                else -> throw ResourceNotFoundException("Cep not found")
            }
        }
        .let { proposalRepository.save(it) }

}