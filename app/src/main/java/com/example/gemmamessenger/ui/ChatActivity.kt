package com.example.gemmamessenger.ui

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gemmamessenger.GemmaMessengerApp
import com.example.gemmamessenger.adapter.MessageAdapter
import com.example.gemmamessenger.databinding.ActivityChatBinding
import com.example.gemmamessenger.model.Agent
import com.example.gemmamessenger.model.Message
import kotlinx.coroutines.launch

class ChatActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChatBinding
    private lateinit var messageAdapter: MessageAdapter
    private val messages = mutableListOf<Message>()
    private lateinit var agent: Agent

    companion object {
        const val EXTRA_AGENT = "extra_agent"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        agent = intent.getParcelableExtra(EXTRA_AGENT, Agent::class.java) ?: throw IllegalStateException("Agent is required")

        setupActionBar()
        setupRecyclerView()
        setupClickListeners()
    }

    private fun setupActionBar() {
        supportActionBar?.apply {
            title = agent.name
            setDisplayHomeAsUpEnabled(true)
        }
    }

    private fun setupRecyclerView() {
        messageAdapter = MessageAdapter(messages)

        binding.rvMessages.apply {
            layoutManager = LinearLayoutManager(this@ChatActivity).apply {
                stackFromEnd = true
            }
            adapter = messageAdapter
        }
    }

    private fun setupClickListeners() {
        binding.btnSend.setOnClickListener {
            val messageText = binding.etMessage.text.toString().trim()

            if (messageText.isNotEmpty()) {
                sendMessage(messageText)
                binding.etMessage.text.clear()
            }
        }
    }

    private fun sendMessage(messageText: String) {
        // Add user message to the list
        val userMessage = Message(
            content = messageText,
            isFromUser = true
        )

        addMessage(userMessage)

        // Show loading indicator
        binding.progressBar.visibility = View.VISIBLE

        // Generate response from the model
        lifecycleScope.launch {
            try {
                val response = GemmaMessengerApp.instance.modelManager.generateResponse(
                    userMessage = messageText,
                    agent = agent
                )

                // Add agent response to the list
                val agentMessage = Message(
                    content = response,
                    isFromUser = false,
                    agentId = agent.id,
                    agentName = agent.name
                )

                addMessage(agentMessage)
            } catch (e: Exception) {
                Toast.makeText(
                    this@ChatActivity,
                    "Error: ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            } finally {
                binding.progressBar.visibility = View.GONE
            }
        }
    }

    private fun addMessage(message: Message) {
        messages.add(message)
        messageAdapter.notifyItemInserted(messages.size - 1)
        binding.rvMessages.scrollToPosition(messages.size - 1)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}
