package br.com.zupedu.ranyell.proposal.card

import br.com.zupedu.ranyell.proposal.card.wallet.Wallet
import br.com.zupedu.ranyell.proposal.shared.validation.Document
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
    @Column(unique = true)
    val number: String
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null
    val createAt: LocalDateTime = LocalDateTime.now()

    @Enumerated(EnumType.STRING)
    var statusCard: StatusCard = StatusCard.UNLOCKED

    fun blockCard() {
        statusCard = StatusCard.BLOCKED
    }
}

enum class StatusCard {
    BLOCKED,
    UNLOCKED;
}