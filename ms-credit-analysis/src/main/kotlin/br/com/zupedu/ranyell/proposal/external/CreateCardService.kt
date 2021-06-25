package br.com.zupedu.ranyell.proposal.external

import br.com.zupedu.ranyell.proposal.CreateCardRequest
import br.com.zupedu.ranyell.proposal.CreateCardResponse
import br.com.zupedu.ranyell.proposal.CreateCardServiceGrpc
import io.grpc.Status
import io.grpc.StatusRuntimeException
import org.slf4j.LoggerFactory
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CreateCardService(
    @Inject private val accountGrpcClient: CreateCardServiceGrpc.CreateCardServiceBlockingStub
) {
    private val LOOGER = LoggerFactory.getLogger(this::class.java)

    fun creationCardOrder(request: CreateCardRequest): CreateCardResponse {
        return try {
            accountGrpcClient.create(request).also {
                LOOGER.info("Request to provision the card da proposal: {}", request.proposalId)
            }
        } catch (exception: StatusRuntimeException) {
            when (exception.status.code) {
                Status.UNAVAILABLE.code -> throw StatusRuntimeException(exception.status.withDescription("communication error, external service unavailable"))
                else -> throw exception
            }
        }
    }
}