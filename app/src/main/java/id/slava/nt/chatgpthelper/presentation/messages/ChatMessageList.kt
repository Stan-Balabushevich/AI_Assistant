package id.slava.nt.chatgpthelper.presentation.messages

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import id.slava.nt.chatgpthelper.domain.model.ChatMessage

@Composable
fun ChatMessageList(
    messages: List<ChatMessage>, // Pass the list of messages
    listState: LazyListState,
    modifier: Modifier = Modifier // Pass the list state
) {
    LazyColumn(
        modifier = modifier
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
}
