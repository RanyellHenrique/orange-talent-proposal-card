package br.com.zupedu.ranyell.proposta.card

import io.micronaut.configuration.kafka.annotation.KafkaClient
import io.micronaut.configuration.kafka.annotation.KafkaKey
import io.micronaut.configuration.kafka.annotation.Topic

@KafkaClient
interface CardProducer {

    @Topic("card-create")
    fun send(@KafkaKey id: Long, card: CardConsumerResponse)
}