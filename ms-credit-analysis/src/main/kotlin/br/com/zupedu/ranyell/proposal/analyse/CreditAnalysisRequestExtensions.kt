package br.com.zupedu.ranyell.proposal.analyse

import br.com.zupedu.ranyell.proposal.CreditAnalysisRequest

fun CreditAnalysisRequest.toAnalysisRequest(): AnalysisRequest {
    return AnalysisRequest(document, name, proposalId)
}