package br.com.zupedu.ranyell.proposta.proposal


import br.com.zupedu.ranyell.proposta.shared.validation.Cep
import br.com.zupedu.ranyell.proposta.shared.validation.Document
import io.micronaut.core.annotation.Introspected
import java.math.BigDecimal
import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import javax.validation.constraints.PositiveOrZero

@Introspected
data class ProposalRequest(
    @field:NotBlank @field:Document
    val document: String,
    @field:NotBlank @field:Email
    val email: String,
    @field:NotBlank
    val name: String,
    @field:NotBlank
    val address: String,
    @field:NotNull @field:PositiveOrZero
    val salary: BigDecimal
) {
    fun toProposal() = Proposal(
        document = this.document,
        email = this.email,
        name = this.name,
        address = this.address,
        salary = this.salary
    )
}