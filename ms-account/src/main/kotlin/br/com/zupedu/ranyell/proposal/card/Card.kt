package br.com.zupedu.ranyell.proposal.card

import br.com.zupedu.ranyell.proposta.shared.validation.Document
import java.time.LocalDateTime
import javax.persistence.*
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

@Entity
class Card(
    @field:NotBlank @field:Document
    val document: String,
    @field:NotBlank
    val name: String,
    @field:NotNull
    val proposalId: Long,
    @field:NotBlank
    val number: String
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null
    val createAt: LocalDateTime = LocalDateTime.now()
    @Enumerated(EnumType.STRING)
    var statusCard: StatusCard = StatusCard.UNLOCKED
}

enum class StatusCard {
    BLOCKED,
    UNLOCKED;
}