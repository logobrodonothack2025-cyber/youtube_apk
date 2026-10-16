package com.example.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Send
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import kotlinx.coroutines.launch
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ReelItem
import com.example.data.model.User
import com.example.ui.components.StoryAvatar
import com.example.ui.theme.IgPrimary
import com.example.ui.theme.IgRedHeart

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReelsScreen(
    reels: List<ReelItem>,
    onLikeReel: (String) -> Unit,
    onCommentClick: (String) -> Unit,
    onShareClick: (ReelItem) -> Unit,
    onAddToPlaylist: (ReelItem) -> Unit,
    onUserClick: (User) -> Unit,
    onGraduateTrialReel: (String) -> Unit = {},
    onAudioClick: (String) -> Unit = {},
    onRepostReel: (String) -> Unit = {},
    modifier: Modifier = Modifier
) {
    val pagerState = rememberPagerState(pageCount = { reels.size })
    val coroutineScope = rememberCoroutineScope()
    var showWatchUpNextSheet by remember { mutableStateOf(false) }
    var autoplayUpNext by remember { mutableStateOf(true) }
    var selectedTrialReelForInsights by remember { mutableStateOf<ReelItem?>(null) }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black)
            .testTag("reels_screen")
    ) {
        VerticalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize()
        ) { page ->
            val reel = reels[page]
            val nextReel = if (page + 1 < reels.size) reels[page + 1] else reels.firstOrNull()

            ReelCard(
                reel = reel,
                nextReel = nextReel,
                onLike = { onLikeReel(reel.id) },
                onCommentClick = { onCommentClick(reel.id) },
                onShareClick = { onShareClick(reel) },
                onAddToPlaylist = { onAddToPlaylist(reel) },
                onUserClick = { onUserClick(reel.user) },
                onOpenWatchUpNext = { showWatchUpNextSheet = true },
                onOpenTrialInsights = { selectedTrialReelForInsights = reel },
                onAudioClick = { onAudioClick(reel.audioTrack) },
                onRepost = { onRepostReel(reel.id) }
            )
        }

        // Top Overlay Header: "Reels" + Watch Up Next Pill + Camera
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp)
                .windowInsetsPadding(WindowInsets.statusBars),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Reels",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    fontSize = 22.sp
                )
            )

            Row(verticalAlignment = Alignment.CenterVertically) {
                // "Watch Up Next" Pill Button
                Surface(
                    onClick = { showWatchUpNextSheet = true },
                    color = Color.Black.copy(alpha = 0.55f),
                    shape = RoundedCornerShape(20.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.45f)),
                    modifier = Modifier.testTag("watch_up_next_button")
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.QueueMusic,
                            contentDescription = "Watch Up Next",
                            tint = Color.White,
                            modifier = Modifier.size(15.dp)
                        )
                        Spacer(modifier = Modifier.width(5.dp))
                        Text(
                            text = "Watch Up Next",
                            color = Color.White,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 12.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.width(8.dp))

                IconButton(onClick = { /* Open reel camera */ }) {
                    Icon(
                        imageVector = Icons.Default.PhotoCamera,
                        contentDescription = "Camera",
                        tint = Color.White,
                        modifier = Modifier.size(26.dp)
                    )
                }
            }
        }

        // WATCH UP NEXT MODAL BOTTOM SHEET
        if (showWatchUpNextSheet) {
            val currentReel = reels.getOrNull(pagerState.currentPage) ?: reels.first()
            WatchUpNextSheet(
                currentReel = currentReel,
                reels = reels,
                currentIndex = pagerState.currentPage,
                autoplay = autoplayUpNext,
                onAutoplayChange = { autoplayUpNext = it },
                onSelectReel = { targetIndex ->
                    coroutineScope.launch {
                        pagerState.animateScrollToPage(targetIndex)
                    }
                    showWatchUpNextSheet = false
                },
                onDismiss = { showWatchUpNextSheet = false }
            )
        }

        // TRIAL REEL INSIGHTS MODAL
        selectedTrialReelForInsights?.let { trialReel ->
            TrialReelInsightsDialog(
                reel = trialReel,
                onGraduate = {
                    onGraduateTrialReel(trialReel.id)
                    selectedTrialReelForInsights = null
                },
                onDismiss = { selectedTrialReelForInsights = null }
            )
        }
    }
}

