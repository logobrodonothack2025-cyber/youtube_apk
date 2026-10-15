package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.BroadcastChannel
import com.example.data.model.ChannelMessage
import com.example.data.model.User
import com.example.ui.theme.IgPrimary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChannelMessagesScreen(
    channel: BroadcastChannel,
    currentUser: User,
    onSendMessage: (text: String, mediaRes: Int?, isVideo: Boolean, mediaCaption: String?) -> Unit,
    onToggleReaction: (messageId: String, emoji: String) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler { onBack() }
    var textInput by remember { mutableStateOf("") }
    var showAddMediaSheet by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Box {
                            Image(
                                painter = painterResource(id = channel.avatarRes),
                                contentDescription = channel.title,
                                modifier = Modifier
                                    .size(38.dp)
                                    .clip(CircleShape),
                                contentScale = ContentScale.Crop
                            )
                            Surface(
                                shape = CircleShape,
                                color = IgPrimary,
                                modifier = Modifier
                                    .size(16.dp)
                                    .align(Alignment.BottomEnd)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text("📢", fontSize = 9.sp)
                                }
                            }
                        }

                        Spacer(modifier = Modifier.width(10.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = channel.title,
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 15.sp
                                    ),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = "Verified",
                                    tint = IgPrimary,
                                    modifier = Modifier.size(13.dp)
                                )
                            }
                            Text(
                                text = "${channel.creator.fullName} • ${"%,d".format(channel.membersCount)} members",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    fontSize = 11.5.sp
                                ),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                },
                actions = {
                    IconButton(onClick = {}) {
                        Icon(
                            imageVector = Icons.Default.NotificationsNone,
                            contentDescription = "Channel Notifications",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                    IconButton(onClick = {}) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = "Channel Info",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        bottomBar = {
            Surface(
                color = MaterialTheme.colorScheme.surface,
                tonalElevation = 6.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .windowInsetsPadding(WindowInsets.navigationBars)
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // ADD MEDIA BUTTON
                        Surface(
                            onClick = { showAddMediaSheet = true },
                            shape = CircleShape,
                            color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f),
                            modifier = Modifier
                                .size(42.dp)
                                .testTag("channel_add_media_btn")
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.AddPhotoAlternate,
                                    contentDescription = "Channel Messages Add Media",
                                    tint = IgPrimary,
                                    modifier = Modifier.size(22.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        // Text input
                        Surface(
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(22.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant
                        ) {
                            TextField(
                                value = textInput,
                                onValueChange = { textInput = it },
                                placeholder = {
                                    Text(
                                        text = "Broadcast to ${"%,d".format(channel.membersCount)} members...",
                                        fontSize = 13.sp,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                },
                                colors = TextFieldDefaults.colors(
                                    focusedContainerColor = Color.Transparent,
                                    unfocusedContainerColor = Color.Transparent,
                                    focusedIndicatorColor = Color.Transparent,
                                    unfocusedIndicatorColor = Color.Transparent
                                ),
                                singleLine = true,
                                modifier = Modifier
                                    .padding(horizontal = 6.dp)
                                    .testTag("channel_message_input")
                            )
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        IconButton(
                            onClick = {
                                if (textInput.isNotBlank()) {
                                    onSendMessage(textInput, null, false, null)
                                    textInput = ""
                                }
                            },
                            enabled = textInput.isNotBlank(),
                            modifier = Modifier.testTag("send_channel_message_btn")
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.Send,
                                contentDescription = "Send Broadcast Message",
                                tint = if (textInput.isNotBlank()) IgPrimary else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f)
                            )
                        }
                    }
                }
            }
        },
        modifier = modifier.testTag("channel_messages_screen")
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 14.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Channel Welcome Header Card
            item {
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f),
                    border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.15f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Image(
                            painter = painterResource(id = channel.avatarRes),
                            contentDescription = channel.title,
                            modifier = Modifier
                                .size(68.dp)
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = channel.title,
                            fontWeight = FontWeight.Bold,
                            fontSize = 17.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = channel.description,
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                fontSize = 12.5.sp
                            ),
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = IgPrimary.copy(alpha = 0.1f)
                        ) {
                            Text(
                                text = "📢 Only creators can send messages • Members can react",
                                color = IgPrimary,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                            )
                        }
                    }
                }
            }

            // Message items
            items(channel.messages, key = { it.id }) { msg ->
                ChannelMessageBubble(
                    message = msg,
                    onReact = { emoji -> onToggleReaction(msg.id, emoji) }
                )
            }
        }
    }

    // ADD MEDIA SHEET
    if (showAddMediaSheet) {
        AddMediaToChannelSheet(
            onSendMedia = { mediaRes, isVideo, caption, messageText ->
                onSendMessage(messageText, mediaRes, isVideo, caption)
                showAddMediaSheet = false
            },
            onDismiss = { showAddMediaSheet = false }
        )
    }
}

