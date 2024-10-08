
# AI Assistant Android App

An Android application that integrates two AI language models, **ChatGPT** and **Gemini**, allowing users to interact through text or voice messages. The app supports multiple languages and offers customizable settings to enhance your AI experience.


## Table of Contents

- [Features](#features)
- [Prerequisites](#prerequisites)
- [Installation](#installation)
- [Usage](#usage)
- [Project Structure](#project-structure)
- [Technologies Used](#technologies-used)
- [API Keys Setup](#api-keys-setup)
- [Contributing](#contributing)
- [Acknowledgments](#acknowledgments)

## Features

- **AI Model Selection**: Choose between ChatGPT and Gemini, and select different models within each.
- **Messaging Options**:
  - **Text Messages**: Communicate with the AI through text input.
  - **Voice Messages**: Use Android's voice recognition to send voice messages.
- **Language Support**: Select your preferred language for interaction.
- **System Messages**: Include a system message to set the AI's role (e.g., Android development tutor) or disable it for general queries.
- **Custom Components**:
  - **Permission Handler**: Manages app permissions seamlessly.
  - **Speech Recognizer**: Custom implementation for improved voice recognition.

## Prerequisites

- **Android Studio**: Version Flamingo or newer.
- **Android Device or Emulator**: Running Android 5.0 (Lollipop) or higher.
- **Internet Connection**: Required for API communication.
- **API Keys**: Valid API keys for ChatGPT and Gemini (see [API Keys Setup](#api-keys-setup)).

## Installation

1. **Clone the Repository**

   ```bash
   git clone https://github.com/Stan-Balabushevich/AI_Assistant.git
   ```

2. **Open the Project**

   - Launch **Android Studio**.
   - Open the cloned project directory.

3. **Set Up API Keys**

   - Follow the instructions in [API Keys Setup](#api-keys-setup).

4. **Sync Gradle**

   - Allow Android Studio to sync and build the project.

5. **Run the App**

   - Select an emulator or connected device.
   - Click the **Run** button to install and launch the app.

## Usage

### Select AI Model

- On the home screen, choose between **ChatGPT** and **Gemini**.
- Select the desired model variant if available.

### Send Messages

- **Text**: Type your message in the input field and tap **Send**.
- **Voice**: Tap the microphone icon to start voice recognition.

### Change Language

- Go to **Settings** and select your preferred language.

### System Message

- Enable the system message to set the AI as an Android development tutor.
- Disable it for general conversations.

## Project Structure

The app follows the **Clean Architecture** and **MVVM** (Model-View-ViewModel) pattern:

- **Presentation Layer**
  - UI components built with **Jetpack Compose**.
  - **ViewModels** handle UI logic and state.
- **Domain Layer**
  - Contains use cases and business logic.
- **Data Layer**
  - Manages data sources and network communication via **Retrofit**.

## Technologies Used

- **Jetpack Compose**: Modern toolkit for building native Android UI.
- **MVVM Pattern**: Facilitates a clear separation of concerns.
- **Koin**: Dependency Injection framework for Kotlin.
- **Retrofit**: Simplifies network API communication.
- **Custom Permission Handler**: Streamlines runtime permission requests.
- **Custom Speech Recognizer**: Enhances voice input functionality.

## API Keys Setup

To communicate with ChatGPT and Gemini APIs, you need to obtain API keys and configure them in the project.

### Obtain API Keys

- **ChatGPT API Key**
  - Sign up or log in at [OpenAI's website](https://platform.openai.com/).
  - Navigate to the API section to generate a new API key.

- **Gemini API Key**
  - Sign up or log in at Gemini's developer portal.
  - Obtain your API key from the dashboard.

### Configure API Keys

1. **Create a Properties File**

   - In the root directory of the project, create a file named `keys.properties`.

   ```properties
   CHATGPT_API_KEY=your_chatgpt_api_key
   GEMINI_API_KEY=your_gemini_api_key
   ```

2. **Modify Gradle Build Script**

   - Open `app/build.gradle`.

   - Add the following code to load the properties:

     ```gradle
     def keysPropertiesFile = rootProject.file("keys.properties")
     def keysProperties = new Properties()
     keysProperties.load(new FileInputStream(keysPropertiesFile))

     android {
         ...

         defaultConfig {
             ...
             buildConfigField "String", "CHATGPT_API_KEY", keysProperties['CHATGPT_API_KEY']
             buildConfigField "String", "GEMINI_API_KEY", keysProperties['GEMINI_API_KEY']
         }
     }
     ```

3. **Access API Keys in Code**

   - Use `BuildConfig.CHATGPT_API_KEY` and `BuildConfig.GEMINI_API_KEY` in your code to access the keys.

## Contributing

Contributions are welcome! Please follow these steps:

1. **Fork** the repository.
2. **Create** a new branch for your feature or bugfix.
3. **Commit** your changes with descriptive messages.
4. **Push** to your fork and submit a **pull request**.

## Acknowledgments

- **OpenAI**: For providing the ChatGPT API.
- **Gemini**: For providing the Gemini API.
- **Android Community**: For continuous support and resources.
