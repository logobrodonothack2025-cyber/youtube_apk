package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.QueueMusic
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Story
import com.example.ui.theme.IgRedHeart
import kotlinx.coroutines.launch

@Composable
fun StoryViewerScreen(
    stories: List<Story>,
    initialIndex: Int,
    onClose: () -> Unit,
    onStoryViewed: (String) -> Unit,
    onAddToPlaylist: (Story) -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler { onClose() }

    var currentIndex by remember { mutableStateOf(initialIndex.coerceIn(0, stories.size - 1)) }
    val currentStory = stories.getOrNull(currentIndex) ?: run {
        onClose()
        return
    }

    val progress = remember { Animatable(0f) }
    val coroutineScope = rememberCoroutineScope()
    var isPaused by remember { mutableStateOf(false) }
    var replyText by remember { mutableStateOf("") }
    var isHearted by remember { mutableStateOf(false) }

    LaunchedEffect(currentIndex) {
        onStoryViewed(currentStory.id)
        progress.snapTo(0f)
        progress.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 5000, easing = LinearEasing)
        )
        // Auto advance
        if (currentIndex < stories.size - 1) {
            currentIndex++
        } else {
            onClose()
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black)
            .testTag("story_viewer_screen")
    ) {
        // Story Full Image
        Image(
            painter = painterResource(id = currentStory.imageRes),
            contentDescription = "Story from ${currentStory.user.username}",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // Gradient top & bottom protection
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Black.copy(alpha = 0.6f),
                            Color.Transparent,
                            Color.Transparent,
                            Color.Black.copy(alpha = 0.7f)
                        )
                    )
                )
        )

        // Tap zones for Left (prev) / Right (next) navigation
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 100.dp)
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .pointerInput(Unit) {
                        detectTapGestures(
                            onTap = {
                                if (currentIndex > 0) {
                                    currentIndex--
                                }
                            }
                        )
                    }
            )
            Box(
                modifier = Modifier
                    .weight(2f)
                    .fillMaxHeight()
                    .pointerInput(Unit) {
                        detectTapGestures(
                            onTap = {
                                if (currentIndex < stories.size - 1) {
                                    currentIndex++
                                } else {
                                    onClose()
                                }
                            }
                        )
                    }
            )
        }

        // Top Header: Progress Segments & User Info
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .windowInsetsPadding(WindowInsets.statusBars)
                .padding(horizontal = 12.dp, vertical = 8.dp)
        ) {
            // Segmented Progress Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 10.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                stories.forEachIndexed { index, _ ->
                    val segmentProgress = when {
                        index < currentIndex -> 1f
                        index == currentIndex -> progress.value
                        else -> 0f
                    }
                    LinearProgressIndicator(
                        progress = { segmentProgress },
                        modifier = Modifier
                            .weight(1f)
                            .height(2.5.dp)
                            .clip(RoundedCornerShape(2.dp)),
                        color = Color.White,
                        trackColor = Color.White.copy(alpha = 0.3f)
                    )
                }
            }

            // User Info & Close Button
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        painter = painterResource(id = currentStory.user.avatarRes),
                        contentDescription = "Avatar",
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = currentStory.user.username,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.5.sp
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = currentStory.timeAgo,
                        color = Color.White.copy(alpha = 0.7f),
                        fontSize = 12.sp
                    )
                }

                IconButton(
                    onClick = onClose,
                    modifier = Modifier.testTag("close_story_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close story",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }

        // Bottom Story Reply Bar
        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .windowInsetsPadding(WindowInsets.navigationBars)
                .padding(horizontal = 14.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = replyText,
                onValueChange = { replyText = it },
                placeholder = {
                    Text(
                        text = "Send message to ${currentStory.user.username}...",
                        color = Color.White.copy(alpha = 0.7f),
                        fontSize = 13.sp
                    )
                },
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp),
                shape = RoundedCornerShape(24.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.White.copy(alpha = 0.7f),
                    unfocusedBorderColor = Color.White.copy(alpha = 0.3f),
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                ),
                singleLine = true
            )

            Spacer(modifier = Modifier.width(10.dp))

            // Add to Playlist Button
            IconButton(
                onClick = { onAddToPlaylist(currentStory) },
                modifier = Modifier
                    .size(44.dp)
                    .testTag("story_playlist_button")
            ) {
                Icon(
                    imageVector = Icons.Outlined.QueueMusic,
                    contentDescription = "Add story to playlist",
                    tint = Color.White,
                    modifier = Modifier.size(26.dp)
                )
            }

            // Quick Heart Reaction
            IconButton(
                onClick = { isHearted = !isHearted },
                modifier = Modifier.size(44.dp)
            ) {
                Icon(
                    imageVector = if (isHearted) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                    contentDescription = "Heart reaction",
                    tint = if (isHearted) IgRedHeart else Color.White,
                    modifier = Modifier.size(28.dp)
                )
            }
        }
    }
}
