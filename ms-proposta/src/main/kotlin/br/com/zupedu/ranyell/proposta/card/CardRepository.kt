package br.com.zupedu.ranyell.proposta.card

import io.micronaut.data.annotation.Repository
import io.micronaut.data.jpa.repository.JpaRepository

@Repository
interface CardRepository : JpaRepository<Card, Long> {
}