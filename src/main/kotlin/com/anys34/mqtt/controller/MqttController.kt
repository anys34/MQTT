package com.anys34.mqtt.controller

import com.anys34.mqtt.controller.dto.req.SendRequest
import com.anys34.mqtt.service.MqttService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/mqtt")
@RestController
class MqttController(
        private val mqttGateway: MqttService.MqttGateway
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
        mqttGateway.sendToMqtt(sendRequest.message)
    }
}