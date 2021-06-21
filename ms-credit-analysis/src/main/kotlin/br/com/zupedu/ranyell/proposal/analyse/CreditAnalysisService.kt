package br.com.zupedu.ranyell.proposal.analyse

import br.com.zupedu.ranyell.proposal.CreditAnalysisResponse
import br.com.zupedu.ranyell.proposal.SolicitationResult.APPROVED
import br.com.zupedu.ranyell.proposal.SolicitationResult.REFUSED
import io.micronaut.validation.Validated
import javax.inject.Singleton
import javax.validation.Valid

@Singleton
@Validated
class CreditAnalysisService {

    fun analize(@Valid request: AnalysisRequest): CreditAnalysisResponse {
        return request.document.let {
            if (it[0] == '3') REFUSED else APPROVED
        }.let {
            request.toCreditAnalysisResponse(it)
        }
    }
}