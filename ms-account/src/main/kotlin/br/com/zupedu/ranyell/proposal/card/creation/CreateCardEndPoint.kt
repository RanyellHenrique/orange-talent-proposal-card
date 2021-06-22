package br.com.zupedu.ranyell.proposal.card.creation

import br.com.zupedu.ranyell.proposal.CreateCardRequest
import br.com.zupedu.ranyell.proposal.CreateCardResponse
import br.com.zupedu.ranyell.proposal.CreateCardServiceGrpc
import br.com.zupedu.ranyell.proposal.shared.exception.HandlerError
import io.grpc.stub.StreamObserver
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
@HandlerError
class CreateCardEndPoint(
    @Inject private val createCardService: CreateCardService
) : CreateCardServiceGrpc.CreateCardServiceImplBase() {

    override fun create(request: CreateCardRequest?, responseObserver: StreamObserver<CreateCardResponse>) {
        val createCardResponse = createCardService.create(request!!.toCreateCard())
        val response = CreateCardResponse.newBuilder().setMessage(createCardResponse).build()
        responseObserver.onNext(response)
        responseObserver.onCompleted()
    }
}