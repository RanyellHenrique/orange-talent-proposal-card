package br.com.zupedu.ranyell.proposal.card.notify

import br.com.zupedu.ranyell.proposal.NotifyCardRequest
import br.com.zupedu.ranyell.proposal.NotifyCardServiceGrpc
import br.com.zupedu.ranyell.proposal.card.Card
import br.com.zupedu.ranyell.proposal.card.CardRepository
import com.google.protobuf.Timestamp
import io.grpc.ManagedChannel
import io.grpc.Status
import io.grpc.StatusRuntimeException
import io.micronaut.context.annotation.Factory
import io.micronaut.grpc.annotation.GrpcChannel
import io.micronaut.grpc.server.GrpcServerChannel
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.stream.Stream
import javax.inject.Inject
import javax.inject.Singleton

@MicronautTest(transactional = false)
internal class NotifyCardEndPointTest(
    @Inject private val grpcClient: NotifyCardServiceGrpc.NotifyCardServiceBlockingStub,
    @Inject private val repository: NotifyRepository,
    @Inject private val cardRepository: CardRepository
) {

    lateinit var card: Card

    @BeforeEach
    internal fun setUp() {
        card = cardRepository.save(Card("10079428002", "Bob Brown", 1L, "1234123412341234"))
    }

    @AfterEach
    internal fun tearDown() {
        repository.deleteAll()
        cardRepository.deleteAll()
    }

    @Test
    internal fun `should create a new notification`() {
        //setting
        val request = NotifyCardRequest.newBuilder()
            .setCardNumber(card.number)
            .setDestiny("São Paulo")
            .setValidUntil(createTimestamp(2))
            .build()
        //action
        val response = grpcClient.notify(request)
        //validation
        with(response) {
            assertEquals("CREATED", result)
            assertEquals(1, repository.count())
        }
    }

    @Test
    internal fun `should not create a new notification when the card does not exist`() {
        //setting
        val request = NotifyCardRequest.newBuilder()
            .setCardNumber("123234888956231454")
            .setDestiny("São Paulo")
            .setValidUntil(createTimestamp())
            .build()
        //action
        val response = assertThrows<StatusRuntimeException> {
            grpcClient.notify(request)
        }
        with(response) {
            assertEquals("Card not found", status.description)
            assertEquals(Status.NOT_FOUND.code, status.code)
        }
    }

    @ParameterizedTest
    @MethodSource("createRequestArguments")
    internal fun `should not create a new notify when any argument is invalid`(
        cardNumber: String,
        destiny: String,
        validUntil: Timestamp
    ) {
        //setting
        val request = NotifyCardRequest.newBuilder()
            .setValidUntil(validUntil)
            .setDestiny(destiny)
            .setCardNumber(cardNumber)
            .build()
        //action
        val response = assertThrows<StatusRuntimeException> {
            grpcClient.notify(request)
        }
        //validation
        with(response) {
            assertEquals("invalid parameters", status.description)
            assertEquals(Status.INVALID_ARGUMENT.code, status.code)
        }

    }

    companion object {
        @JvmStatic
        fun createRequestArguments() = Stream.of(
            Arguments.of("", "", createTimestamp(-2)),                          // All invalid
            Arguments.of("", "São Paulo", createTimestamp()),                       // CardNumber invalid
            Arguments.of("0123123423452345", "", createTimestamp()),                // Destiny invalid
            Arguments.of("0123123423452345", "São Paulo", createTimestamp(-2)) // validUntil invalid
        )

        fun createTimestamp(day: Long = 2) = LocalDateTime.now()
            .plusDays(day).atOffset(ZoneOffset.UTC).toInstant().let {
                Timestamp.newBuilder()
                    .setSeconds(it.epochSecond)
                    .setNanos(it.nano)
                    .build()
            }
    }

    @Factory
    class Clients {
        @Singleton
        fun blockingStub(@GrpcChannel(GrpcServerChannel.NAME) channel: ManagedChannel)
                : NotifyCardServiceGrpc.NotifyCardServiceBlockingStub {
            return NotifyCardServiceGrpc.newBlockingStub(channel)
        }
    }
}