package br.com.zupedu.ranyell.proposta.card

import br.com.zupedu.ranyell.proposta.proposal.Proposal
import br.com.zupedu.ranyell.proposta.proposal.ProposalRepository
import io.micronaut.configuration.kafka.annotation.KafkaKey
import io.micronaut.configuration.kafka.annotation.KafkaListener
import io.micronaut.configuration.kafka.annotation.Topic
import org.slf4j.LoggerFactory
import java.time.LocalDateTime
import javax.inject.Inject
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

@KafkaListener
class AssociateCard(
    @Inject private val repository: CardRepository,
    @Inject private val proposalRepository: ProposalRepository
) {

    private val LOGGER = LoggerFactory.getLogger(this::class.java)

    @Topic("card-create")
    fun receive(@KafkaKey id: Long, response: CardConsumerResponse) {
        proposalRepository.findById(response.proposalId)
            .ifPresent { repository.save(response.toCard(it)) }
            .also { LOGGER.info("card associated with the proposal id = {}", id) }
    }
}

data class CardConsumerResponse(
    @field:NotBlank
    val name: String,
    @field:NotBlank
    val proposalId: Long,
    @field:NotBlank
    val number: String,
    @field:NotNull
    val createAt: LocalDateTime,
    @field:NotNull
    var statusCard: StatusCard
) {

    fun toCard(proposal: Proposal): Card {
        return Card(number, name, createAt, statusCard, proposal)
    }
}