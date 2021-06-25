package br.com.zupedu.ranyell.proposal.analyze

import br.com.zupedu.ranyell.proposal.CreateCardServiceGrpc
import br.com.zupedu.ranyell.proposal.CreditAnalysisResponse
import br.com.zupedu.ranyell.proposal.SolicitationResult.APPROVED
import br.com.zupedu.ranyell.proposal.SolicitationResult.REFUSED
import br.com.zupedu.ranyell.proposal.external.CreateCardService
import io.micronaut.validation.Validated
import org.slf4j.LoggerFactory
import javax.inject.Inject
import javax.inject.Singleton
import javax.validation.Valid

@Singleton
@Validated
class CreditAnalysisService(
    @Inject private val createCardService: CreateCardService
) {
    private val LOOGER = LoggerFactory.getLogger(this::class.java)

    fun analyze(@Valid request: AnalysisRequest) = request.document.let {
        if (it[0] == '3')
            REFUSED.also { LOOGER.info("Proposal {} was rejected", request.proposalId) }
        else
            createCardService.creationCardOrder(request.toCreateCardRequest()).let { APPROVED }
    }.let { request.toCreditAnalysisResponse(it) }
}