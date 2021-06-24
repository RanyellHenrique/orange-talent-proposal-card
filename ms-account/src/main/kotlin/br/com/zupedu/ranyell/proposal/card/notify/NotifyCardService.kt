package br.com.zupedu.ranyell.proposal.card.notify

import br.com.zupedu.ranyell.proposal.card.CardRepository
import br.com.zupedu.ranyell.proposal.shared.exception.ResourceNotFoundException
import io.micronaut.validation.Validated
import javax.inject.Inject
import javax.inject.Singleton
import javax.validation.Valid

@Singleton
@Validated
class NotifyCardService(
    @Inject private val repository: NotifyRepository,
    @Inject private val cardRepository: CardRepository
) {

    fun notify(@Valid request: NotifyRequest) = cardRepository.findByNumber(request.cardNumber)
        ?.let {
            repository.save(request.toNotify(it))
            "CREATED"
        }
        ?: run {
            throw ResourceNotFoundException("Card not found")
        }
}