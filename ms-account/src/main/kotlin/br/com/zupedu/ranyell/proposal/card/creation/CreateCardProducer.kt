package br.com.zupedu.ranyell.proposal.card.creation

import br.com.zupedu.ranyell.proposal.card.Card
import br.com.zupedu.ranyell.proposal.card.StatusCard
import io.micronaut.configuration.kafka.annotation.KafkaClient
import io.micronaut.configuration.kafka.annotation.KafkaKey
import io.micronaut.configuration.kafka.annotation.Topic
import java.time.LocalDateTime
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

@KafkaClient
interface CreateCardProducer {

    @Topic("card-create")
    fun send(@KafkaKey id: Long, card: CardProducerResponse)
}

data class CardProducerResponse(
    val name: String,
    @field:NotNull
    val proposalId: Long,
    @field:NotBlank
    val number: String,
    val createAt: LocalDateTime,
    var statusCard: StatusCard
) {
    constructor(card: Card): this(
        name = card.name,
        proposalId = card.proposalId,
        number = card.number,
        createAt = card.createAt,
        statusCard = card.statusCard
    )
}


