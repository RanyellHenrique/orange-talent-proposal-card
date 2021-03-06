package br.com.zupedu.ranyell.proposta.proposal

import io.micronaut.data.annotation.Repository
import io.micronaut.data.jpa.repository.JpaRepository

@Repository
interface ProposalRepository: JpaRepository<Proposal, Long> {
   fun existsByDocument(document: String): Boolean
   fun findByDocument(document: String): Proposal
}