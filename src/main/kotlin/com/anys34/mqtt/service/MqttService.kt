package com.anys34.mqtt.service

import com.anys34.mqtt.controller.dto.res.ListMessageResponse
import com.anys34.mqtt.domain.Message
import com.anys34.mqtt.domain.repository.MessageRepository
import org.springframework.integration.annotation.MessagingGateway
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class MqttService(
        private val messageRepository: MessageRepository
) {
    @MessagingGateway(defaultRequestChannel = "mqttOutboundChannel")
    interface MqttGateway {
        fun sendToMqtt(@Payload data: String)
    }

    @Transactional
    fun save(message: Message) {
        messageRepository.save(message)
    }

    @Transactional(readOnly = true)
    fun listMessages(clientId: String)
        = messageRepository.findAllByClientId(clientId)
            .map { ListMessageResponse(it) }
            .toList()
}