package br.com.zupedu.ranyell.proposal.card.wallet

import io.micronaut.data.annotation.Repository
import io.micronaut.data.jpa.repository.JpaRepository

@Repository
interface WalletRepository: JpaRepository<Wallet, Long> {
    fun existsByIssuerAndCardNumber(issuer: String, cardNumber: String): Boolean
}
