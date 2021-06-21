package br.com.zupedu.ranyell.proposta.shared.external.creditanalysis

import br.com.zupedu.ranyell.proposta.CreditAnalysisRequest
import br.com.zupedu.ranyell.proposta.CreditAnalysisServiceGrpc
import br.com.zupedu.ranyell.proposta.proposal.Proposal
import br.com.zupedu.ranyell.proposta.proposal.StatusProposal
import org.slf4j.LoggerFactory
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CreditAnalysis(
    @Inject private val grpcClient: CreditAnalysisServiceGrpc.CreditAnalysisServiceBlockingStub
) {
    private val LOGGER = LoggerFactory.getLogger(this::class.java)

    fun analyze(proposal: Proposal) {
        grpcClient.analyze(toCreditAnalysisRequest(proposal))
            .also { proposal.statusProposal = StatusProposal.valueOf(it.solicitationResult.name) }
            .also { LOGGER.info("proposal with id equal to {} is {}", it.proposalId, it.solicitationResult.name) }
    }

    private fun toCreditAnalysisRequest(proposal: Proposal) = CreditAnalysisRequest.newBuilder()
        .setDocument(proposal.document)
        .setName(proposal.name)
        .setProposalId(proposal.id!!)
        .build()
}