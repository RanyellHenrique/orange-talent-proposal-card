package br.com.zupedu.ranyell.proposal.card.block

import br.com.zupedu.ranyell.proposal.BlockCardRequest
import br.com.zupedu.ranyell.proposal.BlockCardServiceGrpc
import br.com.zupedu.ranyell.proposal.card.Card
import br.com.zupedu.ranyell.proposal.card.CardRepository
import br.com.zupedu.ranyell.proposal.card.StatusCard.BLOCKED
import io.grpc.ManagedChannel
import io.grpc.Status
import io.grpc.StatusRuntimeException
import io.micronaut.context.annotation.Factory
import io.micronaut.grpc.annotation.GrpcChannel
import io.micronaut.grpc.server.GrpcServerChannel
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream
import javax.inject.Inject
import javax.inject.Singleton

@MicronautTest(transactional = false)
internal class BlockCardEndPointTest(
    @Inject private val grpcClient: BlockCardServiceGrpc.BlockCardServiceBlockingStub,
    @Inject private val repository: BlockRepository,
    @Inject private val cardRepository: CardRepository
) {

    /*
    *  - Happy path                 -> ok
    *  - Card already blocked       -> ok
    *  - Card not found             -> ok
    *  - Invalid data:
    *       * All invalid           -> ok
    *       * Card invalid          -> ok
    *       * System invalid        -> ok
    */

    lateinit var card: Card

    @BeforeEach
    internal fun setUp() {
        repository.deleteAll()
        cardRepository.deleteAll()
        card = Card("77877779070", "Bob Brow", 1L, "1234456756789874")
        cardRepository.save(card)
    }

    @Test
    internal fun `should block a card`() {
        //setting
        val request = BlockCardRequest.newBuilder()
            .setCardNumber(card.number)
            .setResponsibleSystem("Firefox")
            .build()
        //action
        val response = grpcClient.block(request)
        //validation
        val cardBlock = cardRepository.findByNumber(card.number)
        with(response) {
            assertNotNull(cardBlock)
            assertEquals(BLOCKED, cardBlock!!.statusCard)
            assertEquals("BLOCKED", message)
        }
    }

    @Test
    internal fun `should not block a card when it is already blocked`() {
        //setting
        val request = BlockCardRequest.newBuilder()
            .setCardNumber(card.number)
            .setResponsibleSystem("Firefox")
            .build()
        grpcClient.block(request)
        //action
        val response = assertThrows<StatusRuntimeException> {
            grpcClient.block(request)
        }
        //validation
        with(response) {
            assertEquals(Status.ALREADY_EXISTS.code, status.code)
            assertEquals("Card is already blocked", status.description)
        }
    }

    @Test
    internal fun `should not block a card when card not exists`() {
        //setting
        val request = BlockCardRequest.newBuilder()
            .setCardNumber("1234123412341234")
            .setResponsibleSystem("Firefox")
            .build()
        //action
        val response = assertThrows<StatusRuntimeException> {
            grpcClient.block(request)
        }
        //validation
        with(response) {
            assertEquals(Status.NOT_FOUND.code, status.code)
            assertEquals("card not found", status.description)
        }
    }

    @ParameterizedTest
    @MethodSource("blockRequestArguments")
    internal fun `should not block card when any argument is invalid`(cardNumber: String?, responsibleSystem: String?) {
        //setting
        val request = BlockCardRequest.newBuilder()
            .setResponsibleSystem(responsibleSystem)
            .setCardNumber(cardNumber)
            .build()
        //action
        val response = assertThrows<StatusRuntimeException> {
            grpcClient.block(request)
        }
        //validation
        with(response) {
            assertEquals(Status.INVALID_ARGUMENT.code, status.code)
            assertEquals("invalid parameters", status.description)
        }
    }

    companion object {
        @JvmStatic
        fun blockRequestArguments() = Stream.of(
            Arguments.of("", ""),                   //All invalid
            Arguments.of("1234123412341234", ""),   //ResponsibleSystem invalid
            Arguments.of("","Firefox") //CardNumber invalid
        )
    }


    @Factory
    class Clients {
        @Singleton
        fun blockingStub(@GrpcChannel(GrpcServerChannel.NAME) channel: ManagedChannel)
                : BlockCardServiceGrpc.BlockCardServiceBlockingStub {
            return BlockCardServiceGrpc.newBlockingStub(channel)
        }
    }

}