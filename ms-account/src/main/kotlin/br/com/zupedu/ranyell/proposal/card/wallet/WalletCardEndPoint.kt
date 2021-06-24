package br.com.zupedu.ranyell.proposal.card.wallet

import br.com.zupedu.ranyell.proposal.WalletCardRequest
import br.com.zupedu.ranyell.proposal.WalletCardResponse
import br.com.zupedu.ranyell.proposal.WalletCardServiceGrpc
import br.com.zupedu.ranyell.proposal.shared.exception.HandlerError
import io.grpc.stub.StreamObserver
import javax.inject.Inject
import javax.inject.Singleton

@HandlerError
@Singleton
class WalletCardEndPoint(
    @Inject private val walletService: WalletCardService
) : WalletCardServiceGrpc.WalletCardServiceImplBase() {

    override fun associate(request: WalletCardRequest?, responseObserver: StreamObserver<WalletCardResponse>?) {
        val responseService = walletService.associate(request!!.toWalletRequest())
        val response = WalletCardResponse.newBuilder()
            .setId(responseService.id!!)
            .setResult("ASSOCIATE")
            .build()
        responseObserver!!.onNext(response)
        responseObserver.onCompleted()
    }
}