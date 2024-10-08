package id.slava.nt.chatgpthelper.presentation.buttons

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import id.slava.nt.chatgpthelper.presentation.utils.SpeechRecognitionHelper

@Composable
fun MicrophoneButton(
    speechRecognitionHelper: SpeechRecognitionHelper
) {
    Box(
        modifier = Modifier
            .size(64.dp)
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = {
                        // On press, start listening
                        speechRecognitionHelper.startListening()
                        tryAwaitRelease()  // Wait for the press to be released
                        // On release, stop listening
                        speechRecognitionHelper.stopListening()
                    }
                )
            },
        contentAlignment = Alignment.Center,
        // Customize the appearance of the microphone button
    ) {
        // You can add an Icon or Image for the Microphone here
        Text(text = "🎤",fontSize = 32.sp)  // Example icon for demonstration
    }
}

