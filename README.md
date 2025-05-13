# Gemma Messenger

A multi-agent messenger app for Android that uses the Gemma 3 1B int4 model through Google's AI API. This app allows users to chat with multiple AI agents, each with its own personality and conversation history.

## Features

### Multiple AI Agents
- Support for multiple AI agents with different personalities
- Each agent maintains its own conversation history and context
- Comes with three default agents: Helpful Assistant, Creative Writer, and Technical Expert
- Create custom agents with unique personalities

### Chat Interface
- Modern messenger-style interface
- Different bubble styles for user and agent messages
- Message timestamps
- Smooth scrolling and intuitive UI

### Gemma 3 Integration
- Uses Google's AI API to access the Gemma 3 1B int4 model
- On-device conversation management
- Efficient context handling for natural conversations

## Technical Details

### Architecture
- Written in Kotlin
- Uses MVVM architecture pattern
- Implements modern Android development practices

### Libraries Used
- OkHttp for network requests
- Kotlin Coroutines for asynchronous operations
- ViewBinding for type-safe view access
- RecyclerView for efficient list rendering
- Material Design components for UI

## Setup

### Prerequisites
- Android Studio Arctic Fox or newer
- Android SDK 26 (Android 8.0) or higher
- Google AI API key

### Building the Project
1. Clone this repository
2. Open the project in Android Studio
3. Sync Gradle files
4. Build and run the app on your device or emulator

### API Key Configuration
The app uses the following API key for accessing the Gemma 3 model:
```
API Key: AIzaSyDA3amxFAKM17sKxSS77dB5-Gi1tZpoTjc
Project Number: 723631307795
```

## Usage

### Starting the App
When you first launch the app, it will test the connection to the Google AI API. Once connected, you'll see the agent selection screen.

### Selecting an Agent
The main screen displays a list of available agents. Tap on an agent to start a conversation.

### Chatting with an Agent
- Type your message in the input field at the bottom
- Tap the send button to send your message
- The agent will process your message and respond
- The conversation history is maintained for context

### Creating a New Agent
- Tap the "+" button on the main screen
- Enter a name, description, and personality for the agent
- The personality will influence how the agent responds
- Tap "Save" to create the agent

## License

This project is licensed under the MIT License - see the LICENSE file for details.

## Acknowledgments

- Google for providing the Gemma 3 model and API
- The Android development community for their valuable resources and tools
