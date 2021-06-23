package br.com.zupedu.ranyell.proposal.card.block

import br.com.zupedu.ranyell.proposal.card.Card
import io.micronaut.core.annotation.Introspected
import javax.validation.constraints.NotBlank

@Introspected
data class BlockRequest(
    @field:NotBlank
    val responsibleSystem: String,
    @field:NotBlank
    val cardNumber: String
) {
    fun toBlock(card: Card): Block {
        return Block(responsibleSystem, card)
    }
}