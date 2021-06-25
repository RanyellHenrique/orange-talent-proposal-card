package br.com.zupedu.ranyell.proposal.card

import br.com.zupedu.ranyell.proposal.card.block.Block
import br.com.zupedu.ranyell.proposal.card.notify.Notify
import br.com.zupedu.ranyell.proposal.card.wallet.Wallet
import br.com.zupedu.ranyell.proposal.shared.validation.Document
import java.time.LocalDateTime
import javax.persistence.*
import javax.persistence.CascadeType.*
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

@Entity
class Card(
    @field:NotBlank @field:Document
    @Column(unique = true)
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

    @OneToMany(mappedBy = "card", cascade = [PERSIST, MERGE])
    val blockades: List<Block> = arrayListOf()

    @OneToMany(mappedBy = "card", cascade = [PERSIST, MERGE])
    val notifications: List<Notify> = arrayListOf()

    @OneToMany(mappedBy = "card", cascade = [PERSIST, MERGE])
    val wallets: List<Wallet> = arrayListOf()

    fun blockCard() {
        statusCard = StatusCard.BLOCKED
    }
}

enum class StatusCard {
    BLOCKED,
    UNLOCKED;
}