package br.com.zupedu.ranyell.proposta.proposal

import br.com.zupedu.ranyell.proposta.*
import br.com.zupedu.ranyell.proposta.shared.external.creditanalysis.CreditAnalysisFactory
import io.grpc.ManagedChannel
import io.grpc.Status
import io.grpc.StatusRuntimeException
import io.micronaut.context.annotation.Factory
import io.micronaut.context.annotation.Replaces
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
import org.mockito.Mockito.*
import java.math.BigDecimal
import java.util.stream.Stream
import javax.inject.Singleton

@MicronautTest(transactional = false)
internal class InsertProposalEndPointTest(
    private val repository: ProposalRepository,
    private val grpcClient: InsertProposalServiceGrpc.InsertProposalServiceBlockingStub,
    private val grpcClientCreditAnalysis: CreditAnalysisServiceGrpc.CreditAnalysisServiceBlockingStub
) {

    /*
    *  - Happy path
    *       * APPROVED      -> ok
    *       * REFUSED       -> ok
    *  - Invalid data       -> ok
    *  - Duplicate proposal -> ok
    */

    @BeforeEach
    internal fun setUp() {
        repository.deleteAll()
    }

    @ParameterizedTest
    @MethodSource("proposalTestArguments")
    internal fun `should register a new proposal`(document: String, status: String) {
        //setting
        `when`(grpcClientCreditAnalysis.analyze(any())).thenReturn(
            CreditAnalysisResponse
                .newBuilder()
                .setDocument(document)
                .setName(insertProposalRequest(document).name)
                .setProposalId(1L)
                .setSolicitationResult(SolicitationResult.valueOf(status))
                .build()
        )
        //action
        val response = grpcClient.insert(insertProposalRequest(document))
        //validation
        with(response) {
            assertNotNull(propostaId)
            assertEquals(1, repository.count())
            assertTrue(repository.existsByDocument(document))
        }
        repository.findByDocument(document)?.let {
            assertEquals(StatusProposal.valueOf(status), it.statusProposal)
        }
    }

    companion object{
        @JvmStatic
        fun proposalTestArguments() = Stream.of(
            Arguments.of("33926822090", "REFUSED"),
            Arguments.of("84070202064","APPROVED")
        )
    }

    @Test
    internal fun `not should register a new proposal when data is invalid`() {
        //setting
        val request = InsertProposalRequest.newBuilder().build()
        //action
        val response = assertThrows<StatusRuntimeException> {
            grpcClient.insert(request)
        }
        //validation
        with(response) {
            assertEquals(Status.INVALID_ARGUMENT.code, status.code)
            assertEquals("invalid parameters", status.description)
            assertEquals(0, repository.count())
        }
    }

    @Test
    internal fun `not should register a new proposal when there is already a proposal with the document`() {
        //setting
        val request = insertProposalRequest("95451900000");
        repository.save(
            Proposal(
                request.document,
                request.email,
                request.name,
                request.address,
                BigDecimal(request.salary)
            )
        )
        //action
        val response = assertThrows<StatusRuntimeException> {
            grpcClient.insert(request)
        }
        //validation
        with(response) {
            assertEquals(Status.ALREADY_EXISTS.code, status.code)
            assertEquals("This document already has a proposal", status.description)
            assertTrue(repository.existsByDocument(request.document))
            assertEquals(1, repository.count())
        }
    }

    private fun insertProposalRequest(document: String) = InsertProposalRequest.newBuilder()
        .setAddress("new address")
        .setDocument(document)
        .setName("Bob Brown")
        .setSalary(2500.00)
        .setEmail("bob@email.com")
        .build()

    @Factory
    class Clients {
        @Singleton
        fun blockingStub(@GrpcChannel(GrpcServerChannel.NAME) channel: ManagedChannel)
                : InsertProposalServiceGrpc.InsertProposalServiceBlockingStub? {
            return InsertProposalServiceGrpc.newBlockingStub(channel)
        }
    }

    @Factory
    @Replaces(factory = CreditAnalysisFactory::class)
    internal class MockitoStubFactory {
        @Singleton
        fun stubMock() =
            mock(CreditAnalysisServiceGrpc.CreditAnalysisServiceBlockingStub::class.java)
    }

}