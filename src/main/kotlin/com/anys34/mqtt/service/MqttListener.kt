package com.anys34.mqtt.service

import org.springframework.messaging.Message
import org.springframework.messaging.MessageHandler
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class MqttListener(
        private val mqttService: MqttService
): MessageHandler {
    val regex = "\\[(.*?)\\]".toRegex()

    override fun handleMessage(message: Message<*>) {
        println("[${LocalDateTime.now()}] $message")

        val payload: String = message.payload as String

        val clientIdMatch = regex.find(payload)
        val clientId = clientIdMatch?.groups?.get(1)?.value ?: "Unknown"
        val res = payload.substringAfter("]:").trim()

        mqttService.save(com.anys34.mqtt.domain.Message(clientId, res))
    }
}