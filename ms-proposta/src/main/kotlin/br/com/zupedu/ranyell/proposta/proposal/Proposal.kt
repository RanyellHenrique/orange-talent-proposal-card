package br.com.zupedu.ranyell.proposta.proposal

import br.com.zupedu.ranyell.proposta.shared.validation.Document
import java.math.BigDecimal
import java.time.LocalDateTime
import javax.persistence.*
import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import javax.validation.constraints.PositiveOrZero

@Entity
class Proposal(
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
    @Id
    @GeneratedValue
    val id: Long? = null
    val createdAt: LocalDateTime = LocalDateTime.now()
    @Enumerated(EnumType.STRING)
    var statusProposal: StatusProposal? = null
}

enum class StatusProposal {
    REFUSED,
    APPROVED
}
