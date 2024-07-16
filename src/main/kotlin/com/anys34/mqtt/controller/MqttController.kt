package com.anys34.mqtt.controller

import com.anys34.mqtt.service.MqttService
import org.springframework.web.bind.annotation.GetMapping
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
}