@Composable
fun ChannelMessageBubble(
    message: ChannelMessage,
    onReact: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("channel_message_${message.id}")
    ) {
        // Creator Row
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 4.dp)
        ) {
            Image(
                painter = painterResource(id = message.sender.avatarRes),
                contentDescription = message.sender.username,
                modifier = Modifier
                    .size(24.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = message.sender.username,
                fontWeight = FontWeight.Bold,
                fontSize = 12.5.sp
            )
            Spacer(modifier = Modifier.width(4.dp))
            Surface(
                shape = RoundedCornerShape(4.dp),
                color = IgPrimary
            ) {
                Text(
                    text = "CREATOR",
                    color = Color.White,
                    fontSize = 8.sp,
                    fontWeight = FontWeight.ExtraBold,
                    modifier = Modifier.padding(horizontal = 3.dp, vertical = 1.dp)
                )
            }
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = message.timestamp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 11.sp
            )
        }

        // Message Content Box
        Surface(
            shape = RoundedCornerShape(
                topStart = 4.dp,
                topEnd = 16.dp,
                bottomStart = 16.dp,
                bottomEnd = 16.dp
            ),
            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                // Attached Media (Photo or Video)
                if (message.mediaRes != null) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color.Black),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = message.mediaRes),
                            contentDescription = message.mediaCaption ?: "Attached media",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )

                        // Gradient overlay
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    Brush.verticalGradient(
                                        colors = listOf(
                                            Color.Transparent,
                                            Color.Black.copy(alpha = 0.6f)
                                        )
                                    )
                                )
                        )

                        if (message.isVideo) {
                            Surface(
                                shape = CircleShape,
                                color = Color.Black.copy(alpha = 0.6f),
                                modifier = Modifier.size(46.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = Icons.Default.PlayArrow,
                                        contentDescription = "Play video",
                                        tint = Color.White,
                                        modifier = Modifier.size(28.dp)
                                    )
                                }
                            }

                            Surface(
                                shape = RoundedCornerShape(4.dp),
                                color = Color.Black.copy(alpha = 0.7f),
                                modifier = Modifier
                                    .align(Alignment.BottomEnd)
                                    .padding(8.dp)
                            ) {
                                Text(
                                    text = "0:30 Video",
                                    color = Color.White,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                                )
                            }
                        }

                        if (!message.mediaCaption.isNullOrBlank()) {
                            Text(
                                text = message.mediaCaption,
                                color = Color.White,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier
                                    .align(Alignment.BottomStart)
                                    .padding(8.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                }

                // Text body
                if (message.text.isNotBlank()) {
                    Text(
                        text = message.text,
                        fontSize = 14.sp,
                        lineHeight = 20.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Reaction Pills
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val reactionsList = listOf("❤️", "🔥", "👏", "😍")
                    reactionsList.forEach { emoji ->
                        val count = message.reactions[emoji] ?: 0
                        val isUserReacted = message.userReactions.contains(emoji)

                        Surface(
                            onClick = { onReact(emoji) },
                            shape = RoundedCornerShape(16.dp),
                            color = if (isUserReacted) IgPrimary.copy(alpha = 0.2f) else MaterialTheme.colorScheme.surface,
                            border = androidx.compose.foundation.BorderStroke(
                                width = 1.dp,
                                color = if (isUserReacted) IgPrimary else MaterialTheme.colorScheme.outline.copy(alpha = 0.25f)
                            ),
                            modifier = Modifier.testTag("channel_reaction_${message.id}_$emoji")
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(text = emoji, fontSize = 13.sp)
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "$count",
                                    fontSize = 11.5.sp,
                                    fontWeight = if (isUserReacted) FontWeight.Bold else FontWeight.Normal,
                                    color = if (isUserReacted) IgPrimary else MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddMediaToChannelSheet(
    onSendMedia: (mediaRes: Int, isVideo: Boolean, caption: String, messageText: String) -> Unit,
    onDismiss: () -> Unit
) {
    val sampleMediaOptions = listOf(
        Triple(R.drawable.img_post_city, false, "Night lights in Shibuya 🌃"),
        Triple(R.drawable.img_post_travel, true, "Cliffs of Amalfi 4K Clip 🌊"),
        Triple(R.drawable.img_post_coffee, false, "Pour-over latte art study ☕"),
        Triple(R.drawable.img_user_avatar, false, "Golden hour studio portrait 📸")
    )

    var selectedMediaIndex by remember { mutableStateOf(0) }
    var mediaCaption by remember { mutableStateOf("") }
    var accompanyingText by remember { mutableStateOf("") }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        containerColor = MaterialTheme.colorScheme.surface,
        modifier = Modifier.testTag("add_media_to_channel_sheet")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(bottom = 32.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.AddPhotoAlternate,
                        contentDescription = null,
                        tint = IgPrimary,
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Channel Messages Add Media",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 17.sp
                        )
                    )
                }
                IconButton(onClick = onDismiss) {
                    Icon(Icons.Default.Close, contentDescription = "Close")
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Select Photo or Video from Gallery",
                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Media Picker Thumbnails
            LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                items(sampleMediaOptions.indices.toList()) { index ->
                    val (resId, isVideo, title) = sampleMediaOptions[index]
                    val isSelected = selectedMediaIndex == index

                    Box(
                        modifier = Modifier
                            .size(80.dp, 100.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .border(
                                width = if (isSelected) 3.dp else 1.dp,
                                color = if (isSelected) IgPrimary else MaterialTheme.colorScheme.outline.copy(alpha = 0.2f),
                                shape = RoundedCornerShape(10.dp)
                            )
                            .clickable { selectedMediaIndex = index }
                    ) {
                        Image(
                            painter = painterResource(id = resId),
                            contentDescription = title,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                        if (isVideo) {
                            Surface(
                                shape = CircleShape,
                                color = Color.Black.copy(alpha = 0.6f),
                                modifier = Modifier
                                    .size(24.dp)
                                    .align(Alignment.Center)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = Icons.Default.PlayArrow,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }
                        }
                        if (isSelected) {
                            Surface(
                                shape = CircleShape,
                                color = IgPrimary,
                                modifier = Modifier
                                    .size(20.dp)
                                    .align(Alignment.TopEnd)
                                    .padding(2.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(14.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Caption for Media
            OutlinedTextField(
                value = mediaCaption,
                onValueChange = { mediaCaption = it },
                label = { Text("Media Tagline / Caption") },
                placeholder = { Text(sampleMediaOptions[selectedMediaIndex].third) },
                singleLine = true,
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("media_caption_field")
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Accompanying message text
            OutlinedTextField(
                value = accompanyingText,
                onValueChange = { accompanyingText = it },
                label = { Text("Broadcast Message Text") },
                placeholder = { Text("Add context, presets download link, or details for your members...") },
                minLines = 2,
                maxLines = 4,
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("accompanying_text_field")
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Send Button
            Button(
                onClick = {
                    val (resId, isVideo, defaultTitle) = sampleMediaOptions[selectedMediaIndex]
                    val finalCaption = mediaCaption.ifBlank { defaultTitle }
                    val finalText = accompanyingText.ifBlank { "Shared new media with channel members ✨" }
                    onSendMedia(resId, isVideo, finalCaption, finalText)
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = IgPrimary,
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(22.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("confirm_send_channel_media")
            ) {
                Icon(Icons.Default.CloudUpload, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Broadcast Media to Channel", fontWeight = FontWeight.Bold, fontSize = 15.sp)
            }
        }
    }
}

@Composable
fun CreateChannelDialog(
    currentUser: User,
    onConfirm: (title: String, description: String, isSubscriberOnly: Boolean) -> Unit,
    onDismiss: () -> Unit
) {
    var channelName by remember { mutableStateOf("") }
    var channelDescription by remember { mutableStateOf("") }
    var isSubscriberOnly by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(
                    shape = CircleShape,
                    color = IgPrimary,
                    modifier = Modifier.size(36.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(text = "📢", fontSize = 18.sp)
                    }
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text("Create Broadcast Channel", fontWeight = FontWeight.Bold, fontSize = 17.sp)
                    Text("Direct updates to your audience", fontSize = 11.5.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = channelName,
                    onValueChange = { channelName = it },
                    label = { Text("Channel Name") },
                    placeholder = { Text("${currentUser.fullName}'s Insiders Club") },
                    singleLine = true,
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("channel_name_input")
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = channelDescription,
                    onValueChange = { channelDescription = it },
                    label = { Text("Topic & Description") },
                    placeholder = { Text("What will you share? Behind-the-scenes, tips, preset drops...") },
                    minLines = 2,
                    maxLines = 3,
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("channel_desc_input")
                )

                Spacer(modifier = Modifier.height(12.dp))

                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .padding(10.dp)
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Audience Type", fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                            Text(
                                text = if (isSubscriberOnly) "Subscribers only (Exclusive)" else "All followers can join",
                                fontSize = 11.5.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Switch(
                            checked = isSubscriberOnly,
                            onCheckedChange = { isSubscriberOnly = it },
                            modifier = Modifier.testTag("channel_subscriber_switch")
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val finalName = channelName.ifBlank { "${currentUser.fullName}'s Community 📢" }
                    val finalDesc = channelDescription.ifBlank { "Official updates, media previews, and announcements from @${currentUser.username}" }
                    onConfirm(finalName, finalDesc, isSubscriberOnly)
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = IgPrimary,
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier.testTag("confirm_create_channel_btn")
            ) {
                Text("Create Channel", fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
