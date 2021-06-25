package br.com.zupedu.ranyell.proposal.analyze

import br.com.zupedu.ranyell.proposal.CreditAnalysisRequest

fun CreditAnalysisRequest.toAnalysisRequest(): AnalysisRequest {
    return AnalysisRequest(document, name, proposalId)
}