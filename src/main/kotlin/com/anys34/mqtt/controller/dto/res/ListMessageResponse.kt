package com.anys34.mqtt.controller.dto.res

import com.anys34.mqtt.domain.Message
import java.time.LocalDateTime

class ListMessageResponse(
        message: Message
) {
    var clientId: String = message.clientId
    var message: String = message.message
    var createdAt: LocalDateTime = message.createdAt
}