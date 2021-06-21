package br.com.zupedu.ranyell.proposta.proposal

import br.com.zupedu.ranyell.proposta.InsertProposalRequest
import br.com.zupedu.ranyell.proposta.InsertProposalServiceGrpc
import io.grpc.ManagedChannel
import io.grpc.Status
import io.grpc.StatusRuntimeException
import io.micronaut.context.annotation.Factory
import io.micronaut.grpc.annotation.GrpcChannel
import io.micronaut.grpc.server.GrpcServerChannel
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.math.BigDecimal
import javax.inject.Singleton

@MicronautTest(transactional = false)
internal class InsertProposalEndPointTest(
    private val repository: ProposalRepository,
    private val grpcClient: InsertProposalServiceGrpc.InsertProposalServiceBlockingStub
) {

    /*
    *  - Happy path -> ok
    *  - Invalid data -> ok
    *  - Duplicate proposal
    */

    @AfterEach
    internal fun tearDown() {
        repository.deleteAll()
    }

    @Test
    internal fun `should register a new proposal`() {
        //setting
        //action
        val response = grpcClient.insert(insertProposalRequest())
        //validation
        with(response) {
            assertNotNull(propostaId)
            assertEquals(1L, propostaId)
            assertEquals(1, repository.count())
        }
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
        val request = insertProposalRequest();
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

    fun insertProposalRequest() = InsertProposalRequest.newBuilder()
        .setAddress("new address")
        .setDocument("95451900000")
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

}