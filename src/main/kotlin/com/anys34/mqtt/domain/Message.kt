package com.anys34.mqtt.domain

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import java.time.LocalDateTime

@Entity
class Message(
        clientId: String,
        message: String,
) {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private val id: Long = 0L

    var clientId: String = clientId
        protected set

    var message: String = message
        protected set

    val createdAt: LocalDateTime = LocalDateTime.now()
}