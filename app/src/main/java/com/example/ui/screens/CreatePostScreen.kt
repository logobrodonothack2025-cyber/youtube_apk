package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.User
import com.example.ui.components.getFilterColorFilter
import com.example.ui.theme.IgPrimary
import com.example.ui.theme.IgRedHeart
import com.example.ui.viewmodel.CreateMode

@Composable
fun CreatePostScreen(
    currentUser: User,
    createMode: CreateMode,
    onModeChange: (CreateMode) -> Unit,
    // Photo post draft
    draftImageRes: Int,
    onDraftImageChange: (Int) -> Unit,
    draftCaption: String,
    onDraftCaptionChange: (String) -> Unit,
    draftLocation: String,
    onDraftLocationChange: (String) -> Unit,
    draftFilter: String,
    onDraftFilterChange: (String) -> Unit,
    onPublish: () -> Unit,
    // Video / Reel upload draft
    videoTitle: String,
    onVideoTitleChange: (String) -> Unit,
    videoDescription: String,
    onVideoDescriptionChange: (String) -> Unit,
    videoAudioTrack: String,
    onVideoAudioTrackChange: (String) -> Unit,
    videoThumbnailRes: Int,
    onVideoThumbnailChange: (Int) -> Unit,
    isTrialReel: Boolean = false,
    onIsTrialReelChange: (Boolean) -> Unit = {},
    onPublishVideo: () -> Unit,
    onCancel: () -> Unit,
    modifier: Modifier = Modifier
) {
    val sampleImages = listOf(
        R.drawable.img_post_travel,
        R.drawable.img_post_coffee,
        R.drawable.img_post_city,
        R.drawable.img_user_avatar
    )

    val filters = listOf(
        "Normal",
        "Clarendon",
        "Juno",
        "Valencia",
        "Vintage",
        "Noir"
    )

    val quickLocations = listOf(
        "Positano, Italy",
        "Tokyo, Japan",
        "Paris, France",
        "Brooklyn, New York",
        "Milan, Italy"
    )

    val audioTracks = listOf(
        "Bruno Major • Nothing",
        "Trending Lo-Fi • Chill Session",
        "Amalfi Coast Summer • Coastal Chill",
        "Night Vibes • Tokyo Cyberpunk",
        "Acoustic Morning • Cozy Cafe",
        "${currentUser.username} • Original Audio"
    )

    var isVideoPlaying by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .testTag("create_post_screen")
    ) {
        // Top Bar: Cancel, Title, Share Button
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(
                onClick = onCancel,
                modifier = Modifier.testTag("create_cancel_button")
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Cancel post",
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }

            Text(
                text = if (createMode == CreateMode.PHOTO_POST) "New Post" else "Upload Video / Reel",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            )

            Button(
                onClick = {
                    if (createMode == CreateMode.PHOTO_POST) {
                        onPublish()
                    } else {
                        onPublishVideo()
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = IgPrimary,
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(20.dp),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 6.dp),
                modifier = Modifier.testTag("publish_post_button")
            ) {
                Text(
                    text = "Share",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }
        }

        // Mode Switcher: Photo Post vs Video Upload
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 6.dp),
            shape = RoundedCornerShape(10.dp),
            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(3.dp)
            ) {
                // Photo tab
                Surface(
                    onClick = { onModeChange(CreateMode.PHOTO_POST) },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(8.dp),
                    color = if (createMode == CreateMode.PHOTO_POST) MaterialTheme.colorScheme.surface else Color.Transparent,
                    shadowElevation = if (createMode == CreateMode.PHOTO_POST) 2.dp else 0.dp
                ) {
                    Row(
                        modifier = Modifier.padding(vertical = 8.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Photo,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp),
                            tint = if (createMode == CreateMode.PHOTO_POST) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Photo Post",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = if (createMode == CreateMode.PHOTO_POST) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                // Video tab
                Surface(
                    onClick = { onModeChange(CreateMode.VIDEO_REEL) },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(8.dp),
                    color = if (createMode == CreateMode.VIDEO_REEL) MaterialTheme.colorScheme.surface else Color.Transparent,
                    shadowElevation = if (createMode == CreateMode.VIDEO_REEL) 2.dp else 0.dp
                ) {
                    Row(
                        modifier = Modifier.padding(vertical = 8.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Videocam,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp),
                            tint = if (createMode == CreateMode.VIDEO_REEL) IgPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Upload Video",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = if (createMode == CreateMode.VIDEO_REEL) IgPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // CONTENT BASED ON MODE
        if (createMode == CreateMode.PHOTO_POST) {
            // PHOTO POST BUILDER
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .padding(horizontal = 14.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.Black),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = draftImageRes),
                    contentDescription = "Post preview",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop,
                    colorFilter = getFilterColorFilter(draftFilter)
                )

                // Current Filter Tag Badge
                Surface(
                    color = Color.Black.copy(alpha = 0.6f),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(12.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(13.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = draftFilter,
                            color = Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Choose Photo from Gallery Bar
            Column(modifier = Modifier.padding(horizontal = 14.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(bottom = 8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.PhotoLibrary,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Select Photo from Gallery",
                        style = MaterialTheme.typography.labelLarge.copy(
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                }

                LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    items(sampleImages) { resId ->
                        val isSelected = resId == draftImageRes
                        Box(
                            modifier = Modifier
                                .size(72.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .clickable { onDraftImageChange(resId) }
                                .then(
                                    if (isSelected) {
                                        Modifier.border(2.5.dp, IgPrimary, RoundedCornerShape(8.dp))
                                    } else Modifier
                                )
                        ) {
                            Image(
                                painter = painterResource(id = resId),
                                contentDescription = "Gallery item",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Filters Strip
            Column(modifier = Modifier.padding(horizontal = 14.dp)) {
                Text(
                    text = "Apply Instagram Filter",
                    style = MaterialTheme.typography.labelLarge.copy(
                        fontWeight = FontWeight.SemiBold
                    ),
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(filters) { filterName ->
                        val isSelected = filterName == draftFilter
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .clickable { onDraftFilterChange(filterName) }
                                .padding(bottom = 4.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(64.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .then(
                                        if (isSelected) {
                                            Modifier.border(2.dp, IgPrimary, RoundedCornerShape(8.dp))
                                        } else Modifier
                                    )
                            ) {
                                Image(
                                    painter = painterResource(id = draftImageRes),
                                    contentDescription = "$filterName filter",
                                    modifier = Modifier.fillMaxSize(),
                                    contentScale = ContentScale.Crop,
                                    colorFilter = getFilterColorFilter(filterName)
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = filterName,
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    color = if (isSelected) IgPrimary else MaterialTheme.colorScheme.onSurface
                                )
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Caption Input Field
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp),
                verticalAlignment = Alignment.Top
            ) {
                Image(
                    painter = painterResource(id = currentUser.avatarRes),
                    contentDescription = "Avatar",
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.width(12.dp))
                OutlinedTextField(
                    value = draftCaption,
                    onValueChange = onDraftCaptionChange,
                    placeholder = {
                        Text(
                            "Write a caption, tell the story, add hashtags #photography #travel...",
                            fontSize = 13.5.sp
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("caption_input_field"),
                    minLines = 3,
                    maxLines = 5,
                    shape = RoundedCornerShape(12.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Location Tagging Section
            Column(modifier = Modifier.padding(horizontal = 14.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(bottom = 6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.AddLocation,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Add Location",
                        style = MaterialTheme.typography.labelLarge.copy(
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                }

                OutlinedTextField(
                    value = draftLocation,
                    onValueChange = onDraftLocationChange,
                    placeholder = { Text("Where was this photo taken?", fontSize = 13.sp) },
                    singleLine = true,
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("location_input_field")
                )

                Spacer(modifier = Modifier.height(8.dp))

                LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    items(quickLocations) { loc ->
                        FilterChip(
                            selected = draftLocation == loc,
                            onClick = {
                                onDraftLocationChange(if (draftLocation == loc) "" else loc)
                            },
                            label = { Text(loc, fontSize = 11.5.sp) }
                        )
                    }
                }
            }
        } else {
            // VIDEO / REEL UPLOAD BUILDER
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp)
            ) {
                // Video Preview Player Box
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(280.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.Black)
                        .clickable { isVideoPlaying = !isVideoPlaying },
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = videoThumbnailRes),
                        contentDescription = "Video preview",
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
                                        Color.Black.copy(alpha = 0.4f),
                                        Color.Transparent,
                                        Color.Black.copy(alpha = 0.7f)
                                    )
                                )
                            )
                    )

                    // Play/Pause icon button
                    Surface(
                        shape = CircleShape,
                        color = Color.Black.copy(alpha = 0.6f),
                        modifier = Modifier.size(56.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = if (isVideoPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                                contentDescription = if (isVideoPlaying) "Pause" else "Play",
                                tint = Color.White,
                                modifier = Modifier.size(34.dp)
                            )
                        }
                    }

                    // Duration Tag
                    Surface(
                        color = Color.Black.copy(alpha = 0.75f),
                        shape = RoundedCornerShape(4.dp),
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(10.dp)
                    ) {
                        Text(
                            text = if (isVideoPlaying) "0:14 / 0:30" else "0:30 HD",
                            color = Color.White,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }

                    // Audio track indicator chip on top left
                    Surface(
                        color = Color.Black.copy(alpha = 0.6f),
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(10.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.MusicNote,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(12.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = videoAudioTrack.take(25),
                                color = Color.White,
                                fontSize = 11.sp
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Select Video Footage from Clips
                Text(
                    text = "Select Video Footage",
                    style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.SemiBold),
                    modifier = Modifier.padding(bottom = 6.dp)
                )

                LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    items(sampleImages) { resId ->
                        val isSelected = resId == videoThumbnailRes
                        Box(
                            modifier = Modifier
                                .size(72.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .clickable { onVideoThumbnailChange(resId) }
                                .then(
                                    if (isSelected) {
                                        Modifier.border(2.5.dp, IgPrimary, RoundedCornerShape(8.dp))
                                    } else Modifier
                                )
                        ) {
                            Image(
                                painter = painterResource(id = resId),
                                contentDescription = "Video clip",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                            Icon(
                                imageVector = Icons.Default.Videocam,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier
                                    .align(Alignment.BottomEnd)
                                    .padding(4.dp)
                                    .size(14.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                // VIDEO TITLE INPUT
                OutlinedTextField(
                    value = videoTitle,
                    onValueChange = onVideoTitleChange,
                    label = { Text("Video Title") },
                    placeholder = { Text("e.g. Shibuya Night Walk, Espresso Art Pour...") },
                    leadingIcon = {
                        Icon(Icons.Default.Title, contentDescription = null, tint = IgPrimary)
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("video_title_input_field")
                )

                Spacer(modifier = Modifier.height(14.dp))

                // VIDEO DESCRIPTION INPUT
                OutlinedTextField(
                    value = videoDescription,
                    onValueChange = onVideoDescriptionChange,
                    label = { Text("Video Description & Hashtags") },
                    placeholder = { Text("Tell viewers about this reel, tips, camera gear #reels #viral...") },
                    minLines = 3,
                    maxLines = 5,
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("video_description_input_field")
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Audio Track Selection
                Text(
                    text = "Audio & Music",
                    style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.SemiBold),
                    modifier = Modifier.padding(bottom = 6.dp)
                )

                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(audioTracks) { track ->
                        FilterChip(
                            selected = videoAudioTrack == track,
                            onClick = { onVideoAudioTrackChange(track) },
                            leadingIcon = {
                                Icon(Icons.Default.MusicNote, contentDescription = null, modifier = Modifier.size(14.dp))
                            },
                            label = { Text(track, fontSize = 11.5.sp) }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // TRIAL REEL (Test with non-followers)
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = if (isTrialReel) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.25f) else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                    border = androidx.compose.foundation.BorderStroke(
                        width = 1.dp,
                        color = if (isTrialReel) IgPrimary else MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)
                    ),
                    modifier = Modifier.fillMaxWidth().testTag("trial_reel_settings_card")
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.weight(1f)
                            ) {
                                Surface(
                                    shape = CircleShape,
                                    color = if (isTrialReel) IgPrimary else MaterialTheme.colorScheme.surfaceVariant,
                                    modifier = Modifier.size(36.dp)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Text(text = "🧪", fontSize = 18.sp)
                                    }
                                }
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(
                                            text = "Trial Reel",
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 15.sp,
                                            color = MaterialTheme.colorScheme.onSurface
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Surface(
                                            color = IgPrimary,
                                            shape = RoundedCornerShape(4.dp)
                                        ) {
                                            Text(
                                                text = "NEW",
                                                color = Color.White,
                                                fontSize = 9.sp,
                                                fontWeight = FontWeight.ExtraBold,
                                                modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                                            )
                                        }
                                    }
                                    Text(
                                        text = "Test with non-followers first",
                                        fontSize = 12.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }

                            Switch(
                                checked = isTrialReel,
                                onCheckedChange = onIsTrialReelChange,
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = Color.White,
                                    checkedTrackColor = IgPrimary
                                ),
                                modifier = Modifier.testTag("trial_reel_toggle_switch")
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = "Share this reel with non-followers first to evaluate reach, retention, and engagement. You can graduate it to your followers and profile grid anytime.",
                            fontSize = 12.5.sp,
                            lineHeight = 17.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        if (isTrialReel) {
                            Spacer(modifier = Modifier.height(12.dp))
                            HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.15f))
                            Spacer(modifier = Modifier.height(10.dp))

                            Text(
                                text = "Trial Duration & Audience",
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Spacer(modifier = Modifier.height(6.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = IgPrimary.copy(alpha = 0.15f),
                                    border = androidx.compose.foundation.BorderStroke(1.dp, IgPrimary),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Column(
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Text("48 Hours", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = IgPrimary)
                                        Text("Recommended", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    }
                                }
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = MaterialTheme.colorScheme.surfaceVariant,
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Column(
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Text("24 Hours", fontWeight = FontWeight.SemiBold, fontSize = 12.sp)
                                        Text("Quick test", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    }
                                }
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = MaterialTheme.colorScheme.surfaceVariant,
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Column(
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Text("72 Hours", fontWeight = FontWeight.SemiBold, fontSize = 12.sp)
                                        Text("Extended", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(36.dp))
    }
}
