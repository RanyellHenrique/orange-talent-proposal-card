package br.com.zupedu.ranyell.proposal.card.notify

import br.com.zupedu.ranyell.proposal.card.Card
import io.micronaut.core.annotation.Introspected
import java.time.LocalDate
import javax.validation.constraints.Future
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

@Introspected
data class NotifyRequest(
    @field:NotBlank
    val cardNumber: String,
    @field:NotBlank
    val destiny: String,
    @field:NotNull @field:Future
    val validUntil: LocalDate
) {
    fun toNotify(card: Card): Notify {
        return Notify(cardNumber, destiny, validUntil, card)
    }
}