package br.com.zupedu.ranyell.proposal.card.creation

import br.com.zupedu.ranyell.proposal.card.Card
import br.com.zupedu.ranyell.proposta.shared.validation.Document
import io.micronaut.core.annotation.Introspected
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import javax.validation.constraints.Positive

@Introspected
data class CreateCard(
    @field:NotBlank @field:Document
    val document: String,
    @field:NotBlank
    val name: String,
    @field:NotNull @field:Positive
    val proposalId: Long
) {

    fun toCard(number: String): Card {
        return Card(document, name, proposalId, number)
    }

}
