package br.com.zupedu.ranyell.proposal.analyze

import br.com.zupedu.ranyell.proposal.CreditAnalysisRequest
import br.com.zupedu.ranyell.proposal.CreditAnalysisResponse
import br.com.zupedu.ranyell.proposal.CreditAnalysisServiceGrpc
import br.com.zupedu.ranyell.proposal.shared.exception.HandlerError
import io.grpc.stub.StreamObserver
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
@HandlerError
class CreditAnalysisEndPoint(
    @Inject private val creditService: CreditAnalysisService
) : CreditAnalysisServiceGrpc.CreditAnalysisServiceImplBase() {

    override fun analyze(request: CreditAnalysisRequest?, responseObserver: StreamObserver<CreditAnalysisResponse>?) {
        val response = creditService.analyze(request!!.toAnalysisRequest())
        responseObserver!!.onNext(response)
        responseObserver.onCompleted()
    }
}