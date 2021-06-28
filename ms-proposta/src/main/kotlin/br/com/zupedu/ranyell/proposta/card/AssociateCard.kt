package br.com.zupedu.ranyell.proposta.card

import br.com.zupedu.ranyell.proposta.proposal.ProposalRepository
import br.com.zupedu.ranyell.proposta.shared.exception.ResourceNotFoundException
import io.micronaut.configuration.kafka.annotation.KafkaKey
import io.micronaut.configuration.kafka.annotation.KafkaListener
import io.micronaut.configuration.kafka.annotation.OffsetReset
import io.micronaut.configuration.kafka.annotation.Topic
import io.micronaut.validation.Validated
import org.slf4j.LoggerFactory
import javax.inject.Inject
import javax.validation.Valid

@Validated
@KafkaListener(offsetReset = OffsetReset.EARLIEST)
class AssociateCard(
    @Inject private val repository: CardRepository,
    @Inject private val proposalRepository: ProposalRepository
) {

    private val LOGGER = LoggerFactory.getLogger(this::class.java)

    @Topic("card-create")
    fun receive(@KafkaKey id: Long, @Valid response: CardConsumerResponse) {
        proposalRepository.findById(response.proposalId)
            .orElseThrow { throw ResourceNotFoundException("Proposal id = ${response.proposalId} not found") }
            .let { repository.save(response.toCard(it)) }
            .also { LOGGER.info("card associated with the proposal id = {}", id) }
    }
}

