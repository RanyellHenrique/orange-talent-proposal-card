package br.com.zupedu.ranyell.proposal.card.wallet

import br.com.zupedu.ranyell.proposal.card.CardRepository
import br.com.zupedu.ranyell.proposal.shared.exception.ResourceAlreadyExistingException
import br.com.zupedu.ranyell.proposal.shared.exception.ResourceNotFoundException
import io.micronaut.validation.Validated
import javax.inject.Inject
import javax.inject.Singleton
import javax.validation.Valid

@Validated
@Singleton
class WalletCardService(
    @Inject private val repository: WalletRepository,
    @Inject private val cardRepository: CardRepository
) {

    fun associate(@Valid request: WalletRequest) = cardRepository.findByNumber(request.cardNumber)
        ?.let {
            if (repository.existsByIssuerAndCardNumber(request.issuer, it.number))
                throw ResourceAlreadyExistingException("Card is already associated with the wallet")
            repository.save(request.toWallet(it))
        }
        ?: run { throw ResourceNotFoundException("Card not found") }
}