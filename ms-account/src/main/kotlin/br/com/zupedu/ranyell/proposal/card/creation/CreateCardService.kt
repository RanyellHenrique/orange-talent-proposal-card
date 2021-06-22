package br.com.zupedu.ranyell.proposal.card.creation

import br.com.zupedu.ranyell.proposal.card.CardRepository
import io.micronaut.validation.Validated
import javax.inject.Inject
import javax.inject.Singleton
import javax.validation.Valid

@Validated
@Singleton
class CreateCardService(
    @Inject private val repository: CardRepository,
    @Inject private val generator: CardNumberGenerator,
    @Inject private val producer: CreateCardProducer
) {

    fun create(@Valid request: CreateCard) = request.toCard(generator.generate())
        .run { repository.save(this) }
        .also { producer.send(it.proposalId, CardProducerResponse(it)) }
        .let { "Card in process of creation" }

}
