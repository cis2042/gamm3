package com.example.gemmamessenger.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.gemmamessenger.databinding.ItemMessageAgentBinding
import com.example.gemmamessenger.databinding.ItemMessageUserBinding
import com.example.gemmamessenger.model.Message
import java.text.SimpleDateFormat
import java.util.Locale

class MessageAdapter(
    private val messages: List<Message>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    
    companion object {
        private const val VIEW_TYPE_USER = 1
        private const val VIEW_TYPE_AGENT = 2
        private val DATE_FORMAT = SimpleDateFormat("h:mm a", Locale.getDefault())
    }
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_USER -> {
                val binding = ItemMessageUserBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                UserMessageViewHolder(binding)
            }
            VIEW_TYPE_AGENT -> {
                val binding = ItemMessageAgentBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                AgentMessageViewHolder(binding)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }
    
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val message = messages[position]
        
        when (holder) {
            is UserMessageViewHolder -> holder.bind(message)
            is AgentMessageViewHolder -> holder.bind(message)
        }
    }
    
    override fun getItemCount(): Int = messages.size
    
    override fun getItemViewType(position: Int): Int {
        return if (messages[position].isFromUser) {
            VIEW_TYPE_USER
        } else {
            VIEW_TYPE_AGENT
        }
    }
    
    inner class UserMessageViewHolder(
        private val binding: ItemMessageUserBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        
        fun bind(message: Message) {
            binding.tvMessage.text = message.content
            binding.tvTimestamp.text = DATE_FORMAT.format(message.timestamp)
        }
    }
    
    inner class AgentMessageViewHolder(
        private val binding: ItemMessageAgentBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        
        fun bind(message: Message) {
            binding.tvAgentName.text = message.agentName
            binding.tvMessage.text = message.content
            binding.tvTimestamp.text = DATE_FORMAT.format(message.timestamp)
        }
    }
}
