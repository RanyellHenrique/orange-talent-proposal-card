package br.com.zupedu.ranyell.proposal.analyse

import br.com.zupedu.ranyell.proposal.CreditAnalysisRequest
import br.com.zupedu.ranyell.proposal.CreditAnalysisServiceGrpc
import br.com.zupedu.ranyell.proposal.SolicitationResult
import br.com.zupedu.ranyell.proposal.SolicitationResult.*
import io.grpc.ManagedChannel
import io.grpc.Status
import io.grpc.StatusRuntimeException
import io.micronaut.context.annotation.Factory
import io.micronaut.grpc.annotation.GrpcChannel
import io.micronaut.grpc.server.GrpcServerChannel
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import javax.inject.Inject
import javax.inject.Singleton

@MicronautTest
internal class CreditAnalysisEndPointTest(
    @Inject private val grpcClient: CreditAnalysisServiceGrpc.CreditAnalysisServiceBlockingStub
) {

    lateinit var DOCUMENT_APPROVED: String
    lateinit var DOCUMENT_REFUSED: String

    @BeforeEach
    internal fun setUp() {
        DOCUMENT_APPROVED = "04423727013"
        DOCUMENT_REFUSED = "37334880080"
    }

    @Test
    internal fun `should return an analyze approved when the document starts with a digit other than 3`() {
        //setting
        val request = creditAnalysisRequest(DOCUMENT_APPROVED)
        //action
        val response = grpcClient.analyze(request)
        //validation
        with(response) {
            assertEquals(DOCUMENT_APPROVED, document)
            assertEquals(APPROVED, solicitationResult)
        }
    }

    @Test
    internal fun `should return an analyze refused when the document starts a digit equal to  3`() {
        //setting
        val request = creditAnalysisRequest(DOCUMENT_REFUSED)
        //action
        val response = grpcClient.analyze(request)
        //validation
        with(response) {
            assertEquals(DOCUMENT_REFUSED, document)
            assertEquals(REFUSED, solicitationResult)
        }
    }

    @Test
    internal fun `not should return an analyze when fields are not valid`() {
        //setting
        val request = CreditAnalysisRequest.newBuilder().build()
        //action
        val response = assertThrows<StatusRuntimeException> {
            grpcClient.analyze(request)
        }
        //validation
        with(response) {
            assertEquals(Status.INVALID_ARGUMENT.code, status.code)
            assertEquals("invalid parameters", status.description)
        }
    }

    private fun creditAnalysisRequest(document: String) = CreditAnalysisRequest.newBuilder()
        .setDocument(document)
        .setProposalId(1L)
        .setName("Bob Brown")
        .build()

    @Factory
    class Clients {
        @Singleton
        fun blockingStub(@GrpcChannel(GrpcServerChannel.NAME) channel: ManagedChannel)
                : CreditAnalysisServiceGrpc.CreditAnalysisServiceBlockingStub {
            return CreditAnalysisServiceGrpc.newBlockingStub(channel)
        }
    }

}