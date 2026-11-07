package me.gblrod.composechatbot.data.remote

import com.google.firebase.ai.type.FirebaseAIException
import com.google.firebase.ai.type.PromptBlockedException
import com.google.firebase.ai.type.QuotaExceededException
import com.google.firebase.ai.type.RequestTimeoutException
import com.google.firebase.ai.type.ServerException
import java.io.IOException
import kotlinx.coroutines.CancellationException
import me.gblrod.composechatbot.domain.exception.ChatException

suspend fun <T> safeApiCall(block: suspend () -> T): T {
    try {
        return block()

    } catch (e: CancellationException) {
        throw e

    } catch (e: QuotaExceededException) {
        throw ChatException.QuotaExceeded(cause = e)

    } catch (e: RequestTimeoutException) {
        throw ChatException.Timeout(cause = e)

    } catch (e: PromptBlockedException) {
        throw ChatException.ContentBlocked(cause = e)

    } catch (e: IOException) {
        throw ChatException.Network(cause = e)

    } catch (e: ServerException) {
        throw ChatException.Server(cause = e)

    } catch (e: FirebaseAIException) {
        if (e.hasCause<IOException>()) throw ChatException.Network(cause = e)
        throw ChatException.Unknown(cause = e)

    } catch (e: Exception) {
        throw ChatException.Unknown(cause = e)
    }
}

private inline fun <reified T : Throwable> Throwable.hasCause(): Boolean {
    var current: Throwable? = this

    while (current != null) {
        if (current is T) return true
        current = current.cause
    }

    return false
}