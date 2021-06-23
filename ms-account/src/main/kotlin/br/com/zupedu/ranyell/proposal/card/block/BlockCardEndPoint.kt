package br.com.zupedu.ranyell.proposal.card.block

import br.com.zupedu.ranyell.proposal.BlockCardRequest
import br.com.zupedu.ranyell.proposal.BlockCardResponse
import br.com.zupedu.ranyell.proposal.BlockCardServiceGrpc
import br.com.zupedu.ranyell.proposal.shared.exception.HandlerError
import io.grpc.stub.StreamObserver
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
@HandlerError
class BlockCardEndPoint(
    @Inject private val blockCardService: BlockCardService
): BlockCardServiceGrpc.BlockCardServiceImplBase() {

    override fun block(request: BlockCardRequest, responseObserver: StreamObserver<BlockCardResponse>?) {
        val serviceResponse = blockCardService.block(request.toBlockRequest())
        val response = BlockCardResponse.newBuilder().setMessage(serviceResponse).build()
        responseObserver!!.onNext(response)
        responseObserver.onCompleted()
    }
}