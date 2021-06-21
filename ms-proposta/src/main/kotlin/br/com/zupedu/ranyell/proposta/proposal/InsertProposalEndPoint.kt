package br.com.zupedu.ranyell.proposta.proposal

import br.com.zupedu.ranyell.proposta.InsertProposalRequest
import br.com.zupedu.ranyell.proposta.InsertProposalResponse
import br.com.zupedu.ranyell.proposta.InsertProposalServiceGrpc
import br.com.zupedu.ranyell.proposta.shared.exception.HandlerError
import io.grpc.stub.StreamObserver
import org.slf4j.LoggerFactory
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
@HandlerError
class InsertProposalEndPoint(
    @Inject private val insertProposalService: InsertProposalService
) : InsertProposalServiceGrpc.InsertProposalServiceImplBase() {

    private val LOGGER = LoggerFactory.getLogger(this::class.java)

    override fun insert(request: InsertProposalRequest?, responseObserver: StreamObserver<InsertProposalResponse>) {
        val proposal = insertProposalService.insert(request!!.toProposalRequest())
        val response = InsertProposalResponse.newBuilder().setPropostaId(proposal.id!!).build()
        LOGGER.info("${proposal.name} customer proposal successfully registered")
        responseObserver.onNext(response)
        responseObserver.onCompleted()
    }
}