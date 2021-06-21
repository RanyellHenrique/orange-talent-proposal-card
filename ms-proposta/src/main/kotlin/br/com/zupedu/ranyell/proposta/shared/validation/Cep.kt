package br.com.zupedu.ranyell.proposta.shared.validation

import javax.validation.Constraint
import javax.validation.Payload
import javax.validation.ReportAsSingleViolation
import javax.validation.constraints.Pattern
import kotlin.annotation.AnnotationRetention.RUNTIME
import kotlin.annotation.AnnotationTarget.*
import kotlin.reflect.KClass

@MustBeDocumented
@Retention(RUNTIME)
@Constraint(validatedBy = [])
@ReportAsSingleViolation
@Pattern(
    regexp = "^\\d{5}-?\\d{3}\$",
    flags = [Pattern.Flag.CASE_INSENSITIVE]
)
@Target(FIELD, CONSTRUCTOR, PROPERTY, VALUE_PARAMETER)
annotation class Cep(
    val message: String = "Não é um formato válido de cep",
    val groups: Array<KClass<Any>> = [],
    val payload: Array<KClass<Payload>> = []
)
