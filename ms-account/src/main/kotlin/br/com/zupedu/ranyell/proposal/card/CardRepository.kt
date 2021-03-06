package br.com.zupedu.ranyell.proposal.card

import io.micronaut.data.annotation.Repository
import io.micronaut.data.jpa.repository.JpaRepository

@Repository
interface CardRepository : JpaRepository<Card, Long> {

    fun existsByProposalId(proposalId: Long): Boolean
    fun findByNumber(number: String): Card?
    fun findByProposalId(proposalId: Long): Card?
    fun existsByDocument(document: String): Boolean
}
