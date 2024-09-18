package id.slava.nt.chatgpthelper.presentation.mainscreen

import android.Manifest
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import id.slava.nt.chatgpthelper.data.utils.PermissionsHandler
import id.slava.nt.chatgpthelper.data.utils.SpeechRecognitionHelper
import id.slava.nt.chatgpthelper.domain.model.ChatMessage
import id.slava.nt.chatgpthelper.domain.model.Language
import id.slava.nt.chatgpthelper.presentation.buttons.MicrophoneButton
import id.slava.nt.chatgpthelper.presentation.dialogs.PermissionDialog
import id.slava.nt.chatgpthelper.presentation.messages.BotMessageBubble
import id.slava.nt.chatgpthelper.presentation.messages.ErrorMessageBubble
import id.slava.nt.chatgpthelper.presentation.messages.LoadingMessageBubble
import id.slava.nt.chatgpthelper.presentation.messages.UserMessageBubble
import kotlinx.coroutines.delay
import org.koin.compose.koinInject

@Composable
fun SpeechRecognitionScreen(modifier: Modifier = Modifier) {

    val languages = listOf(
        Language("en-EN", "English"),
        Language("es-ES", "Spanish"),
        Language("ru-RU", "Russian"),
        Language("uk-UA", "Ukrainian"),
        Language("fr-FR", "French"),
        Language("nrf-NO", "Norwegian"),
        Language("de-DE", "German"),
        Language("it-IT", "Italian"),
        Language("pt-PT", "Portuguese")
    )

    var isLanguageMenuExpanded by remember { mutableStateOf(false) }
    var selectedLanguage by remember { mutableStateOf(Language("en-EN", "English")) } // Default language// Set your desired language here, e.g., "en-EN"
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    val viewModelMain: MainScreenViewModel = koinInject()

    val responseState by viewModelMain.responseState.collectAsState()


    // Use `remember` to create instances once per composition
    val speechRecognitionHelper =  remember { SpeechRecognitionHelper(context, selectedLanguage.code) }
//    val speechRecognitionHelper by remember { mutableStateOf(SpeechRecognitionHelper(context, selectedLanguage)) }
    val permissionHandler = remember { PermissionsHandler(context) }
    var isListening by remember { mutableStateOf(false) } // New state to track if listening
    var startTime by remember { mutableLongStateOf(0) }

    // State variables
    var userInputText by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }
    var permissionsGranted by remember { mutableStateOf(false) }
    var messages by remember { mutableStateOf(listOf<ChatMessage>()) }

    // Permission dialog screen
    PermissionDialog(
        showDialog = showDialog,
        onDismiss = { showDialog = false },
        permissionName = "Audio Record"
    )

    fun sendMessage(messageContent: String) {
        val userMessage = ChatMessage.UserMessage(content = messageContent)
        messages = messages + userMessage // Update the messages list with the user's message

        // Call the ViewModel to get the response from ChatGPT
        viewModelMain.getChatGPTResponse(messageContent)
    }

    DisposableEffect(Unit) {
        onDispose {
            speechRecognitionHelper.destroy()
        }
    }

    // Observer for lifecycle changes to mimic "resume" behavior
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                permissionHandler.setPermissionCallbacks(
                    onGranted = { permission ->
                        if (permission == Manifest.permission.RECORD_AUDIO) {
                            speechRecognitionHelper.startRecognition(
                                onTextUpdate = {
                                    sendMessage(it)
                                    userInputText = it
                                },
                                onErrorMessage = {
                                    Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                                },
                                onListeningStateChange = { isListening = it })
                            permissionsGranted = true
                        }
                    },
                    onDenied = { permission ->
                        if (permission == Manifest.permission.RECORD_AUDIO) {
                            showDialog = true
                        }
                    }
                )
                permissionHandler.requestPermission(Manifest.permission.RECORD_AUDIO)
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    // Update the text with elapsed time while listening
    LaunchedEffect(isListening) {
        if (isListening) {
            startTime = System.currentTimeMillis() // Record the start time
            while (isListening) {
                val elapsedSeconds = (System.currentTimeMillis() - startTime) / 1000
                userInputText = "Listening... ${elapsedSeconds}s"
                delay(1000L)  // Update the text every second
            }
        }
    }

    // Observe responseState and update messages
    LaunchedEffect(responseState) {
        when {
            responseState.isLoading -> {
                // Add LoadingMessage if not already present
                if (messages.lastOrNull() !is ChatMessage.LoadingMessage) {
                    messages = messages + ChatMessage.LoadingMessage
                }
            }

            responseState.error != null -> {
                // Remove LoadingMessage
                messages = messages.filterNot { it is ChatMessage.LoadingMessage }

                val errorMessage = ChatMessage.ErrorMessage(content = "Error: ${responseState.error}")
                messages = messages + errorMessage
            }

            responseState.response != null -> {
                // Remove LoadingMessage
                messages = messages.filterNot { it is ChatMessage.LoadingMessage }

                val botMessage = ChatMessage.BotMessage(content = responseState.response!!)
                messages = messages + botMessage
            }
        }
    }


    val listState = rememberLazyListState()
    LaunchedEffect(messages.size) {
        listState.animateScrollToItem(messages.size)
    }



    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .padding(top = 40.dp),

        ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Column {

                Text(text = selectedLanguage.name, fontSize = 24.sp, modifier = Modifier.clickable {
                    isLanguageMenuExpanded = true
                })

                    DropdownMenu(
                        expanded = isLanguageMenuExpanded,
                        onDismissRequest = { isLanguageMenuExpanded = false }
                    ) {
                        languages.forEach { language ->
                            DropdownMenuItem(
                                text = { Text(text = language.name) },
                                onClick = {
                                    selectedLanguage = language
                                    isLanguageMenuExpanded = false
                                    speechRecognitionHelper.updateLanguage(selectedLanguage.code)
                                }
                            )
                        }
                    }
            }

            IconButton(onClick = {
                messages = listOf()
                userInputText = ""
            }) {
                Icon(Icons.Default.Delete, contentDescription = "Clear Chat")
            }

        }

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            state = listState
        ) {

            items(messages) { message ->
                when (message) {
                    is ChatMessage.UserMessage -> {
                        UserMessageBubble(message.content)
                    }
                    is ChatMessage.BotMessage -> {
                        BotMessageBubble(message.content)
                    }
                    is ChatMessage.LoadingMessage -> {
                        LoadingMessageBubble()
                    }
                    is ChatMessage.ErrorMessage -> {
                        ErrorMessageBubble(message.content)
                    }
                }
            }
        }

        HorizontalDivider()

        if (permissionsGranted) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextField(
                    value = userInputText,
                    onValueChange = { userInputText = it },
                    modifier = Modifier.weight(1f),
                    placeholder = { Text( text = when(selectedLanguage.code){
                        "en-EN" -> "Type your message"
                        "es-ES" -> "Escribe tu mensaje"
                        "ru-RU" -> "Type your message in russian"
                        else -> "Type your message default"
                    }) },
                    keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Send),
                    keyboardActions = KeyboardActions(
                        onSend = {
                            if (userInputText.isNotBlank()) {
                                sendMessage(userInputText)
                                userInputText = ""
                            }
                        }
                    )
                )
                IconButton(onClick = {
                    if (userInputText.isNotBlank()) {
                        sendMessage(userInputText)
                        userInputText = ""
                    }
                }) {
                    Icon(Icons.AutoMirrored.Filled.Send, contentDescription = "Send")
                }
                MicrophoneButton(speechRecognitionHelper)
            }
        } else {
            Text(text = "Permissions not granted")
            Button(onClick = {
                permissionHandler.requestPermission(Manifest.permission.RECORD_AUDIO)
            }) {
                Text(text = "Request Permissions")
            }
        }
    }
}

