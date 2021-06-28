package br.com.zupedu.ranyell.proposta.card

import br.com.zupedu.ranyell.proposta.proposal.Proposal
import java.time.LocalDateTime
import javax.persistence.*
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

@Entity
class Card(
    @field:NotBlank
    val number: String,
    @field:NotBlank
    val name: String,
    @field:NotNull
    val createAt: LocalDateTime,
    @field:NotNull
    @Enumerated(EnumType.STRING)
    val statusCard: StatusCard,
    @OneToOne
    val proposal: Proposal
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null
}

enum class StatusCard {
    BLOCKED,
    UNLOCKED;
}
