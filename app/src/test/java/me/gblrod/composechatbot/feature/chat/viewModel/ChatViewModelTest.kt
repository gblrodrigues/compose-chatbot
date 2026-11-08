package me.gblrod.composechatbot.feature.chat.viewModel

import android.util.Log
import androidx.lifecycle.ViewModelStore
import io.mockk.every
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import me.gblrod.composechatbot.R
import me.gblrod.composechatbot.domain.exception.ChatException
import me.gblrod.composechatbot.domain.model.ChatMessage
import me.gblrod.composechatbot.domain.model.MessageRole
import me.gblrod.composechatbot.domain.model.MessageStatus
import me.gblrod.composechatbot.domain.repository.ChatRepository
import me.gblrod.composechatbot.feature.chat.state.ChatUiEvent
import me.gblrod.composechatbot.repository.FakeChatRepository
import me.gblrod.composechatbot.repository.FakeChatRepository.Operation
import me.gblrod.composechatbot.rules.MainDispatcherRule
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.io.IOException

@OptIn(ExperimentalCoroutinesApi::class)
class ChatViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()
    private val markPendingMessagesAsFailed = Operation.MARK_PENDING_MESSAGES_AS_FAILED
    private val collectMessages = Operation.COLLECT_MESSAGES

    private lateinit var viewModelStore: ViewModelStore
    private var viewModelId = 0

    @Before
    fun setUp() {
        viewModelStore = ViewModelStore()
        viewModelId = 0

        mockkStatic(Log::class)

        every {
            Log.e(any(), any(), any())
        } returns 0
    }

    @After
    fun tearDown() {
        viewModelStore.clear()
        mainDispatcherRule.testDispatcher.scheduler.advanceUntilIdle()

        unmockkStatic(Log::class)
    }

    @Test
    fun shouldMarkPendingMessagesAsFailedOnInitialization() = runTest {
        val fakeChatRepository = FakeChatRepository(initialMessages = initialMessagesWithPending())
        val chatViewModel = createChatViewModel(repository = fakeChatRepository)

        runCurrent()
        assertEquals(
            listOf(markPendingMessagesAsFailed, collectMessages),
            fakeChatRepository.operations
        )
        assertTrue(chatViewModel.isHistoryLoaded)
        assertEquals(
            initialMessagesWithPending().map { it.copy(status = MessageStatus.FAILED) },
            chatViewModel.messageList.toList()
        )
    }

    @Test
    fun shouldLoadMessagesAndMarkHistoryAsLoaded() = runTest {
        val fakeChatRepository = FakeChatRepository(initialMessages = initialMessagesWithSent())
        val chatViewModel = createChatViewModel(repository = fakeChatRepository)

        runCurrent()
        assertTrue(chatViewModel.isHistoryLoaded)
        assertEquals(initialMessagesWithSent(), chatViewModel.messageList.toList())
    }

    @Test
    fun shouldUpdateMessageListWhenRepositoryEmitsNewMessages() = runTest {
        val fakeChatRepository = FakeChatRepository(initialMessages = initialMessagesWithSent())
        val chatViewModel = createChatViewModel(repository = fakeChatRepository)

        runCurrent()
        assertTrue(chatViewModel.isHistoryLoaded)
        assertEquals(initialMessagesWithSent(), chatViewModel.messageList.toList())

        val updatedMessages = initialMessagesWithSent() + ChatMessage(
            id = 3L,
            message = "What is Kotlin?",
            role = MessageRole.USER
        )

        fakeChatRepository.emitMessages(messages = updatedMessages)

        runCurrent()
        assertEquals(updatedMessages, chatViewModel.messageList.toList())
    }

    @Test
    fun shouldEmitPersistenceErrorAndContinueLoadingHistoryWhenRecoveryFails() = runTest {
        val fakeChatRepository = FakeChatRepository(
            initialMessages = initialMessagesWithPending(),
            recoveryException = ChatException.Persistence(
                cause = IOException("Database error")
            )
        )

        val chatViewModel = createChatViewModel(repository = fakeChatRepository)

        runCurrent()
        val event = chatViewModel.events.first()

        assertEquals(
            ChatUiEvent.ShowSnackbar(R.string.chat_exception_persistence),
            event
        )
        assertEquals(
            listOf(markPendingMessagesAsFailed, collectMessages),
            fakeChatRepository.operations
        )
        assertTrue(chatViewModel.isHistoryLoaded)
        assertEquals(
            initialMessagesWithPending(),
            chatViewModel.messageList.toList()
        )
    }

    private fun initialMessagesWithSent(): List<ChatMessage> {
        return listOf(
            ChatMessage(
                id = 1L,
                message = "Hello!",
                role = MessageRole.USER
            ),
            ChatMessage(
                id = 2L,
                message = "Hello! How can I help you?",
                role = MessageRole.MODEL
            )
        )
    }

    private fun initialMessagesWithPending(): List<ChatMessage> {
        return listOf(
            ChatMessage(
                id = 1L,
                message = "Hello!",
                role = MessageRole.USER,
                status = MessageStatus.PENDING
            )
        )
    }

    private fun createChatViewModel(repository: ChatRepository): ChatViewModel {
        return ChatViewModel(chatRepository = repository).also {
            viewModelStore.put(key = "ChatViewModel-${viewModelId++}", viewModel = it)
        }
    }
}