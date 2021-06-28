package br.com.zupedu.ranyell.proposta.card

import br.com.zupedu.ranyell.proposta.proposal.Proposal
import io.micronaut.core.annotation.Introspected
import java.time.LocalDateTime
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

@Introspected
data class CardConsumerResponse(
    @field:NotBlank
    val name: String,
    @field:NotNull
    val proposalId: Long,
    @field:NotBlank
    val number: String,
    @field:NotNull
    val createAt: LocalDateTime,
    @field:NotNull
    var statusCard: StatusCard
) {

    fun toCard(proposal: Proposal): Card {
        return Card(number, name, createAt, statusCard, proposal)
    }
}