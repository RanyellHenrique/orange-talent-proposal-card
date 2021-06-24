package br.com.zupedu.ranyell.proposal.card.notify

import br.com.zupedu.ranyell.proposal.card.Card
import java.time.LocalDate
import java.time.LocalDateTime
import javax.persistence.*
import javax.validation.constraints.Future
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

@Entity
class Notify(
    @field:NotBlank
    val cardNumber: String,
    @field:NotBlank
    val destiny: String,
    @field:NotNull @field:Future
    val validUntil: LocalDate,
    @ManyToOne
    @field:NotNull
    val card: Card
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null
    val createAt: LocalDateTime = LocalDateTime.now()
}