package br.com.zupedu.ranyell.proposta

import io.micronaut.runtime.Micronaut.*
fun main(args: Array<String>) {
	build()
	    .args(*args)
		.packages("br.com.zupedu.ranyell.proposta")
		.start()
}

