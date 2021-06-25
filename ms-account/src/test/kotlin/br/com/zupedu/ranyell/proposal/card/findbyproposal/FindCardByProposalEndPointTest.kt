package br.com.zupedu.ranyell.proposal.card.findbyproposal

import br.com.zupedu.ranyell.proposal.FindCardByProposalGrpc
import br.com.zupedu.ranyell.proposal.FindCardByProposalRequest
import br.com.zupedu.ranyell.proposal.card.Card
import br.com.zupedu.ranyell.proposal.card.CardRepository
import io.grpc.ManagedChannel
import io.grpc.Status
import io.grpc.StatusRuntimeException
import io.micronaut.context.annotation.Factory
import io.micronaut.grpc.annotation.GrpcChannel
import io.micronaut.grpc.server.GrpcServerChannel
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import javax.inject.Inject
import javax.inject.Singleton

@MicronautTest(transactional = false)
internal class FindCardByProposalEndPointTest(
    @Inject private val grpcClient: FindCardByProposalGrpc.FindCardByProposalBlockingStub,
    @Inject private val repository: CardRepository
) {

    /*
    *  - Happy path                 -> ok
    *  - Card not found             -> ok
    *  - Invalid data:
    *       * proposalId invalid    -> ok
    */


    lateinit var card: Card

    @BeforeEach
    internal fun setUp() {
        repository.deleteAll()
        card = repository.save(Card("10079428002", "Bob Brown", 1L, "1234123412341234"))
    }

    @Test
    internal fun `should return a card`() {
        //setting
        val request = FindCardByProposalRequest.newBuilder()
            .setProposalId(1)
            .build()
        //action
        val response = grpcClient.find(request)
        //validation
        with(response) {
            assertEquals(card.name, name)
            assertEquals(card.number, cardNumber)
            assertEquals(card.proposalId, proposalId)
        }
    }

    @Test
    internal fun `not return a card when the proposal does not exist`() {
        //setting
        val request = FindCardByProposalRequest.newBuilder()
            .setProposalId(Long.MAX_VALUE)
            .build()
        //action
        val response = assertThrows<StatusRuntimeException> {
            grpcClient.find(request)
        }
        //validation
        with(response) {
            assertEquals("Card not found", status.description)
            assertEquals(Status.NOT_FOUND.code, status.code)
        }
    }

    @Test
    internal fun `not return a card when any arguments is invalid`() {
        //setting
        val request = FindCardByProposalRequest.newBuilder()
            .setProposalId(-4)
            .build()
        //action
        val response = assertThrows<StatusRuntimeException> {
            grpcClient.find(request)
        }
        //validation
        with(response) {
            assertEquals("invalid parameters", status.description)
            assertEquals(Status.INVALID_ARGUMENT.code, status.code)
        }
    }

    @Factory
    class Clients {
        @Singleton
        fun blockingStub(@GrpcChannel(GrpcServerChannel.NAME) channel: ManagedChannel)
                : FindCardByProposalGrpc.FindCardByProposalBlockingStub {
            return FindCardByProposalGrpc.newBlockingStub(channel)
        }
    }
}