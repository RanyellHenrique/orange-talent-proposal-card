package br.com.zupedu.ranyell.proposal.card.notify

import io.micronaut.data.annotation.Repository
import io.micronaut.data.jpa.repository.JpaRepository

@Repository
interface NotifyRepository: JpaRepository<Notify, Long> {

}
