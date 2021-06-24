package br.com.zupedu.ranyell.proposal.card.notify

import br.com.zupedu.ranyell.proposal.NotifyCardRequest
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneOffset

fun NotifyCardRequest.toNotifyRequest(): NotifyRequest {
    return NotifyRequest(
        cardNumber = cardNumber,
        destiny = destiny,
        validUntil = validUntil.let {
            Instant.ofEpochSecond(it.seconds)
                .atOffset(ZoneOffset.UTC)
                .toLocalDate()
        }
    )
}