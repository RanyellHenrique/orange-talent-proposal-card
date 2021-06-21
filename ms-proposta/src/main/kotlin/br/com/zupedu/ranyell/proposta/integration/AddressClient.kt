package br.com.zupedu.ranyell.proposta.integration

import br.com.zupedu.ranyell.proposta.proposal.Address
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.PathVariable
import io.micronaut.http.client.annotation.Client

@Client("\${address.client.url}")
interface AddressClient {

    @Get("/{cep}/json")
    fun findByCep(@PathVariable cep: String): HttpResponse<Any?>

}

data class AddressResponse(
    val cep: String?,
    val logradouro: String?,
    val complemento: String?,
    val localidade: String?,
    val uf: String?
) {

    fun toAddress() = Address(
        cep = this.cep!!,
        publicPlace = this.logradouro!!,
        complement = this.complemento!!,
        location = this.localidade!!,
        uf = this.uf!!
    )
}
