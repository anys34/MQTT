package com.anys34.mqtt.controller

import com.anys34.mqtt.config.MqttConfig.Companion.PUB_CLIENT_ID
import com.anys34.mqtt.controller.dto.req.SendRequest
import com.anys34.mqtt.service.MqttService
import org.springframework.web.bind.annotation.*

@RequestMapping("/mqtt")
@RestController
class MqttController(
        private val mqttGateway: MqttService.MqttGateway,
        private val mqttService: MqttService
) {
    @GetMapping("/ping")
    fun ping() {
        mqttGateway.sendToMqtt("Hello World!")
    }

    @PostMapping("/publish")
    fun send(
            @RequestBody
            sendRequest: SendRequest
    ) {
        mqttGateway.sendToMqtt("["+ PUB_CLIENT_ID+"]: " + sendRequest.message)
    }

    @GetMapping("/messages")
    fun listMessage(
            @RequestParam clientId: String
    ) = mqttService.listMessages(clientId)
}