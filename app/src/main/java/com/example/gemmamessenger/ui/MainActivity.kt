package com.example.gemmamessenger.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gemmamessenger.GemmaMessengerApp
import com.example.gemmamessenger.R
import com.example.gemmamessenger.adapter.AgentAdapter
import com.example.gemmamessenger.databinding.ActivityMainBinding
import com.example.gemmamessenger.databinding.DialogAddAgentBinding
import com.example.gemmamessenger.model.Agent
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityMainBinding
    private lateinit var agentAdapter: AgentAdapter
    private val agents = mutableListOf<Agent>()
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        setupRecyclerView()
        setupClickListeners()
        loadDefaultAgents()
        initializeModel()
    }
    
    private fun setupRecyclerView() {
        agentAdapter = AgentAdapter(agents) { agent ->
            openChatActivity(agent)
        }
        
        binding.rvAgents.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = agentAdapter
        }
    }
    
    private fun setupClickListeners() {
        binding.fabAddAgent.setOnClickListener {
            showAddAgentDialog()
        }
    }
    
    private fun loadDefaultAgents() {
        agents.clear()
        agents.addAll(Agent.getDefaultAgents())
        agentAdapter.notifyDataSetChanged()
    }
    
    private fun initializeModel() {
        binding.progressBar.visibility = View.VISIBLE
        
        lifecycleScope.launch {
            val success = GemmaMessengerApp.instance.modelManager.initializeModel()
            
            binding.progressBar.visibility = View.GONE
            
            if (success) {
                Toast.makeText(
                    this@MainActivity,
                    R.string.model_loaded,
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                Toast.makeText(
                    this@MainActivity,
                    R.string.error_loading_model,
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }
    
    private fun showAddAgentDialog() {
        val dialogBinding = DialogAddAgentBinding.inflate(layoutInflater)
        
        AlertDialog.Builder(this)
            .setTitle(R.string.add_agent)
            .setView(dialogBinding.root)
            .setPositiveButton(R.string.save) { _, _ ->
                val name = dialogBinding.etAgentName.text.toString()
                val description = dialogBinding.etAgentDescription.text.toString()
                val personality = dialogBinding.etAgentPersonality.text.toString()
                
                if (name.isNotBlank() && description.isNotBlank() && personality.isNotBlank()) {
                    val newAgent = Agent(
                        name = name,
                        description = description,
                        personality = personality
                    )
                    
                    agents.add(newAgent)
                    agentAdapter.notifyItemInserted(agents.size - 1)
                }
            }
            .setNegativeButton(R.string.cancel, null)
            .show()
    }
    
    private fun openChatActivity(agent: Agent) {
        val intent = Intent(this, ChatActivity::class.java).apply {
            putExtra(ChatActivity.EXTRA_AGENT, agent)
        }
        startActivity(intent)
    }
    
    override fun onDestroy() {
        super.onDestroy()
        if (isFinishing) {
            GemmaMessengerApp.instance.modelManager.close()
        }
    }
}
