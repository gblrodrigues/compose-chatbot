package me.gblrod.composechatbot.feature.chat.components

import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import java.io.File

@Composable
fun ChatMessageImage(
    imagePath: String,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    val imageFile = remember(context, imagePath) {
        File(context.filesDir, imagePath)
    }

    AsyncImage(
        model = imageFile,
        contentDescription = null,
        contentScale = ContentScale.Fit,
        modifier = modifier
            .widthIn(max = 280.dp)
            .heightIn(max = 240.dp)
            .clip(shape = MaterialTheme.shapes.large)
    )
}