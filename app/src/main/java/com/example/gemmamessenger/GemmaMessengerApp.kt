package com.example.gemmamessenger

import android.app.Application
import com.example.gemmamessenger.model.ModelManager

class GemmaMessengerApp : Application() {
    
    // Singleton instance of ModelManager
    val modelManager: ModelManager by lazy {
        ModelManager(applicationContext)
    }
    
    override fun onCreate() {
        super.onCreate()
        instance = this
    }
    
    companion object {
        lateinit var instance: GemmaMessengerApp
            private set
    }
}
