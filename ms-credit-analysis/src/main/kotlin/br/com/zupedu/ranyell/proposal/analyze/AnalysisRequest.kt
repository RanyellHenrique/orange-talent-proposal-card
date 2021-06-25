package br.com.zupedu.ranyell.proposal.analyze

import br.com.zupedu.ranyell.proposal.CreateCardRequest
import br.com.zupedu.ranyell.proposal.CreditAnalysisResponse
import br.com.zupedu.ranyell.proposal.SolicitationResult
import br.com.zupedu.ranyell.proposal.shared.validation.Document
import io.micronaut.core.annotation.Introspected
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

@Introspected
data class AnalysisRequest(
    @field:Document @field:NotBlank
    val document: String,
    @field:NotBlank
    val name: String,
    @field:NotNull
    val proposalId: Long
) {
    fun toCreditAnalysisResponse(result: SolicitationResult): CreditAnalysisResponse {
        return CreditAnalysisResponse.newBuilder()
            .setDocument(document)
            .setName(name)
            .setProposalId(proposalId)
            .setSolicitationResult(result)
            .build()
    }

    fun toCreateCardRequest() = CreateCardRequest.newBuilder()
        .setDocument(document)
        .setName(name)
        .setProposalId(proposalId)
        .build()
}

