package com.anys34.mqtt.domain.repository

import com.anys34.mqtt.domain.Message
import org.springframework.data.jpa.repository.JpaRepository

interface MessageRepository: JpaRepository<Message, Long> {
    fun findAllByClientId(clientId: String): List<Message>
}