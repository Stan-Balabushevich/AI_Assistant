package id.slava.nt.chatgpthelper.data.utils

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.util.Log

class SpeechRecognitionHelper(private val context: Context, private var language: String) {

    private var speechRecognizer: SpeechRecognizer? = null
    private var recognizerIntent: Intent? = null
    private var recognitionListener: RecognitionListener? = null

    init {
        initializeRecognizer()
    }

    private fun initializeRecognizer() {
        // Clean up any existing recognizer
        speechRecognizer?.destroy()
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(context)

        // Initialize recognizer intent with the current language
        recognizerIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
            )
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, language)
        }

        // Re-attach the recognition listener
        recognitionListener?.let {
            speechRecognizer?.setRecognitionListener(it)
        }
    }

    fun updateLanguage(newLanguage: String) {
        language = newLanguage
        initializeRecognizer() // Re-initialize with the new language
    }

    private fun setupRecognitionListener(
        onResults: (String) -> Unit,
        onReadyForSpeech: () -> Unit = {},
        onError: (Int) -> Unit = {}
    ) {
        recognitionListener = object : RecognitionListener {
            override fun onReadyForSpeech(params: Bundle) {
                onReadyForSpeech()
            }

            override fun onBeginningOfSpeech() {}

            override fun onRmsChanged(rmsdB: Float) {}

            override fun onBufferReceived(buffer: ByteArray) {}

            override fun onEndOfSpeech() {}

            override fun onError(error: Int) {
                onError(error)
            }

            override fun onResults(results: Bundle) {
                val matches: ArrayList<String>? =
                    results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                if (!matches.isNullOrEmpty()) {
                    val text = matches[0] // The best match
                    onResults(text)
                } else {
                    Log.e("SpeechRecognitionHelper", "No results found")
                }
            }

            override fun onPartialResults(partialResults: Bundle) {
                val matches: ArrayList<String>? =
                    partialResults.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                if (!matches.isNullOrEmpty()) {
                    val text = matches[0] // The best match
                    onResults(text)
                }
            }

            override fun onEvent(eventType: Int, params: Bundle) {}
        }
        speechRecognizer?.setRecognitionListener(recognitionListener)
    }

    fun startListening() {
        if (SpeechRecognizer.isRecognitionAvailable(context)) {
            speechRecognizer?.startListening(recognizerIntent)
        }
    }

    fun stopListening() {
        speechRecognizer?.stopListening()
    }

    fun destroy() {
        speechRecognizer?.destroy()
        speechRecognizer = null
    }

    // Helper function to set up the speech recognizer
    fun startRecognition(
        onTextUpdate: (String) -> Unit,
        onListeningStateChange: (Boolean) -> Unit
    ) {
        setupRecognitionListener(
            onReadyForSpeech = {
                onListeningStateChange(true)  // Start listening
            },
            onResults = { resultText ->
                onListeningStateChange(false)  // Stop listening
                onTextUpdate(resultText)  // Update the text with recognized speech
            },
            onError = { error ->
                onListeningStateChange(false)  // Stop listening on error
                val errorMessage = when (error) {
                    SpeechRecognizer.ERROR_AUDIO -> "Audio recording error"
                    SpeechRecognizer.ERROR_CLIENT -> "Client side error"
                    SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS -> "Insufficient permissions"
                    SpeechRecognizer.ERROR_NETWORK -> "Network error"
                    SpeechRecognizer.ERROR_NETWORK_TIMEOUT -> "Network timeout"
                    SpeechRecognizer.ERROR_NO_MATCH -> "No match"
                    SpeechRecognizer.ERROR_RECOGNIZER_BUSY -> "RecognitionService busy"
                    SpeechRecognizer.ERROR_SERVER -> "Server error"
                    SpeechRecognizer.ERROR_SPEECH_TIMEOUT -> "No speech input"
                    else -> "Unknown error"
                }
                onTextUpdate(errorMessage)
                Log.e("SpeechRecognizer", "Error: $errorMessage")
            }
        )
    }
}

