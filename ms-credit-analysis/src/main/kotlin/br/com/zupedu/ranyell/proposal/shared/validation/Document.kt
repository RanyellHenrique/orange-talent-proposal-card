package br.com.zupedu.ranyell.proposal.shared.validation

import org.hibernate.validator.constraints.CompositionType.OR
import org.hibernate.validator.constraints.ConstraintComposition
import org.hibernate.validator.constraints.br.CNPJ
import org.hibernate.validator.constraints.br.CPF
import javax.validation.Constraint
import javax.validation.Payload
import javax.validation.ReportAsSingleViolation
import kotlin.annotation.AnnotationRetention.RUNTIME
import kotlin.annotation.AnnotationTarget.*
import kotlin.reflect.KClass

@MustBeDocumented
@Retention(RUNTIME)
@Constraint(validatedBy = [])
@ReportAsSingleViolation
@CPF
@ConstraintComposition(OR)
@CNPJ
@Target(FIELD, CONSTRUCTOR, PROPERTY, VALUE_PARAMETER)
annotation class Document(
    val message: String = "Não é um formato válido de documento",
    val groups: Array<KClass<Any>> = [],
    val payload: Array<KClass<Payload>> = []
)
