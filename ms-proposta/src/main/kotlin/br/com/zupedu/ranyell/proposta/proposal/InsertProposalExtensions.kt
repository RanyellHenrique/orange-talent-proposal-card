package br.com.zupedu.ranyell.proposta.proposal

import br.com.zupedu.ranyell.proposta.InsertProposalRequest
import java.math.BigDecimal

fun InsertProposalRequest.toProposalRequest(): ProposalRequest {
    return ProposalRequest(
        document = this.document,
        email = this.email,
        name = this.name,
        cep = this.cep,
        salary = BigDecimal(this.salary)
    )
}