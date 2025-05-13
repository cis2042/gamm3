package com.example.gemmamessenger.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.gemmamessenger.databinding.ItemAgentBinding
import com.example.gemmamessenger.model.Agent

class AgentAdapter(
    private val agents: List<Agent>,
    private val onAgentClick: (Agent) -> Unit
) : RecyclerView.Adapter<AgentAdapter.AgentViewHolder>() {
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AgentViewHolder {
        val binding = ItemAgentBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return AgentViewHolder(binding)
    }
    
    override fun onBindViewHolder(holder: AgentViewHolder, position: Int) {
        holder.bind(agents[position])
    }
    
    override fun getItemCount(): Int = agents.size
    
    inner class AgentViewHolder(
        private val binding: ItemAgentBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        
        init {
            binding.root.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onAgentClick(agents[position])
                }
            }
        }
        
        fun bind(agent: Agent) {
            binding.tvAgentName.text = agent.name
            binding.tvAgentDescription.text = agent.description
        }
    }
}
