package id.slava.nt.chatgpthelper.presentation.mainscreen

import android.Manifest
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateListOf
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
import id.slava.nt.chatgpthelper.common.AIModels
import id.slava.nt.chatgpthelper.common.GPTModels
import id.slava.nt.chatgpthelper.common.GeminiModels
import id.slava.nt.chatgpthelper.common.LANGUAGES
import id.slava.nt.chatgpthelper.common.aiModels
import id.slava.nt.chatgpthelper.common.geminiModels
import id.slava.nt.chatgpthelper.common.gptModels
import id.slava.nt.chatgpthelper.common.languages
import id.slava.nt.chatgpthelper.data.utils.PermissionsHandler
import id.slava.nt.chatgpthelper.data.utils.SpeechRecognitionHelper
import id.slava.nt.chatgpthelper.domain.model.ChatMessage
import id.slava.nt.chatgpthelper.domain.model.UserRequest
import id.slava.nt.chatgpthelper.presentation.buttons.Checkbox
import id.slava.nt.chatgpthelper.presentation.buttons.MicrophoneButton
import id.slava.nt.chatgpthelper.presentation.dialogs.PermissionDialog
import id.slava.nt.chatgpthelper.presentation.messages.ChatMessageList
import kotlinx.coroutines.delay
import org.koin.compose.koinInject

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SpeechRecognitionScreen(modifier: Modifier = Modifier) {

    var isAiModelMenuExpanded by remember { mutableStateOf(false) }
    var isLanguageMenuExpanded by remember { mutableStateOf(false) }
    var isGptModelMenuExpanded by remember { mutableStateOf(false) }
    var isGeminiModelMenuExpanded by remember { mutableStateOf(false) }

    var selectedAiModel by remember { mutableStateOf(AIModels.GEMINI) }
    var selectedLanguage by remember { mutableStateOf(LANGUAGES.ENGLISH) }
    var selectedGptModel by remember { mutableStateOf(GPTModels.GPT_4_TURBO) }
    var selectedGeminiModel by remember { mutableStateOf(GeminiModels.GEMINI_1_5_FLASH) }
    var useSystemMessageIsChecked by remember { mutableStateOf(true) }

    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    val viewModelMain: MainScreenViewModel = koinInject()
    val responseState by viewModelMain.responseState.collectAsState()

    // Use `remember` to create instances once per composition
    val speechRecognitionHelper =
        remember { SpeechRecognitionHelper(context, selectedLanguage.languageCode) }
    val permissionHandler = remember { PermissionsHandler(context) }
    var isListening by remember { mutableStateOf(false) } // New state to track if listening
    var startTime by remember { mutableLongStateOf(0) }

    // State variables
    var userInputText by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }
    var permissionsGranted by remember { mutableStateOf(false) }
    var messages by remember { mutableStateOf(listOf<ChatMessage>()) }
    // List to store the entire conversation history
    val conversationHistory = remember { mutableStateListOf<UserRequest>() }

    // Permission dialog screen
    PermissionDialog(
        showDialog = showDialog,
        onDismiss = { showDialog = false },
        permissionName = "Audio Record"
    )

    fun sendMessage(messageContent: String) {
        val userMessage = ChatMessage.UserMessage(content = messageContent)
        messages = messages + userMessage // Update the messages list with the user's message

        // Add the user's message to the conversation history
        conversationHistory.add(UserRequest(role = "user", content = messageContent))

        // Call the ViewModel to get the response from ChatGPT or Gemini
        if (selectedAiModel == AIModels.GEMINI) viewModelMain.getGeminiResponse(conversationHistory) else viewModelMain.getChatGPTResponse(
            conversationHistory
        )

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

                val errorMessage =
                    ChatMessage.ErrorMessage(content = "Error: ${responseState.error}")
                messages = messages + errorMessage
            }

            responseState.response != null -> {
                // Remove LoadingMessage
                messages = messages.filterNot { it is ChatMessage.LoadingMessage }

                val botMessage = ChatMessage.BotMessage(content = responseState.response!!)
                messages = messages + botMessage

                conversationHistory.add(
                    UserRequest(
                        role = "assistant",
                        content = botMessage.content
                    )
                )
            }
        }
    }

    val listState = rememberLazyListState()
    LaunchedEffect(messages.size) {
        listState.animateScrollToItem(messages.size)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(
                                "${selectedAiModel.name}  ⬇\uFE0F",
                                fontSize = 18.sp,
                                modifier = Modifier.clickable {
                                    isAiModelMenuExpanded = true
                                })
                            DropdownMenu(
                                expanded = isAiModelMenuExpanded,
                                onDismissRequest = { isAiModelMenuExpanded = false }
                            ) {
                                aiModels.forEach { model ->
                                    DropdownMenuItem(
                                        text = { Text(text = model.name) },
                                        onClick = {
                                            selectedAiModel = model
                                            isAiModelMenuExpanded = false

                                        }
                                    )
                                }
                            }
                        }

                        IconButton(onClick = {
                            messages = listOf()
                            conversationHistory.clear()
                            userInputText = ""
                        }) {
                            Icon(Icons.Default.Delete, contentDescription = "Clear Chat")
                        }
                    }

                }
            )
        },
