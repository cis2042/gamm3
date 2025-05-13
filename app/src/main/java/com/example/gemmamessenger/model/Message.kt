package com.example.gemmamessenger.model

import java.util.Date

data class Message(
    val id: String = java.util.UUID.randomUUID().toString(),
    val content: String,
    val isFromUser: Boolean,
    val agentId: String? = null,
    val agentName: String? = null,
    val timestamp: Date = Date()
)
