package br.com.zupedu.ranyell.proposta.card

import br.com.zupedu.ranyell.proposta.proposal.Proposal
import br.com.zupedu.ranyell.proposta.proposal.ProposalRepository
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.awaitility.core.ConditionTimeoutException
import org.awaitility.kotlin.await
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.concurrent.TimeUnit.SECONDS
import javax.inject.Inject

@MicronautTest(transactional = false, environments = ["test"])
internal class AssociateCardTest(
    @Inject private val cardRepository: CardRepository,
    @Inject private val proposalRepository: ProposalRepository
) {
    @Inject
    private lateinit var cardProducer: CardProducer

    private lateinit var proposal: Proposal

    @BeforeEach
    internal fun setUp() {
        cardRepository.deleteAll()
        proposalRepository.deleteAll()
        proposal = proposalRepository.save(proposal())
    }

    @Test
    internal fun `should associate a card to the proposal`() {
        //action
        cardProducer.send(1L, cardConsumerResponse(proposal.id!!))
        await.atMost(3, SECONDS).until { cardRepository.existsByNumber("123456789456789456") }
        //validation
        cardRepository.findByNumber("123456789456789456")?.let {
            assertEquals("Bob Brown", it.name)
            assertEquals(StatusCard.UNLOCKED, it.statusCard)
            assertNotNull(it.createAt)
            assertNotNull(it.proposal)
            assertNotNull(it.id)
            assertEquals("123456789456789456", it.number)
        }
    }

    @Test
    internal fun `should not associate a card with the proposal when the proposal does not exist`() {
        //action
        val error = assertThrows<ConditionTimeoutException> {
            cardProducer.send(Long.MAX_VALUE, cardConsumerResponse(Long.MAX_VALUE, "547895456995211223589"))
            await.atMost(3, SECONDS).until { cardRepository.existsByNumber("547895456995211223589") }
        }
        //validation
        println("\n\n\n\n\n ${error.message} \n\n\n\n")
        assertEquals(0, cardRepository.count())
        assertFalse(cardRepository.existsByNumber("547895456995211223589"))
    }

    private fun cardConsumerResponse(proposalId: Long, number: String = "123456789456789456") = CardConsumerResponse(
        proposal.name,
        proposalId,
        number,
        LocalDateTime.now(),
        StatusCard.UNLOCKED
    )

    private fun proposal() = Proposal(
        "05187022199",
        "bob@email.com",
        "Bob Brown",
        "address",
        BigDecimal("2500.00")
    )
}