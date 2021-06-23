package br.com.zupedu.ranyell.proposal.card.creation

import br.com.zupedu.ranyell.proposal.CreateCardRequest
import br.com.zupedu.ranyell.proposal.CreateCardServiceGrpc
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
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream
import javax.inject.Inject
import javax.inject.Singleton

@MicronautTest(transactional = false, environments = ["test"])
internal class CreateCardEndPointTest(
    @Inject private val grpcClient: CreateCardServiceGrpc.CreateCardServiceBlockingStub,
    @Inject private val repository: CardRepository
) {
    /*
    *  - Happy path                 -> ok
    *  - Duplicate proposalId       -> ok
    *  - Invalid data:
    *       * All invalid           -> ok
    *       * Document invalid      -> ok
    *       * ProposalId invalid    -> ok
    *       * Name invalid          -> ok
    */

    @BeforeEach
    internal fun setUp() {
        repository.deleteAll()
    }

    @Test
    internal fun `should create a new card`() {
        //setting
        val request = CreateCardRequest.newBuilder()
            .setDocument("10079428002")
            .setProposalId(1L)
            .setName("Bob Brown")
            .build()
        //action
        val response = grpcClient.create(request)
        //validation
        with(response) {
            assertEquals("Card in process of creation", message)
            assertTrue(repository.existsByProposalId(request.proposalId))
            assertEquals(1, repository.count())
        }
    }

    @Test
    internal fun `should not create a new card when a card with the proposalId entered already exists`() {
        //setting
        val request = CreateCardRequest.newBuilder()
            .setDocument("10079428002")
            .setProposalId(1L)
            .setName("Bob Brown")
            .build()
        repository.save(Card(request.document, request.name, request.proposalId, CardNumberGenerator().generate()))
        //action
        val response = assertThrows<StatusRuntimeException> {
            grpcClient.create(request)
        }
        //validation
        with(response) {
            assertEquals(Status.ALREADY_EXISTS.code, status.code)
            assertEquals("this proposal already has a card", status.description)
        }
    }

    @ParameterizedTest
    @MethodSource("createRequestArguments")
    internal fun `should not create a new card when any argument is invalid`(
        document: String?,
        proposalId: Long?,
        name: String?
    ) {
        //setting
        val request = proposalId?.let {
            CreateCardRequest.newBuilder()
                .setName(name)
                .setProposalId(it)
                .setDocument(document)
                .build()
        } ?: run {
            CreateCardRequest.newBuilder()
                .setName(name)
                .setDocument(document)
                .build()
        }
        //action
        val response = assertThrows<StatusRuntimeException> {
            grpcClient.create(request)
        }
        //validation
        with(response) {
            assertEquals(Status.INVALID_ARGUMENT.code, status.code)
            assertEquals("invalid parameters", status.description)
        }
    }

    companion object {
        @JvmStatic
        fun createRequestArguments() = Stream.of(
            Arguments.of("", null, ""),                     //All invalid
            Arguments.of("0548853", 1L, "Bob Brown"),       //Document invalid
            Arguments.of("24405039089", null, "Bob Brown"), //ProposalId invalid
            Arguments.of("24405039089", 1L, ""),            //Name invalid
        )
    }


    @Factory
    class Clients {
        @Singleton
        fun blockingStub(@GrpcChannel(GrpcServerChannel.NAME) channel: ManagedChannel)
                : CreateCardServiceGrpc.CreateCardServiceBlockingStub {
            return CreateCardServiceGrpc.newBlockingStub(channel)
        }
    }

}