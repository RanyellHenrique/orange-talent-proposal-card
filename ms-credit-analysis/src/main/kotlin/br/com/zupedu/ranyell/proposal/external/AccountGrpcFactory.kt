package br.com.zupedu.ranyell.proposal.external

import br.com.zupedu.ranyell.proposal.CreateCardServiceGrpc
import io.grpc.ManagedChannel
import io.micronaut.context.annotation.Factory
import io.micronaut.grpc.annotation.GrpcChannel
import javax.inject.Singleton

@Factory
class AccountGrpcFactory(
    @GrpcChannel("account") val channel: ManagedChannel
) {
    @Singleton
    fun createCard() = CreateCardServiceGrpc.newBlockingStub(channel)
}