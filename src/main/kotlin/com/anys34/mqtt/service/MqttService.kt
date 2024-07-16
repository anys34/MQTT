package com.anys34.mqtt.service

import org.springframework.integration.annotation.MessagingGateway
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.stereotype.Service

@Service
class MqttService {
    @MessagingGateway(defaultRequestChannel = "mqttOutboundChannel")
    interface MqttGateway {
        fun sendToMqtt(@Payload data: String)
    }
}