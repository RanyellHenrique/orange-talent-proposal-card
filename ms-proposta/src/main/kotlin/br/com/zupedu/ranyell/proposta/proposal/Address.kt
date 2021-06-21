package br.com.zupedu.ranyell.proposta.proposal

import br.com.zupedu.ranyell.proposta.shared.validation.Cep
import javax.persistence.Embeddable
import javax.validation.constraints.NotBlank

@Embeddable
class Address(
    @field:NotBlank @field:Cep
    val cep: String,
    @field:NotBlank
    val publicPlace: String,
    @field:NotBlank
    val complement: String,
    @field:NotBlank
    val location: String,
    @field:NotBlank
    val uf: String
) {

}
