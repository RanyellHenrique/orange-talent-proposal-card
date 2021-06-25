package br.com.zupedu.ranyell.proposal.card.findbyproposal

import br.com.zupedu.ranyell.proposal.FindCardByProposalGrpc
import br.com.zupedu.ranyell.proposal.FindCardByProposalRequest
import br.com.zupedu.ranyell.proposal.FindCardByProposalResponse
import br.com.zupedu.ranyell.proposal.shared.exception.HandlerError
import io.grpc.stub.StreamObserver
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
@HandlerError
class FindCardByProposalEndPoint(
    @Inject private val findCardService: FindCardByProposalService
): FindCardByProposalGrpc.FindCardByProposalImplBase() {

    override fun find(
        request: FindCardByProposalRequest?,
        responseObserver: StreamObserver<FindCardByProposalResponse>?
    ) {
        val response = findCardService.find(request!!.toFindByProposalRequest())
        responseObserver!!.onNext(response)
        responseObserver.onCompleted()
    }
}

fun FindCardByProposalRequest.toFindByProposalRequest() = FindByProposalRequest(proposalId)
