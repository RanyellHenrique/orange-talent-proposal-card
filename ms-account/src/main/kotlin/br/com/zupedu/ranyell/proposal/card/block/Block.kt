package br.com.zupedu.ranyell.proposal.card.block

import br.com.zupedu.ranyell.proposal.card.Card
import java.time.LocalDateTime
import javax.persistence.*
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

@Entity
class Block(
    @field:NotBlank
    val responsibleSystem: String,
    @field:NotNull
    @ManyToOne
    val card: Card
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null
    val blockedIn: LocalDateTime = LocalDateTime.now()
    var active: Boolean = true
}