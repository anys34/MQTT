package com.anys34.mqtt.config

import org.eclipse.paho.client.mqttv3.MqttConnectOptions
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.integration.annotation.ServiceActivator
import org.springframework.integration.channel.DirectChannel
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler
import org.springframework.messaging.MessageChannel
import org.springframework.messaging.MessageHandler

@Configuration
class MqttConfig {
    private val brokerUrl = "tcp://10.150.150.90:1883"
    private val topic = "bssm"

    @Bean
    fun mqttPahoClientFactory()
    = DefaultMqttPahoClientFactory().apply {
        connectionOptions = MqttConnectOptions().apply {
            serverURIs = arrayOf(brokerUrl)
        }
    }

    @Bean
    fun mqttOutboundChannel(): MessageChannel {
        return DirectChannel()
    }

    @Bean
    @ServiceActivator(inputChannel = "mqttOutboundChannel")
    fun mqttOutbound(): MessageHandler {
        val handler = MqttPahoMessageHandler(CLIENT_ID, mqttPahoClientFactory()).apply {
            setAsync(true)
            setDefaultTopic(topic)
        }
        return handler
    }

    companion object {
        const val CLIENT_ID = "mqtt-anys"
    }
}