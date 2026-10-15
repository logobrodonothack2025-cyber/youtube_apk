package com.example.ui.screens

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Send
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.Post
import com.example.data.model.Story
import com.example.data.model.User
import com.example.ui.components.DoubleTapHeartOverlay
import com.example.ui.components.StoryAvatar
import com.example.ui.components.getFilterColorFilter
import com.example.ui.theme.IgPrimary
import com.example.ui.theme.IgRedHeart
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(
    posts: List<Post>,
    stories: List<Story>,
    currentUser: User,
    onStoryClick: (Int) -> Unit,
    onLikePost: (String) -> Unit,
    onSavePost: (String) -> Unit,
    onAddToPlaylist: (Post) -> Unit,
    onCommentClick: (String) -> Unit,
    onShareClick: (Post) -> Unit,
    onUserClick: (User) -> Unit,
    isErrorState: Boolean = false,
    onRetryFeed: () -> Unit = {},
    onSimulateErrorClick: () -> Unit = {},
    onAudioClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("home_feed_list")
    ) {
        // Stories Tray
        item {
            StoriesTray(
                stories = stories,
                currentUser = currentUser,
                onStoryClick = onStoryClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            )
            HorizontalDivider(
                thickness = 0.5.dp,
                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.15f)
            )
        }

        // Test Error / Home Page Not Working Trigger Bar
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (isErrorState) "⚠️ Home Page Status: Not Working" else "Feed Status: Connected",
                    fontSize = 11.5.sp,
                    color = if (isErrorState) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurfaceVariant,
                    fontWeight = FontWeight.Medium
                )

                Surface(
                    onClick = onSimulateErrorClick,
                    shape = RoundedCornerShape(12.dp),
                    color = if (isErrorState) MaterialTheme.colorScheme.errorContainer else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
                    border = androidx.compose.foundation.BorderStroke(0.5.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)),
                    modifier = Modifier.testTag("home_simulate_error_btn")
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Warning,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.error,
                            modifier = Modifier.size(12.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = if (isErrorState) "Showing: Not Working (Retry)" else "Test 'Not Working' Dialog",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = if (isErrorState) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }

        // Home Page Not Working Error Card
        if (isErrorState) {
            item {
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.45f),
                    border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.error.copy(alpha = 0.6f)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                        .testTag("home_page_not_working_card")
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.ErrorOutline,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.error,
                            modifier = Modifier.size(48.dp)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "Something went wrong",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp,
                                color = MaterialTheme.colorScheme.error
                            )
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "We're having trouble loading your home feed right now. Tap 'Try again' to reload or open the error dialog.",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                fontSize = 13.sp
                            ),
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            Button(
                                onClick = onRetryFeed,
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = IgPrimary,
                                    contentColor = Color.White
                                ),
                                shape = RoundedCornerShape(20.dp),
                                modifier = Modifier.testTag("home_feed_try_again_btn")
                            ) {
                                Icon(Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Try again", fontWeight = FontWeight.Bold)
                            }
                            OutlinedButton(
                                onClick = onSimulateErrorClick,
                                shape = RoundedCornerShape(20.dp),
                                modifier = Modifier.testTag("home_open_error_dialog_btn")
                            ) {
                                Text("Open Dialog", fontWeight = FontWeight.SemiBold)
                            }
                        }
                    }
                }
            }
        }

        // Feed Posts or Empty State
        if (posts.isEmpty()) {
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 32.dp, vertical = 60.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            .border(2.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.BookmarkBorder,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(40.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "No posts yet",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "When you or accounts you follow share photos and videos, they'll appear here.",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontSize = 13.sp
                        ),
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                }
            }
        } else {
            items(posts, key = { it.id }) { post ->
                PostCard(
                    post = post,
                    onLike = { onLikePost(post.id) },
                    onSave = { onSavePost(post.id) },
                    onAddToPlaylist = { onAddToPlaylist(post) },
                    onCommentClick = { onCommentClick(post.id) },
                    onShareClick = { onShareClick(post) },
                    onUserClick = { onUserClick(post.user) },
                    onAudioClick = onAudioClick,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}

@Composable
fun StoriesTray(
    stories: List<Story>,
    currentUser: User,
    onStoryClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyRow(
        modifier = modifier.testTag("stories_tray"),
        contentPadding = PaddingValues(horizontal = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        itemsIndexed(stories) { index, story ->
            val isMe = story.id == "story_me"
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.width(72.dp)
            ) {
                StoryAvatar(
                    avatarRes = story.user.avatarRes,
                    size = 68.dp,
                    hasStory = !isMe || story.isViewed,
                    isViewed = story.isViewed,
                    isCloseFriend = story.isCloseFriend,
                    isSelfAdd = isMe,
                    onClick = { onStoryClick(index) }
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = if (isMe) "Your story" else story.user.username,
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Normal
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PostCard(
    post: Post,
    onLike: () -> Unit,
    onSave: () -> Unit,
    onAddToPlaylist: () -> Unit,
    onCommentClick: () -> Unit,
    onShareClick: () -> Unit,
    onUserClick: () -> Unit,
    onAudioClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val coroutineScope = rememberCoroutineScope()
    var showBurstHeart by remember { mutableStateOf(false) }
    var isExpandedCaption by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .background(MaterialTheme.colorScheme.surface)
            .testTag("post_card_${post.id}")
    ) {
        // Post Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .weight(1f)
                    .clickable { onUserClick() }
            ) {
                StoryAvatar(
                    avatarRes = post.user.avatarRes,
                    size = 38.dp,
                    hasStory = true,
                    isViewed = false,
                    onClick = onUserClick
                )
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = post.user.username,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.5.sp
                            )
                        )
                        if (post.user.isVerified) {
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(
                                imageVector = Icons.Filled.CheckCircle,
                                contentDescription = "Verified",
                                tint = IgPrimary,
                                modifier = Modifier.size(13.dp)
                            )
                        }
                    }
                    if (!post.location.isNullOrEmpty()) {
                        Text(
                            text = post.location,
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                fontSize = 11.sp
                            ),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }

            IconButton(
                onClick = { /* More options */ },
                modifier = Modifier.size(36.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = "Post Options",
                    tint = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.size(20.dp)
                )
            }
        }

        // Post Image with Double-Tap detection
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .combinedClickable(
                    onDoubleClick = {
                        if (!post.isLiked) {
                            onLike()
                        }
                        coroutineScope.launch {
                            showBurstHeart = true
                            delay(800)
                            showBurstHeart = false
                        }
                    },
                    onClick = {}
                ),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = post.imageRes),
                contentDescription = "Post photo by ${post.user.username}",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
                colorFilter = getFilterColorFilter(post.filterName)
            )

            // Animated Burst Heart Overlay
            DoubleTapHeartOverlay(
                isTriggered = showBurstHeart,
                modifier = Modifier.align(Alignment.Center)
            )
        }

        // Post Actions Row (Like, Comment, Share, Bookmark)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Heart Like Button with Spring Pop
                val heartScale by animateFloatAsState(
                    targetValue = if (post.isLiked) 1.2f else 1.0f,
                    animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
                    label = "heart_scale"
                )
                IconButton(
                    onClick = onLike,
                    modifier = Modifier
                        .scale(heartScale)
                        .testTag("post_like_button_${post.id}")
                ) {
                    Icon(
                        imageVector = if (post.isLiked) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                        contentDescription = if (post.isLiked) "Unlike" else "Like",
                        tint = if (post.isLiked) IgRedHeart else MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.size(26.dp)
                    )
                }

                // Comment Button
                IconButton(
                    onClick = onCommentClick,
                    modifier = Modifier.testTag("post_comment_button_${post.id}")
                ) {
                    Icon(
                        imageVector = Icons.Outlined.ChatBubbleOutline,
                        contentDescription = "Comment",
                        tint = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.size(24.dp)
                    )
                }

                // Share / Direct Message Button
                IconButton(
                    onClick = onShareClick,
                    modifier = Modifier.testTag("post_share_button_${post.id}")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Outlined.Send,
                        contentDescription = "Share",
                        tint = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                // Add to Playlist Button
                IconButton(
                    onClick = onAddToPlaylist,
                    modifier = Modifier.testTag("post_playlist_button_${post.id}")
                ) {
                    Icon(
                        imageVector = Icons.Outlined.QueueMusic,
                        contentDescription = "Add to playlist",
                        tint = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.size(24.dp)
                    )
                }

                // Save / Bookmark Button
                IconButton(
                    onClick = onSave,
                    modifier = Modifier.testTag("post_save_button_${post.id}")
                ) {
                    Icon(
                        imageVector = if (post.isSaved) Icons.Filled.Bookmark else Icons.Outlined.BookmarkBorder,
                        contentDescription = if (post.isSaved) "Remove from saved" else "Save post",
                        tint = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.size(26.dp)
                    )
                }
            }
        }

        // Post Details: Likes, Caption, Comments Preview, Time
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp)
        ) {
            // Likes Count
            Text(
                text = "${"%,d".format(post.likesCount)} likes",
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.5.sp
                )
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Caption with Username prefix
            val captionText = buildAnnotatedString {
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                    append(post.user.username)
                    append(" ")
                }
                append(post.caption)
            }

            Text(
                text = captionText,
                style = MaterialTheme.typography.bodyMedium.copy(fontSize = 13.5.sp),
                maxLines = if (isExpandedCaption) Int.MAX_VALUE else 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.clickable { isExpandedCaption = !isExpandedCaption }
            )

            if (!isExpandedCaption && post.caption.length > 60) {
                Text(
                    text = "more",
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 12.sp
                    ),
                    modifier = Modifier
                        .clickable { isExpandedCaption = true }
                        .padding(top = 1.dp)
                )
            }

            if (post.caption.contains("Bruno Major", ignoreCase = true) || post.caption.contains("Nothing", ignoreCase = true)) {
                Surface(
                    onClick = onAudioClick,
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.55f),
                    modifier = Modifier
                        .padding(top = 4.dp, bottom = 2.dp)
                        .testTag("home_post_audio_chip_${post.id}")
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.MusicNote,
                            contentDescription = "Audio track",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(13.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Bruno Major • Nothing",
                            fontSize = 11.5.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            // View all comments
            if (post.commentsCount > 0) {
                Text(
                    text = "View all ${post.commentsCount} comments",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 13.sp
                    ),
                    modifier = Modifier
                        .clickable { onCommentClick() }
                        .padding(vertical = 2.dp)
                )
            }

            // Timestamp
            Text(
                text = post.timeAgo,
                style = MaterialTheme.typography.labelSmall.copy(
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                    fontSize = 10.5.sp,
                    fontWeight = FontWeight.Normal
                ),
                modifier = Modifier.padding(top = 2.dp, bottom = 4.dp)
            )
        }
    }
}
