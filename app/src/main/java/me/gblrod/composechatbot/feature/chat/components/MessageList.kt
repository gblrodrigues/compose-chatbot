package me.gblrod.composechatbot.feature.chat.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import me.gblrod.composechatbot.domain.model.ChatMessage

@Composable
fun MessageList(
    messageList: List<ChatMessage>,
    modifier: Modifier = Modifier,
    isLoading: Boolean,
    onCopyMessage: (String) -> Unit
) {
    val state = rememberLazyListState()

    LaunchedEffect(messageList.size, isLoading) {
        if (messageList.isNotEmpty() || isLoading) {
            state.animateScrollToItem(index = 0)
        }
    }

    LazyColumn(
        state = state,
        modifier = modifier.padding(horizontal = 8.dp),
        reverseLayout = true,
        contentPadding = PaddingValues(vertical = 8.dp)
    ) {
        if (isLoading) {
            item {
                TypingIndicator()
            }
        }

        items(
            items = messageList.asReversed(),
            key = { it.id }
        ) {
            MessageRow(
                chatMessage = it,
                onCopyMessage = { response ->
                    onCopyMessage(response)
                }
            )
        }
    }
}