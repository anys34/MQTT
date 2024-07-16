package com.anys34.mqtt.config

import com.anys34.mqtt.service.MqttService
import org.eclipse.paho.client.mqttv3.MqttConnectOptions
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.integration.annotation.ServiceActivator
import org.springframework.integration.channel.DirectChannel
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter
import org.springframework.messaging.MessageChannel
import org.springframework.messaging.MessageHandler
import java.time.LocalDateTime

@Configuration
class MqttConfig(
        private val mqttService: MqttService
) {
    private val brokerUrl = "tcp://10.150.150.90:1883"
    private val topic = "bssm"
    val regex = "\\[(.*?)\\]".toRegex()

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
        val handler = MqttPahoMessageHandler(PUB_CLIENT_ID, mqttPahoClientFactory()).apply {
            setAsync(true)
            setDefaultTopic(topic)
        }
        return handler
    }

    @Bean
    fun mqttInboundChannel(): MessageChannel {
        return DirectChannel()
    }

    @Bean
    fun inbound(): MqttPahoMessageDrivenChannelAdapter {
        val adapter = MqttPahoMessageDrivenChannelAdapter(
                brokerUrl,
                SUB_CLIENT_ID,
                mqttPahoClientFactory(),
                topic,
        )

        adapter.setCompletionTimeout(5000)
        adapter.setConverter(DefaultPahoMessageConverter())
        adapter.setQos(1)
        adapter.outputChannel = mqttInboundChannel()
        return adapter
    }

    @Bean
    @ServiceActivator(inputChannel = "mqttInboundChannel")
    fun mqttInbound(): MessageHandler {
        return MessageHandler { message ->
            println("[${LocalDateTime.now()}] $message")

            val payload: String = message.payload as String

            val clientIdMatch = regex.find(payload)
            val clientId = clientIdMatch?.groups?.get(1)?.value ?: "Unknown"
            val res = payload.substringAfter("]:").trim()

            mqttService.save(com.anys34.mqtt.domain.Message(clientId, res))
        }
    }


    companion object {
        const val PUB_CLIENT_ID = "mqtt-anys-pub"
        const val SUB_CLIENT_ID = "mqtt-anys-sub"
    }
}