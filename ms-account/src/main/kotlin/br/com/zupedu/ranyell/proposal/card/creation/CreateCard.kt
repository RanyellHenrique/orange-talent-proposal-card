package br.com.zupedu.ranyell.proposal.card.creation

import br.com.zupedu.ranyell.proposal.card.Card
import io.micronaut.core.annotation.Introspected
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

@Introspected
data class CreateCard(
    @field:NotBlank
    val document: String,
    @field:NotBlank
    val name: String,
    @field:NotNull
    val proposalId: Long
) {

    fun toCard(number: String): Card {
        return Card(document,name,proposalId,number)
    }

}
