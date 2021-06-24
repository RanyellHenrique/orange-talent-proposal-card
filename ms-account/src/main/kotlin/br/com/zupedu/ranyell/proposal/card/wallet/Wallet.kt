package br.com.zupedu.ranyell.proposal.card.wallet

import br.com.zupedu.ranyell.proposal.card.Card
import java.time.LocalDateTime
import javax.persistence.*
import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

@Entity
class Wallet(
    @field:NotBlank @field:Email
    val email: String,
    @field:NotBlank
    val issuer: String,
    @field:NotNull
    @ManyToOne
    val card: Card
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null
    val createAt: LocalDateTime = LocalDateTime.now()
}