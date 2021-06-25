package br.com.zupedu.ranyell.proposal.card.findbyproposal

import io.micronaut.core.annotation.Introspected
import javax.validation.constraints.NotNull
import javax.validation.constraints.Positive

@Introspected
data class FindByProposalRequest(
    @field:NotNull @field:Positive
    val proposalId: Long
) {
}