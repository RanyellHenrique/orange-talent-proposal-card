package br.com.zupedu.ranyell.proposal.card.block

import io.micronaut.data.annotation.Repository
import io.micronaut.data.jpa.repository.JpaRepository

@Repository
interface BlockRepository : JpaRepository<Block, Long> {
}