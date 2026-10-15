package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material.icons.outlined.PersonPin
import androidx.compose.material.icons.outlined.QueueMusic
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
import com.example.data.model.Playlist
import com.example.data.model.PlaylistItemType
import com.example.data.model.Post
import com.example.data.model.User
import com.example.ui.components.StoryAvatar
import com.example.ui.theme.IgPrimary
import com.example.ui.theme.IgRedHeart

enum class ProfileGridTab {
    POSTS,
    PLAYLISTS,
    SAVED,
    TAGGED
}

@Composable
fun ProfileScreen(
    user: User,
    posts: List<Post>,
    playlists: List<Playlist>,
    onEditProfileClick: () -> Unit,
    onPostClick: (String) -> Unit,
    onPlaylistClick: (String) -> Unit,
    onCreatePlaylistClick: () -> Unit,
    onSettingsClick: () -> Unit,
    onSwitchAccountClick: () -> Unit,
    onCreatePostClick: () -> Unit = {},
    onCreateReelClick: () -> Unit = {},
    onAccountStatusClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    var selectedGridTab by remember { mutableStateOf(ProfileGridTab.POSTS) }
    var playlistPrivacyFilter by remember { mutableStateOf("All") } // "All", "Public", "Private"
    var showEmptyStatePreview by remember { mutableStateOf(false) }

    val userPosts = posts.filter { it.user.id == user.id || it.user.username == user.username }
    val savedPosts = posts.filter { it.isSaved }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("profile_screen")
    ) {
        // Channel Terminated (3/3 Copyright Strike • Content Removed) Warning Banner
        if (user.isChannelTerminated) {
            item {
                Surface(
                    onClick = onAccountStatusClick,
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.5f),
                    border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.error),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 6.dp)
                        .testTag("profile_account_status_warning_banner")
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier.size(36.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.Gavel,
                                    contentDescription = "Channel Terminated",
                                    tint = Color.White,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "Channel Terminated",
                                    fontWeight = FontWeight.ExtraBold,
                                    color = MaterialTheme.colorScheme.error,
                                    fontSize = 13.5.sp
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Surface(
                                    shape = RoundedCornerShape(4.dp),
                                    color = MaterialTheme.colorScheme.error
                                ) {
                                    Text(
                                        text = "Content Removed",
                                        color = Color.White,
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                                    )
                                }
                            }
                            Text(
                                text = "3 to 3 Copyright Strike • Content Removed • Tap to view Account Status",
                                fontSize = 11.5.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                            contentDescription = "Details",
                            tint = MaterialTheme.colorScheme.error,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }

        // Top Profile Bar
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clickable(onClick = onSwitchAccountClick)
                        .testTag("profile_username_dropdown")
                ) {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = "Private/Public account",
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = user.username,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 19.sp
                        )
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = "Switch Account",
                        modifier = Modifier.size(24.dp)
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = onCreatePlaylistClick) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Create",
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    IconButton(
                        onClick = onSettingsClick,
                        modifier = Modifier.testTag("profile_settings_menu_btn")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = "Settings and activity",
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }
        }

        // Profile Info Header: Avatar & Stats
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                StoryAvatar(
                    avatarRes = user.avatarRes,
                    size = 84.dp,
                    hasStory = true,
                    onClick = onEditProfileClick
                )

                Row(
                    modifier = Modifier.weight(1f),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    ProfileStatItem(count = "${userPosts.size}", label = "Posts")
                    ProfileStatItem(count = "${playlists.size}", label = "Playlists")
                    ProfileStatItem(count = "${"%,d".format(user.followersCount)}", label = "Followers")
                }
            }
        }

        // Bio & Links Section
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp)
            ) {
                Text(
                    text = user.fullName,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                )

                if (user.bio.isNotBlank()) {
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = user.bio,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontSize = 13.5.sp,
                            lineHeight = 18.sp
                        )
                    )
                }

                if (user.website.isNotBlank()) {
                    Spacer(modifier = Modifier.height(3.dp))
                    Text(
                        text = "🔗 ${user.website}",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = IgPrimary,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 13.sp
                        )
                    )
                }
            }
        }

        // Action Buttons: Edit Profile & Share Profile
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = onEditProfileClick,
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant,
                        contentColor = MaterialTheme.colorScheme.onSurface
                    ),
                    modifier = Modifier
                        .weight(1f)
                        .height(36.dp)
                        .testTag("edit_profile_button"),
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Text(
                        text = "Edit profile",
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold)
                    )
                }

                Button(
                    onClick = onCreatePlaylistClick,
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant,
                        contentColor = MaterialTheme.colorScheme.onSurface
                    ),
                    modifier = Modifier
                        .weight(1f)
                        .height(36.dp)
                        .testTag("create_playlist_button"),
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.QueueMusic,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "New Playlist",
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold)
                        )
                    }
                }
            }
        }

        // Story Highlights Carousel
        item {
            val highlights = listOf(
                HighlightItem("Amalfi 🌊", R.drawable.img_post_travel),
                HighlightItem("Paris ☕", R.drawable.img_post_coffee),
                HighlightItem("Tokyo 🌃", R.drawable.img_post_city),
                HighlightItem("Me ✨", R.drawable.img_user_avatar)
            )

            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                items(highlights) { hl ->
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.width(66.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(62.dp)
                                .border(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f), CircleShape)
                                .padding(3.dp)
                        ) {
                            Image(
                                painter = painterResource(id = hl.imageRes),
                                contentDescription = hl.title,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clip(CircleShape),
                                contentScale = ContentScale.Crop
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = hl.title,
                            style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp),
                            maxLines = 1
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
        }

        // Profile Grid Tabs (Posts, Playlists, Saved, Tagged)
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(0.5.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                TabButton(
                    selected = selectedGridTab == ProfileGridTab.POSTS,
                    selectedIcon = Icons.Filled.GridOn,
                    unselectedIcon = Icons.Outlined.GridOn,
                    onClick = { selectedGridTab = ProfileGridTab.POSTS }
                )
                TabButton(
                    selected = selectedGridTab == ProfileGridTab.PLAYLISTS,
                    selectedIcon = Icons.Filled.QueueMusic,
                    unselectedIcon = Icons.Outlined.QueueMusic,
                    onClick = { selectedGridTab = ProfileGridTab.PLAYLISTS }
                )
                TabButton(
                    selected = selectedGridTab == ProfileGridTab.SAVED,
                    selectedIcon = Icons.Filled.Bookmark,
                    unselectedIcon = Icons.Outlined.BookmarkBorder,
                    onClick = { selectedGridTab = ProfileGridTab.SAVED }
                )
                TabButton(
                    selected = selectedGridTab == ProfileGridTab.TAGGED,
                    selectedIcon = Icons.Filled.PersonPin,
                    unselectedIcon = Icons.Outlined.PersonPin,
                    onClick = { selectedGridTab = ProfileGridTab.TAGGED }
                )
            }
        }

        // Tab Content: Playlists Tab vs Posts/Saved/Tagged
        if (selectedGridTab == ProfileGridTab.PLAYLISTS) {
            // Playlists Filter Chips (All, Public, Private)
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    FilterChip(
                        selected = playlistPrivacyFilter == "All",
                        onClick = { playlistPrivacyFilter = "All" },
                        label = { Text("All (${playlists.size})") }
                    )
                    FilterChip(
                        selected = playlistPrivacyFilter == "Public",
                        onClick = { playlistPrivacyFilter = "Public" },
                        leadingIcon = {
                            Icon(Icons.Default.Public, contentDescription = null, modifier = Modifier.size(14.dp))
                        },
                        label = { Text("Public (${playlists.count { !it.isPrivate }})") }
                    )
                    FilterChip(
                        selected = playlistPrivacyFilter == "Private",
                        onClick = { playlistPrivacyFilter = "Private" },
                        leadingIcon = {
                            Icon(Icons.Default.Lock, contentDescription = null, modifier = Modifier.size(14.dp))
                        },
                        label = { Text("Private (${playlists.count { it.isPrivate }})") }
                    )
                }
            }

            val filteredPlaylists = when (playlistPrivacyFilter) {
                "Public" -> playlists.filter { !it.isPrivate }
                "Private" -> playlists.filter { it.isPrivate }
                else -> playlists
            }

            items(filteredPlaylists, key = { it.id }) { pl ->
                PlaylistCard(
                    playlist = pl,
                    onClick = { onPlaylistClick(pl.id) }
                )
                Spacer(modifier = Modifier.height(10.dp))
            }
        } else {
            // Grid Content for Posts / Saved / Tagged
            item {
                val displayList = when (selectedGridTab) {
                    ProfileGridTab.POSTS -> if (showEmptyStatePreview) emptyList() else userPosts
                    ProfileGridTab.SAVED -> savedPosts
                    ProfileGridTab.TAGGED -> emptyList()
                    else -> userPosts
                }

                // Empty state preview toggle chip (allows testing No post yet anytime)
                if (selectedGridTab == ProfileGridTab.POSTS) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 14.dp, vertical = 4.dp),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            onClick = { showEmptyStatePreview = !showEmptyStatePreview },
                            shape = RoundedCornerShape(12.dp),
                            color = if (showEmptyStatePreview) IgPrimary.copy(alpha = 0.15f) else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                            border = androidx.compose.foundation.BorderStroke(
                                1.dp,
                                if (showEmptyStatePreview) IgPrimary else MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)
                            ),
                            modifier = Modifier.testTag("toggle_empty_state_preview_btn")
                        ) {
                            Text(
                                text = if (showEmptyStatePreview) "Showing: No Post Yet (Preview)" else "Test No Post Yet",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = if (showEmptyStatePreview) IgPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }
                }

                if (displayList.isEmpty()) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 44.dp, horizontal = 24.dp)
                            .testTag("profile_no_posts_yet_container"),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .size(76.dp)
                                .border(2.dp, MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.CameraAlt,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.size(36.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "No posts yet",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp
                            )
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "When you share photos and videos, they will appear on your profile.",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                fontSize = 13.5.sp,
                                lineHeight = 18.sp
                            ),
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(20.dp))
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Button(
                                onClick = onCreatePostClick,
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = IgPrimary,
                                    contentColor = Color.White
                                ),
                                shape = RoundedCornerShape(20.dp),
                                modifier = Modifier.testTag("profile_share_first_photo_btn")
                            ) {
                                Icon(Icons.Default.AddPhotoAlternate, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Share first photo", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            }

                            OutlinedButton(
                                onClick = onCreateReelClick,
                                shape = RoundedCornerShape(20.dp),
                                modifier = Modifier.testTag("profile_share_first_reel_btn")
                            ) {
                                Text("Upload Reel", fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                            }
                        }
                    }
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 300.dp, max = 800.dp)
                    ) {
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(3),
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(1.dp),
                            horizontalArrangement = Arrangement.spacedBy(1.5.dp),
                            verticalArrangement = Arrangement.spacedBy(1.5.dp)
                        ) {
                            items(displayList) { post ->
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .aspectRatio(1f)
                                        .clickable { onPostClick(post.id) }
                                ) {
                                    Image(
                                        painter = painterResource(id = post.imageRes),
                                        contentDescription = "Post photo",
                                        modifier = Modifier.fillMaxSize(),
                                        contentScale = ContentScale.Crop
                                    )
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
fun PlaylistCard(
    playlist: Playlist,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.surfaceVariant,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 14.dp)
            .testTag("playlist_card_${playlist.id}")
    ) {
        Row(
            modifier = Modifier.padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Cover Image with Gradient
            Box(
                modifier = Modifier
                    .size(76.dp)
                    .clip(RoundedCornerShape(8.dp))
            ) {
                Image(
                    painter = painterResource(id = playlist.coverRes),
                    contentDescription = playlist.title,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.6f))
                            )
                        )
                )
                // Items count overlay
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(4.dp)
                        .background(Color.Black.copy(alpha = 0.7f), RoundedCornerShape(4.dp))
                        .padding(horizontal = 4.dp, vertical = 1.dp)
                ) {
                    Text(
                        text = "${playlist.items.size}",
                        color = Color.White,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = playlist.title,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                if (playlist.description.isNotBlank()) {
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = playlist.description,
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontSize = 12.sp
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                val postsCount = playlist.items.count { it.type == PlaylistItemType.POST }
                val reelsCount = playlist.items.count { it.type == PlaylistItemType.REEL }
                val storiesCount = playlist.items.count { it.type == PlaylistItemType.STORY }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Privacy badge chip
                    Surface(
                        shape = RoundedCornerShape(4.dp),
                        color = if (playlist.isPrivate) IgRedHeart.copy(alpha = 0.15f) else IgPrimary.copy(alpha = 0.15f)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = if (playlist.isPrivate) Icons.Default.Lock else Icons.Default.Public,
                                contentDescription = null,
                                tint = if (playlist.isPrivate) IgRedHeart else IgPrimary,
                                modifier = Modifier.size(10.dp)
                            )
                            Spacer(modifier = Modifier.width(3.dp))
                            Text(
                                text = if (playlist.isPrivate) "Private" else "Public",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (playlist.isPrivate) IgRedHeart else IgPrimary
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = "${postsCount}p • ${reelsCount}r • ${storiesCount}s",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = "View playlist",
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@Composable
fun ProfileStatItem(count: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = count,
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 17.sp
            )
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall.copy(
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 12.sp
            )
        )
    }
}

@Composable
fun RowScope.TabButton(
    selected: Boolean,
    selectedIcon: androidx.compose.ui.graphics.vector.ImageVector,
    unselectedIcon: androidx.compose.ui.graphics.vector.ImageVector,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .weight(1f)
            .clickable(onClick = onClick)
            .padding(vertical = 10.dp)
            .then(
                if (selected) {
                    Modifier.border(
                        androidx.compose.foundation.BorderStroke(1.5.dp, MaterialTheme.colorScheme.onSurface)
                    )
                } else Modifier
            ),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = if (selected) selectedIcon else unselectedIcon,
            contentDescription = null,
            tint = if (selected) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(24.dp)
        )
    }
}

data class HighlightItem(val title: String, val imageRes: Int)