@Composable
fun ReelCard(
    reel: ReelItem,
    nextReel: ReelItem? = null,
    onLike: () -> Unit,
    onCommentClick: () -> Unit,
    onShareClick: () -> Unit,
    onAddToPlaylist: () -> Unit,
    onUserClick: () -> Unit,
    onOpenWatchUpNext: () -> Unit = {},
    onOpenTrialInsights: () -> Unit = {},
    onAudioClick: () -> Unit = {},
    onRepost: () -> Unit = {}
) {
    var isFollowing by remember { mutableStateOf(false) }

    // Vinyl spinning animation
    val infiniteTransition = rememberInfiniteTransition(label = "vinyl_spin")
    val angle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(4000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "vinyl_angle"
    )

    Box(modifier = Modifier.fillMaxSize()) {
        // Video Thumbnail Mockup
        Image(
            painter = painterResource(id = reel.thumbnailRes),
            contentDescription = "Reel video",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // Gradient shadow overlay on bottom and top
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Black.copy(alpha = 0.4f),
                            Color.Transparent,
                            Color.Transparent,
                            Color.Black.copy(alpha = 0.85f)
                        )
                    )
                )
        )

        // Right Action Rail (Heart, Comment, Share, Audio)
        Column(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 12.dp, bottom = 28.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Like
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                IconButton(
                    onClick = onLike,
                    modifier = Modifier.size(44.dp)
                ) {
                    Icon(
                        imageVector = if (reel.isLiked) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                        contentDescription = "Like reel",
                        tint = if (reel.isLiked) IgRedHeart else Color.White,
                        modifier = Modifier.size(30.dp)
                    )
                }
                Text(
                    text = "${"%,d".format(reel.likesCount)}",
                    color = Color.White,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            // Comment
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                IconButton(
                    onClick = onCommentClick,
                    modifier = Modifier.size(44.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.ChatBubbleOutline,
                        contentDescription = "Comment on reel",
                        tint = Color.White,
                        modifier = Modifier.size(28.dp)
                    )
                }
                Text(
                    text = "${"%,d".format(reel.commentsCount)}",
                    color = Color.White,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            // Share
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                IconButton(
                    onClick = onShareClick,
                    modifier = Modifier.size(44.dp)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Outlined.Send,
                        contentDescription = "Share reel",
                        tint = Color.White,
                        modifier = Modifier.size(26.dp)
                    )
                }
                Text(
                    text = "${"%,d".format(reel.sharesCount)}",
                    color = Color.White,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            // Repost Reel
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                IconButton(
                    onClick = onRepost,
                    modifier = Modifier
                        .size(44.dp)
                        .testTag("reel_repost_button_${reel.id}")
                ) {
                    Icon(
                        imageVector = Icons.Default.Repeat,
                        contentDescription = if (reel.isReposted) "Undo Repost" else "Repost reel",
                        tint = if (reel.isReposted) Color(0xFF00BA7C) else Color.White,
                        modifier = Modifier.size(28.dp)
                    )
                }
                Text(
                    text = if (reel.repostsCount > 0) "${"%,d".format(reel.repostsCount)}" else "Repost",
                    color = if (reel.isReposted) Color(0xFF00BA7C) else Color.White,
                    fontSize = 11.5.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            // Add to Playlist
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                IconButton(
                    onClick = onAddToPlaylist,
                    modifier = Modifier
                        .size(44.dp)
                        .testTag("reel_playlist_button_${reel.id}")
                ) {
                    Icon(
                        imageVector = Icons.Outlined.QueueMusic,
                        contentDescription = "Add reel to playlist",
                        tint = Color.White,
                        modifier = Modifier.size(28.dp)
                    )
                }
                Text(
                    text = "Save",
                    color = Color.White,
                    fontSize = 11.5.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            // Spinning Audio Disk
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .rotate(angle)
                    .clip(CircleShape)
                    .background(Color(0xFF262626))
                    .border(2.dp, Color.White.copy(alpha = 0.8f), CircleShape)
                    .clickable(onClick = onAudioClick)
                    .testTag("reel_audio_disk_btn"),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = reel.user.avatarRes),
                    contentDescription = "Audio track",
                    modifier = Modifier
                        .size(20.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
            }
        }

        // Bottom Left Creator Info & Audio
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(start = 16.dp, end = 80.dp, bottom = 28.dp)
        ) {
            // Trial Reel Badge (if test mode reel)
            if (reel.isTrialReel) {
                Surface(
                    onClick = onOpenTrialInsights,
                    shape = RoundedCornerShape(16.dp),
                    color = Color(0xFF6200EA).copy(alpha = 0.85f),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.7f)),
                    modifier = Modifier.testTag("reel_trial_badge_${reel.id}")
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "🧪 Trial Reel",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.5.sp
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "• ${reel.trialNonFollowerPercentage}% Non-followers",
                            color = Color.White.copy(alpha = 0.85f),
                            fontSize = 11.sp
                        )
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
            }

            // Creator Row
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                StoryAvatar(
                    avatarRes = reel.user.avatarRes,
                    size = 38.dp,
                    hasStory = true,
                    onClick = onUserClick
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = reel.user.username,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
                if (reel.user.isVerified) {
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(
                        imageVector = Icons.Filled.CheckCircle,
                        contentDescription = "Verified",
                        tint = IgPrimary,
                        modifier = Modifier.size(13.dp)
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                // Follow / Following button
                Surface(
                    onClick = { isFollowing = !isFollowing },
                    shape = RoundedCornerShape(8.dp),
                    color = if (isFollowing) Color.White.copy(alpha = 0.2f) else Color.Transparent,
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color.White)
                ) {
                    Text(
                        text = if (isFollowing) "Following" else "Follow",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Caption
            Text(
                text = reel.caption,
                color = Color.White,
                fontSize = 13.5.sp,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Audio Bar
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .clickable(onClick = onAudioClick)
                    .padding(vertical = 2.dp, horizontal = 4.dp)
                    .testTag("reel_audio_bar_btn")
            ) {
                Icon(
                    imageVector = Icons.Default.MusicNote,
                    contentDescription = "Audio track",
                    tint = Color.White,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = reel.audioTrack,
                    color = Color.White.copy(alpha = 0.9f),
                    fontSize = 12.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            // Watch Up Next Ticker Preview
            if (nextReel != null) {
                Spacer(modifier = Modifier.height(8.dp))
                Surface(
                    onClick = onOpenWatchUpNext,
                    shape = RoundedCornerShape(12.dp),
                    color = Color.Black.copy(alpha = 0.5f),
                    border = androidx.compose.foundation.BorderStroke(0.5.dp, Color.White.copy(alpha = 0.35f)),
                    modifier = Modifier.testTag("reel_up_next_ticker")
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.PlayArrow,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(12.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Up Next: @${nextReel.user.username}",
                            color = Color.White.copy(alpha = 0.9f),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "• View Queue",
                            color = Color(0xFF3897F0),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WatchUpNextSheet(
    currentReel: ReelItem,
    reels: List<ReelItem>,
    currentIndex: Int,
    autoplay: Boolean,
    onAutoplayChange: (Boolean) -> Unit,
    onSelectReel: (Int) -> Unit,
    onDismiss: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        containerColor = MaterialTheme.colorScheme.surface,
        modifier = Modifier.testTag("watch_up_next_bottom_sheet")
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
                        imageVector = Icons.Default.QueueMusic,
                        contentDescription = null,
                        tint = IgPrimary,
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Watch Up Next",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Autoplay Next",
                        style = MaterialTheme.typography.labelMedium.copy(fontSize = 12.sp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Switch(
                        checked = autoplay,
                        onCheckedChange = onAutoplayChange,
                        modifier = Modifier.testTag("autoplay_up_next_switch")
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Currently Playing Card
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                border = androidx.compose.foundation.BorderStroke(1.dp, IgPrimary.copy(alpha = 0.4f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = currentReel.thumbnailRes),
                        contentDescription = null,
                        modifier = Modifier
                            .size(54.dp)
                            .clip(RoundedCornerShape(8.dp)),
                        contentScale = ContentScale.Crop
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Surface(
                                shape = RoundedCornerShape(4.dp),
                                color = IgPrimary
                            ) {
                                Text(
                                    text = "NOW PLAYING",
                                    color = Color.White,
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                                )
                            }
                            if (currentReel.isTrialReel) {
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(text = "🧪 Trial Reel", fontSize = 10.sp, color = IgPrimary, fontWeight = FontWeight.Bold)
                            }
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = currentReel.caption,
                            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = "@${currentReel.user.username} • ${currentReel.audioTrack}",
                            style = MaterialTheme.typography.labelSmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Queue Section Header
            Text(
                text = "Up Next in Queue (${reels.size - 1} videos)",
                style = MaterialTheme.typography.titleSmall.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Queue List
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 380.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                itemsIndexed(reels) { index, item ->
                    if (index != currentIndex) {
                        Surface(
                            onClick = { onSelectReel(index) },
                            shape = RoundedCornerShape(10.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("up_next_item_$index")
                        ) {
                            Row(
                                modifier = Modifier.padding(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(50.dp, 66.dp)
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(Color.Black),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Image(
                                        painter = painterResource(id = item.thumbnailRes),
                                        contentDescription = null,
                                        modifier = Modifier.fillMaxSize(),
                                        contentScale = ContentScale.Crop
                                    )
                                    Surface(
                                        shape = CircleShape,
                                        color = Color.Black.copy(alpha = 0.6f),
                                        modifier = Modifier.size(24.dp)
                                    ) {
                                        Box(contentAlignment = Alignment.Center) {
                                            Icon(
                                                imageVector = Icons.Default.PlayArrow,
                                                contentDescription = "Play",
                                                tint = Color.White,
                                                modifier = Modifier.size(16.dp)
                                            )
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.width(12.dp))

                                Column(modifier = Modifier.weight(1f)) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(
                                            text = "@${item.user.username}",
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 13.sp
                                        )
                                        if (item.isTrialReel) {
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text(text = "🧪 Trial", fontSize = 10.sp, color = IgPrimary, fontWeight = FontWeight.Bold)
                                        }
                                    }
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = item.caption,
                                        fontSize = 12.sp,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = "🎵 ${item.audioTrack}",
                                        fontSize = 11.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }

                                Spacer(modifier = Modifier.width(8.dp))

                                Button(
                                    onClick = { onSelectReel(index) },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = IgPrimary,
                                        contentColor = Color.White
                                    ),
                                    shape = RoundedCornerShape(16.dp),
                                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                                    modifier = Modifier.height(32.dp)
                                ) {
                                    Text("Play", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TrialReelInsightsDialog(
    reel: ReelItem,
    onGraduate: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(
                    shape = CircleShape,
                    color = Color(0xFF6200EA),
                    modifier = Modifier.size(36.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(text = "🧪", fontSize = 18.sp)
                    }
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = "Trial Reel Insights",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    Text(
                        text = "Active in test mode",
                        style = MaterialTheme.typography.labelSmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                    )
                }
            }
        },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "This reel was shared with non-followers first to test performance across the Explore algorithm.",
                    style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Performance Metrics Grid
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text("Trial Views", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                Text("${"%,d".format(reel.trialViewsCount.coerceAtLeast(reel.likesCount * 3))}", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                            }
                            Column {
                                Text("Non-Followers", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                Text("${reel.trialNonFollowerPercentage}%", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = IgPrimary)
                            }
                            Column {
                                Text("Time Left", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                Text("${reel.trialDurationHoursLeft}h", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))
                        HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.15f))
                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text("Completion Rate", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                Text("76.8%", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            }
                            Column {
                                Text("Shares / Saves", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                Text("${reel.sharesCount + 42}", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = "Ready to share with your regular audience? Graduate this reel now to add it to your main profile grid and notify your followers.",
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                )
            }
        },
        confirmButton = {
            Button(
                onClick = onGraduate,
                colors = ButtonDefaults.buttonColors(
                    containerColor = IgPrimary,
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier.testTag("graduate_trial_reel_btn")
            ) {
                Text("Graduate to Profile & Followers", fontWeight = FontWeight.Bold, fontSize = 13.sp)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Keep Testing")
            }
        }
    )
}
