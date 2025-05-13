package com.example.gemmamessenger.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.UUID

@Parcelize
data class Agent(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val description: String,
    val personality: String,
    val systemPrompt: String = generateSystemPrompt(name, personality)
) : Parcelable {
    companion object {
        fun generateSystemPrompt(name: String, personality: String): String {
            return """
                You are $name, an AI assistant with the following personality traits:
                $personality
                
                Always respond in a way that reflects your personality.
                Keep your responses concise and helpful.
                """.trimIndent()
        }
        
        // Default agents
        fun getDefaultAgents(): List<Agent> {
            return listOf(
                Agent(
                    name = "Helpful Assistant",
                    description = "A friendly and helpful assistant that can answer your questions",
                    personality = "Friendly, helpful, knowledgeable, and concise."
                ),
                Agent(
                    name = "Creative Writer",
                    description = "An assistant that specializes in creative writing and storytelling",
                    personality = "Creative, imaginative, eloquent, and inspiring."
                ),
                Agent(
                    name = "Technical Expert",
                    description = "An assistant that specializes in technical topics and programming",
                    personality = "Analytical, precise, technical, and thorough."
                )
            )
        }
    }
}