//        floatingActionButtonPosition = FabPosition.Center,
        floatingActionButton = {
            FloatingActionButton(
                onClick = { /* Handle FAB click */ }) {
                MicrophoneButton(speechRecognitionHelper)
            }

        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(bottom = 20.dp),

                ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Column {
                        Text(
                            text = "${selectedLanguage.languageCode.takeLast(2)}  ⬇\uFE0F",
                            fontSize = 18.sp,
                            modifier = Modifier.clickable {
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
                                        speechRecognitionHelper.updateLanguage(selectedLanguage.languageCode)
                                    }
                                )
                            }
                        }
                    }

                    if (selectedAiModel == AIModels.GEMINI) {
                        Column {
                            Text(
                                text = "${selectedGeminiModel.modelName}  ⬇\uFE0F",
                                fontSize = 18.sp,
                                modifier = Modifier.clickable {
                                    isGeminiModelMenuExpanded = true
                                })
                            DropdownMenu(
                                expanded = isGeminiModelMenuExpanded,
                                onDismissRequest = { isGeminiModelMenuExpanded = false }
                            ) {
                                geminiModels.forEach { model ->
                                    DropdownMenuItem(
                                        text = { Text(text = model.modelName) },
                                        onClick = {
                                            selectedGeminiModel = model
                                            isGeminiModelMenuExpanded = false
                                            Toast.makeText(
                                                context,
                                                model.modelDescription,
                                                Toast.LENGTH_LONG
                                            ).show()
                                            viewModelMain.updateGeminiModel(model)
                                        }
                                    )
                                }
                            }
                        }
                    } else {
                        Column {
                            Text(
                                text = "${selectedGptModel.modelName}  ⬇\uFE0F",
                                fontSize = 18.sp,
                                modifier = Modifier.clickable {
                                    isGptModelMenuExpanded = true
                                })
                            DropdownMenu(
                                expanded = isGptModelMenuExpanded,
                                onDismissRequest = { isGptModelMenuExpanded = false }
                            ) {
                                gptModels.forEach { model ->
                                    DropdownMenuItem(
                                        text = { Text(text = model.modelName) },
                                        onClick = {
                                            selectedGptModel = model
                                            isGptModelMenuExpanded = false
                                            Toast.makeText(
                                                context,
                                                model.modelDescription,
                                                Toast.LENGTH_LONG
                                            ).show()
                                            viewModelMain.updateGptModel(model)
                                        }
                                    )
                                }
                            }
                        }
                    }
                }

                ChatMessageList(
                    messages = messages,
                    listState = listState,
                    modifier = Modifier.weight(1f)
                )

                HorizontalDivider()

                if (permissionsGranted) {
                    Column {

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            SelectionContainer(
                                modifier = Modifier.weight(1f)
                            ) {

                            }
                            TextField(
                                value = userInputText,
                                onValueChange = { userInputText = it },
                                modifier = Modifier.weight(1f),
                                placeholder = {
                                    Text(
                                        text = when (selectedLanguage) {
                                            LANGUAGES.ENGLISH -> "Type your message"
                                            LANGUAGES.SPANISH -> "Escribe tu mensaje"
                                            LANGUAGES.RUSSIAN -> "Введите ваше сообщение"
                                            LANGUAGES.UKRAINIAN -> "Введіть своє повідомлення"
                                            else -> "Type your message"
                                        }
                                    )
                                },
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
                        }

                        Checkbox(isChecked = useSystemMessageIsChecked, onCheckedChange = {
                            useSystemMessageIsChecked = it
                            viewModelMain.updateUseSystemMessage(it)
                        })
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
    )
}

