package br.com.zupedu.ranyell.proposal.card.notify

import br.com.zupedu.ranyell.proposal.NotifyCardRequest
import br.com.zupedu.ranyell.proposal.NotifyCardResponse
import br.com.zupedu.ranyell.proposal.NotifyCardServiceGrpc
import br.com.zupedu.ranyell.proposal.shared.exception.HandlerError
import io.grpc.stub.StreamObserver
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
@HandlerError
class NotifyCardEndPoint(
    @Inject private val notifyService: NotifyCardService
): NotifyCardServiceGrpc.NotifyCardServiceImplBase() {

    override fun notify(request: NotifyCardRequest?, responseObserver: StreamObserver<NotifyCardResponse>?) {
        val responseService = notifyService.notify(request!!.toNotifyRequest())
        val response = NotifyCardResponse.newBuilder()
            .setResult(responseService)
            .build()
        responseObserver!!.onNext(response)
        responseObserver.onCompleted()
    }
}