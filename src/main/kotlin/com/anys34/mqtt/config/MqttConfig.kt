package com.anys34.mqtt.config

import com.anys34.mqtt.service.MqttListener
import org.eclipse.paho.client.mqttv3.MqttConnectOptions
import org.springframework.beans.factory.annotation.Autowired
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

@Configuration
class MqttConfig {
    @Autowired
    private lateinit var mqttListener: MqttListener
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
        return mqttListener
    }


    companion object {
        const val PUB_CLIENT_ID = "mqtt-anys-pub"
        const val SUB_CLIENT_ID = "mqtt-anys-sub"
    }
}