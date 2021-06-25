package br.com.zupedu.ranyell.proposal.card.findbyproposal

import br.com.zupedu.ranyell.proposal.BlockCard
import br.com.zupedu.ranyell.proposal.FindCardByProposalResponse
import br.com.zupedu.ranyell.proposal.NotifyCard
import br.com.zupedu.ranyell.proposal.WalletCard
import br.com.zupedu.ranyell.proposal.card.Card
import br.com.zupedu.ranyell.proposal.card.block.Block
import br.com.zupedu.ranyell.proposal.card.notify.Notify
import br.com.zupedu.ranyell.proposal.card.wallet.Wallet
import com.google.protobuf.Timestamp
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneOffset

class FindCardByProposalResponseConverter {

    companion object {
        fun converter(card: Card): FindCardByProposalResponse {
            return FindCardByProposalResponse.newBuilder()
                .setCardNumber(card.number)
                .setCreateAt(timestamp(card.createAt))
                .setName(card.name)
                .setProposalId(card.proposalId)
                .addAllBlockades(blockCard(card.blockades))
                .addAllNotifications(notifyCard(card.notifications))
                .addAllWallets(walletCard(card.wallets))
                .build()
        }

        private fun blockCard(blocks: List<Block>) = blocks.map {
            BlockCard.newBuilder()
                .setId(it.id!!)
                .setResponsibleSystem(it.responsibleSystem)
                .setBlockedIn(timestamp(it.blockedIn))
                .setActive(it.active)
                .build()
        }

        private fun notifyCard(notifications: List<Notify>) = notifications.map {
            NotifyCard.newBuilder()
                .setValidUntil(timestamp(LocalDateTime.of(it.validUntil, LocalTime.MIN)))
                .setDestiny(it.destiny)
                .build()
        }

        private fun walletCard(wallets: List<Wallet>) = wallets.map {
            WalletCard.newBuilder()
                .setId(it.id!!)
                .setEmail(it.email)
                .setIssuer(it.issuer)
                .setCreateAt(timestamp(it.createAt))
                .build()
        }

        private fun timestamp(date: LocalDateTime) = date.let {
            Timestamp.newBuilder()
                .setSeconds(it.toEpochSecond(ZoneOffset.UTC))
                .setNanos(it.nano)
                .build()
        }

    }
}