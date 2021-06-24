package br.com.zupedu.ranyell.proposal.card.wallet

import br.com.zupedu.ranyell.proposal.WalletCardRequest
import br.com.zupedu.ranyell.proposal.WalletCardServiceGrpc
import br.com.zupedu.ranyell.proposal.card.Card
import br.com.zupedu.ranyell.proposal.card.CardRepository
import io.grpc.ManagedChannel
import io.grpc.Status
import io.grpc.StatusRuntimeException
import io.micronaut.context.annotation.Factory
import io.micronaut.grpc.annotation.GrpcChannel
import io.micronaut.grpc.server.GrpcServerChannel
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions.*
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
internal class WalletCardEndPointTest(
    @Inject private val repository: WalletRepository,
    @Inject private val cardRepository: CardRepository,
    @Inject private val grpcClient: WalletCardServiceGrpc.WalletCardServiceBlockingStub
) {
    /*
    *  - Happy path                 -> ok
    *  - Already exists wallet      -> ok
    *  - Card not found             -> ok
    *  - Invalid data:
    *       * All invalid           -> ok
    *       * Card invalid          -> ok
    *       * Email invalid         -> ok
    *       * Issuer invalid        -> ok
    */

    lateinit var card: Card

    @BeforeEach
    internal fun setUp() {
        repository.deleteAll()
        cardRepository.deleteAll()
        card = cardRepository.save(Card("10079428002", "Bob Brown", 1L, "1234123412341234"))
    }

    @Test
    internal fun `should create a new wallet`() {
        //setting
        val request = WalletCardRequest.newBuilder()
            .setCardNumber(card.number)
            .setEmail("bob@email.com")
            .setIssuer("PAYPAL")
            .build()
        //action
        val response = grpcClient.associate(request)
        //validation
        with(response) {
            assertNotNull(id)
            assertEquals("ASSOCIATE", result)
        }
        assertEquals(1, repository.count())
        assertTrue(repository.existsByIssuerAndCardNumber(request.issuer, request.cardNumber))
    }

    @Test
    internal fun `should not create a new wallet when the card does not exist`() {
        //setting
        val request = WalletCardRequest.newBuilder()
            .setCardNumber("0123012301230123")
            .setEmail("bob@email.com")
            .setIssuer("PAYPAL")
            .build()
        //action
        val response = assertThrows<StatusRuntimeException> {
            grpcClient.associate(request)
        }
        //validation
        with(response) {
            assertEquals(Status.NOT_FOUND.code, status.code)
            assertEquals("Card not found", status.description)
        }
        assertEquals(0, repository.count())
    }

    @Test
    internal fun `should not create a new wallet when the digital wallet is already associated with the card`() {
        //setting
        val request = WalletCardRequest.newBuilder()
            .setCardNumber(card.number)
            .setEmail("bob@email.com")
            .setIssuer("PAYPAL")
            .build()
        repository.save(Wallet(request.email, request.issuer, card))
        //action
        val response = assertThrows<StatusRuntimeException> {
            grpcClient.associate(request)
        }
        //validation
        with(response) {
            assertEquals(Status.ALREADY_EXISTS.code, status.code)
            assertEquals("Card is already associated with the wallet", status.description)
        }
        assertEquals(1, repository.count())
    }

    @ParameterizedTest
    @MethodSource("createRequestArguments")
    internal fun `should not create a new notify when any argument is invalid`(
        cardNumber: String,
        email: String,
        issuer: String
    ) {
        //setting
        val request = WalletCardRequest.newBuilder()
            .setCardNumber(cardNumber)
            .setEmail(email)
            .setIssuer(issuer)
            .build()
        //action
        val response = assertThrows<StatusRuntimeException> {
            grpcClient.associate(request)
        }
        //validation
        with(response) {
            assertEquals("invalid parameters", status.description)
            assertEquals(Status.INVALID_ARGUMENT.code, status.code)
        }
        assertEquals(0, repository.count())
    }

    companion object {
        @JvmStatic
        fun createRequestArguments() = Stream.of(
            Arguments.of("", "", ""),                                   // All invalid
            Arguments.of("", "bob@email.com", "PAYPAL"),                // Card invalid
            Arguments.of("1234123445677894", "bo.com.br", "PAYPAL"),    // Email invalid
            Arguments.of("1234123445677894", "bob@email.com", "")       // Issuer invalid
        )
    }

    @Factory
    class Clients {
        @Singleton
        fun blockingStub(@GrpcChannel(GrpcServerChannel.NAME) channel: ManagedChannel): WalletCardServiceGrpc.WalletCardServiceBlockingStub {
            return WalletCardServiceGrpc.newBlockingStub(channel)
        }
    }

}