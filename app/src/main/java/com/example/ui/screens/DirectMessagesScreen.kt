package com.example.ui.screens

import androidx.activity.compose.BackHandler
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
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.BroadcastChannel
import com.example.data.model.ChatConversation
import com.example.data.model.ChatMessage
import com.example.data.model.User
import com.example.ui.theme.IgCloseFriendsGreen
import com.example.ui.theme.IgPrimary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DirectMessagesScreen(
    currentUser: User,
    conversations: List<ChatConversation>,
    channels: List<BroadcastChannel> = emptyList(),
    onSelectConversation: (String) -> Unit,
    onSelectChannel: (String) -> Unit = {},
    onCreateChannelClick: () -> Unit = {},
    onBack: () -> Unit,
    onTriggerError: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    BackHandler { onBack() }
    var dmSearchQuery by remember { mutableStateOf("") }
    var dmTab by remember { mutableStateOf("messages") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = currentUser.username,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )
                    )
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
                    // Create Broadcast Channel Action
                    IconButton(
                        onClick = onCreateChannelClick,
                        modifier = Modifier.testTag("dm_create_channel_action_btn")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Campaign,
                            contentDescription = "Create Broadcast Channel",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    // Action button to test/demonstrate "Something went wrong"
                    IconButton(
                        onClick = onTriggerError,
                        modifier = Modifier.testTag("dm_error_trigger_btn")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = "Report issue / Something went wrong",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    IconButton(onClick = { /* New message */ }) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "New Message",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        modifier = modifier.testTag("direct_messages_screen")
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Search Bar
            item {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 6.dp),
                    shape = RoundedCornerShape(10.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        TextField(
                            value = dmSearchQuery,
                            onValueChange = { dmSearchQuery = it },
                            placeholder = { Text("Search", fontSize = 13.5.sp) },
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = Color.Transparent,
                                unfocusedContainerColor = Color.Transparent,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent
                            ),
                            singleLine = true,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }

            // Session Expired / Error Test Chip
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 2.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    Surface(
                        onClick = onTriggerError,
                        shape = RoundedCornerShape(12.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
                        border = androidx.compose.foundation.BorderStroke(0.5.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)),
                        modifier = Modifier.testTag("dm_session_expired_trigger_btn")
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.LockClock,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.error,
                                modifier = Modifier.size(12.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Test 'Session Expired' Dialog",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }

            // Account Deactivation / Deletion Notice banner if current account is deactivated
            if (currentUser.isDeactivated || currentUser.isScheduledForDeletion) {
                item {
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 14.dp, vertical = 6.dp),
                        shape = RoundedCornerShape(10.dp),
                        color = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.5f)
                    ) {
                        Row(
                            modifier = Modifier
                                .clickable { onTriggerError() }
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Info,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.error,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "Account Scheduled for Deletion",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    color = MaterialTheme.colorScheme.error
                                )
                                Text(
                                    text = "${currentUser.deletionDaysLeft} days left until permanent deletion (${currentUser.deletionDate}). Messages may be limited.",
                                    fontSize = 11.5.sp,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }
                }
            }

            // Notes Tray (Instagram Notes)
            item {
                Text(
                    text = "Notes",
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    ),
                    modifier = Modifier.padding(start = 16.dp, top = 8.dp, bottom = 4.dp)
                )

                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp),
                    contentPadding = PaddingValues(horizontal = 14.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Your note
                    item {
                        NoteAvatarBubble(
                            avatarRes = currentUser.avatarRes,
                            name = "Your note",
                            noteText = currentUser.note ?: "Share a thought..."
                        )
                    }

                    // Friends notes
                    items(conversations) { conv ->
                        if (!conv.user.note.isNullOrBlank()) {
                            NoteAvatarBubble(
                                avatarRes = conv.user.avatarRes,
                                name = conv.user.username,
                                noteText = conv.user.note
                            )
                        }
                    }
                }

                HorizontalDivider(thickness = 0.5.dp, color = MaterialTheme.colorScheme.outline.copy(alpha = 0.15f))
            }

            // Sub-tabs: Messages vs Channels
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 6.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    FilterChip(
                        selected = dmTab == "messages",
                        onClick = { dmTab = "messages" },
                        label = {
                            Text(
                                text = "Messages",
                                fontWeight = if (dmTab == "messages") FontWeight.Bold else FontWeight.Normal,
                                fontSize = 13.sp
                            )
                        },
                        modifier = Modifier.testTag("dm_tab_messages")
                    )

                    FilterChip(
                        selected = dmTab == "channels",
                        onClick = { dmTab = "channels" },
                        leadingIcon = { Text("📢", fontSize = 12.sp) },
                        label = {
                            Text(
                                text = "Channels (${channels.size})",
                                fontWeight = if (dmTab == "channels") FontWeight.Bold else FontWeight.Normal,
                                fontSize = 13.sp
                            )
                        },
                        modifier = Modifier.testTag("dm_tab_channels")
                    )
                }
            }

            if (dmTab == "messages") {
                // Messages Header
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Messages",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp
                            )
                        )
                        Text(
                            text = "Requests (2)",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = IgPrimary,
                                fontWeight = FontWeight.SemiBold
                            ),
                            modifier = Modifier
                                .clickable { onTriggerError() }
                                .testTag("dm_requests_button")
                        )
                    }
                }

                // Conversations List
            val filteredConversations = conversations.filter {
                dmSearchQuery.isBlank() || it.user.username.contains(dmSearchQuery, ignoreCase = true) ||
                it.user.fullName.contains(dmSearchQuery, ignoreCase = true)
            }

            items(filteredConversations, key = { it.id }) { conv ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onSelectConversation(conv.id) }
                        .padding(horizontal = 16.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Avatar with online status dot
                    Box(modifier = Modifier.size(54.dp)) {
                        Image(
                            painter = painterResource(id = conv.user.avatarRes),
                            contentDescription = conv.user.username,
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )
                        if (conv.isOnline) {
                            Box(
                                modifier = Modifier
                                    .align(Alignment.BottomEnd)
                                    .size(14.dp)
                                    .background(MaterialTheme.colorScheme.surface, CircleShape)
                                    .padding(2.dp)
                                    .background(IgCloseFriendsGreen, CircleShape)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = conv.user.fullName,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = if (conv.unreadCount > 0) FontWeight.Bold else FontWeight.SemiBold,
                                fontSize = 14.5.sp
                            )
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "${conv.lastMessage} • ${conv.timestamp}",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = if (conv.unreadCount > 0) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant,
                                fontWeight = if (conv.unreadCount > 0) FontWeight.Bold else FontWeight.Normal,
                                fontSize = 13.sp
                            ),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                    // Camera quick button
                    IconButton(
                        onClick = { /* Quick photo message */ },
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.CameraAlt,
                            contentDescription = "Send camera shot",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
            } else {
                // CHANNELS TAB CONTENT
                item {
                    // Create Channel Promotion Card
                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.35f),
                        border = androidx.compose.foundation.BorderStroke(1.dp, IgPrimary.copy(alpha = 0.5f)),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 6.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                shape = CircleShape,
                                color = IgPrimary,
                                modifier = Modifier.size(42.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text("📢", fontSize = 20.sp)
                                }
                            }

                            Spacer(modifier = Modifier.width(12.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "Create Broadcast Channel",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.5.sp
                                )
                                Text(
                                    text = "Send updates, media drops, and messages to your followers.",
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }

                            Spacer(modifier = Modifier.width(8.dp))

                            Button(
                                onClick = onCreateChannelClick,
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = IgPrimary,
                                    contentColor = Color.White
                                ),
                                shape = RoundedCornerShape(16.dp),
                                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                                modifier = Modifier.testTag("create_channel_btn")
                            ) {
                                Text("Create", fontSize = 12.5.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }

                item {
                    Text(
                        text = "Joined Broadcast Channels",
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.5.sp
                        ),
                        modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 10.dp, bottom = 4.dp)
                    )
                }

                // Channels list
                val filteredChannels = channels.filter {
                    dmSearchQuery.isBlank() || it.title.contains(dmSearchQuery, ignoreCase = true) ||
                    it.creator.username.contains(dmSearchQuery, ignoreCase = true)
                }

                items(filteredChannels, key = { it.id }) { channel ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onSelectChannel(channel.id) }
                            .padding(horizontal = 16.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Channel Avatar with broadcast badge
                        Box(modifier = Modifier.size(54.dp)) {
                            Image(
                                painter = painterResource(id = channel.avatarRes),
                                contentDescription = channel.title,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clip(CircleShape),
                                contentScale = ContentScale.Crop
                            )
                            Surface(
                                shape = CircleShape,
                                color = IgPrimary,
                                modifier = Modifier
                                    .align(Alignment.BottomEnd)
                                    .size(18.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text("📢", fontSize = 10.sp)
                                }
                            }
                        }

                        Spacer(modifier = Modifier.width(14.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = channel.title,
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.5.sp
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
                            Spacer(modifier = Modifier.height(2.dp))
                            val lastMsg = channel.messages.lastOrNull()
                            val msgPreview = when {
                                lastMsg?.mediaRes != null && lastMsg.isVideo -> "🎬 [Video] ${lastMsg.mediaCaption ?: lastMsg.text}"
                                lastMsg?.mediaRes != null -> "📷 [Photo] ${lastMsg.mediaCaption ?: lastMsg.text}"
                                lastMsg != null -> lastMsg.text
                                else -> channel.description
                            }
                            Text(
                                text = "${channel.creator.fullName} • ${"%,d".format(channel.membersCount)} members",
                                style = MaterialTheme.typography.labelSmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Text(
                                text = msgPreview,
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    fontSize = 12.sp
                                ),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Open channel",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
                            modifier = Modifier
                                .size(16.dp)
                                .rotate(180f)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun NoteAvatarBubble(
    avatarRes: Int,
    name: String,
    noteText: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.width(76.dp)
    ) {
        // Speech Bubble on top of avatar
        Surface(
            shape = RoundedCornerShape(12.dp),
            color = MaterialTheme.colorScheme.surfaceVariant,
            shadowElevation = 2.dp,
            modifier = Modifier.offset(y = 6.dp)
        ) {
            Text(
                text = noteText,
                style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
            )
        }

        Image(
            painter = painterResource(id = avatarRes),
            contentDescription = name,
            modifier = Modifier
                .size(54.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = name,
            style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatConversationScreen(
    conversation: ChatConversation,
    onSendMessage: (String) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler { onBack() }
    var textInput by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Image(
                            painter = painterResource(id = conversation.user.avatarRes),
                            contentDescription = conversation.user.username,
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = conversation.user.username,
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.5.sp
                                )
                            )
                            Text(
                                text = if (conversation.isOnline) "Active now" else "Active recently",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = if (conversation.isOnline) IgCloseFriendsGreen else MaterialTheme.colorScheme.onSurfaceVariant,
                                    fontSize = 11.sp
                                )
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
                            imageVector = Icons.Default.Videocam,
                            contentDescription = "Video Call",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                    IconButton(onClick = {}) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = "Details",
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
                tonalElevation = 4.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .windowInsetsPadding(WindowInsets.navigationBars)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(22.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant
                    ) {
                        TextField(
                            value = textInput,
                            onValueChange = { textInput = it },
                            placeholder = { Text("Message...", fontSize = 13.5.sp) },
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = Color.Transparent,
                                unfocusedContainerColor = Color.Transparent,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent
                            ),
                            singleLine = true,
                            modifier = Modifier.padding(horizontal = 6.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    if (textInput.isNotBlank()) {
                        IconButton(
                            onClick = {
                                onSendMessage(textInput)
                                textInput = ""
                            }
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.Send,
                                contentDescription = "Send message",
                                tint = IgPrimary
                            )
                        }
                    } else {
                        IconButton(onClick = {}) {
                            Icon(
                                imageVector = Icons.Default.CameraAlt,
                                contentDescription = "Camera",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        },
        modifier = modifier.testTag("chat_conversation_screen")
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 14.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(conversation.messages, key = { it.id }) { msg ->
                ChatBubble(message = msg)
            }
        }
    }
}

@Composable
fun ChatBubble(message: ChatMessage) {
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = if (message.isMine) Alignment.CenterEnd else Alignment.CenterStart
    ) {
        Surface(
            shape = RoundedCornerShape(
                topStart = 18.dp,
                topEnd = 18.dp,
                bottomStart = if (message.isMine) 18.dp else 4.dp,
                bottomEnd = if (message.isMine) 4.dp else 18.dp
            ),
            color = if (message.isMine) IgPrimary else MaterialTheme.colorScheme.surfaceVariant,
            modifier = Modifier.widthIn(max = 280.dp)
        ) {
            Column(modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp)) {
                Text(
                    text = message.text,
                    color = if (message.isMine) Color.White else MaterialTheme.colorScheme.onSurface,
                    fontSize = 14.sp,
                    lineHeight = 19.sp
                )
                Text(
                    text = message.timestamp,
                    color = if (message.isMine) Color.White.copy(alpha = 0.7f) else MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 10.sp,
                    modifier = Modifier.align(Alignment.End).padding(top = 2.dp)
                )
            }
        }
    }
}
