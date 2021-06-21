package br.com.zupedu.ranyell.proposta.shared.exception

import io.micronaut.aop.Around
import kotlin.annotation.AnnotationRetention.*
import kotlin.annotation.AnnotationTarget.*

@MustBeDocumented
@Retention(RUNTIME)
@Target(CLASS, FUNCTION)
@Around
annotation class HandlerError
