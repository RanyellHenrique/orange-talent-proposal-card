package br.com.zupedu.ranyell.proposal.card.block

import br.com.zupedu.ranyell.proposal.card.CardRepository
import br.com.zupedu.ranyell.proposal.card.StatusCard.BLOCKED
import br.com.zupedu.ranyell.proposal.shared.exception.ResourceAlreadyExistingException
import io.micronaut.validation.Validated
import org.apache.kafka.common.errors.ResourceNotFoundException
import javax.inject.Inject
import javax.inject.Singleton
import javax.transaction.Transactional
import javax.validation.Valid

@Validated
@Singleton
class BlockCardService(
    @Inject private val repository: BlockRepository,
    @Inject private val cardRepository: CardRepository
) {

    @Transactional
    fun block(@Valid request: BlockRequest) = cardRepository.findByNumber(request.cardNumber)
        ?.let {
            if (it.statusCard == BLOCKED) {
                throw ResourceAlreadyExistingException("Card is already blocked")
            }
            repository.save(request.toBlock(it))
            it.blockCard()
        }
        ?: run { throw ResourceNotFoundException("card not found") }
}