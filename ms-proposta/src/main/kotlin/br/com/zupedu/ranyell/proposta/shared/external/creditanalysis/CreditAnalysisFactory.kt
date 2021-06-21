package br.com.zupedu.ranyell.proposta.shared.external.creditanalysis

import br.com.zupedu.ranyell.proposta.CreditAnalysisServiceGrpc
import io.grpc.ManagedChannel
import io.micronaut.context.annotation.Factory
import io.micronaut.grpc.annotation.GrpcChannel
import javax.inject.Singleton

@Factory
class CreditAnalysisFactory(@GrpcChannel("credit-analysis") val channel: ManagedChannel) {

    @Singleton
    fun analyze() = CreditAnalysisServiceGrpc.newBlockingStub(channel)
}