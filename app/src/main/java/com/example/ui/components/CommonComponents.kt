package com.example.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.*
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.automirrored.outlined.Send
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.theme.*
import com.example.ui.viewmodel.MainTab

@Composable
fun StoryAvatar(
    @DrawableRes avatarRes: Int,
    size: Dp = 68.dp,
    hasStory: Boolean = true,
    isViewed: Boolean = false,
    isCloseFriend: Boolean = false,
    isSelfAdd: Boolean = false,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .size(size)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            )
            .testTag("story_avatar")
    ) {
        val borderWidth = if (size > 60.dp) 2.5.dp else 2.dp
        val padding = if (hasStory) borderWidth + 2.dp else 0.dp

        val brush = when {
            !hasStory -> null
            isViewed -> IgViewedStoryBrush
            isCloseFriend -> IgCloseFriendsBrush
            else -> IgStoryBrush
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .then(
                    if (brush != null) {
                        Modifier.border(borderWidth, brush, CircleShape)
                    } else {
                        Modifier
                    }
                )
                .padding(padding)
        ) {
            Image(
                painter = painterResource(id = avatarRes),
                contentDescription = "User Avatar",
                modifier = Modifier
                    .fillMaxSize()
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
        }

        if (isSelfAdd) {
            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .size(22.dp)
                    .background(MaterialTheme.colorScheme.surface, CircleShape)
                    .padding(2.dp)
                    .background(IgPrimary, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Story",
                    tint = Color.White,
                    modifier = Modifier.size(14.dp)
                )
            }
        }
    }
}

@Composable
fun DoubleTapHeartOverlay(
    isTriggered: Boolean,
    modifier: Modifier = Modifier
) {
    AnimatedVisibility(
        visible = isTriggered,
        enter = scaleIn(
            initialScale = 0.2f,
            animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessMedium)
        ) + fadeIn(),
        exit = scaleOut(
            targetScale = 1.3f,
            animationSpec = tween(durationMillis = 200)
        ) + fadeOut(animationSpec = tween(durationMillis = 200)),
        modifier = modifier
    ) {
        Icon(
            imageVector = Icons.Filled.Favorite,
            contentDescription = "Burst Heart",
            tint = Color.White.copy(alpha = 0.95f),
            modifier = Modifier
                .size(110.dp)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InstagramTopBar(
    unreadMessages: Int = 1,
    hasNewActivity: Boolean = true,
    onNotificationsClick: () -> Unit,
    onDirectMessagesClick: () -> Unit,
    onSubscriptionsClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    TopAppBar(
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(start = 4.dp)
            ) {
                Text(
                    text = "Instagram",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontFamily = FontFamily.Cursive,
                        fontWeight = FontWeight.Bold,
                        fontSize = 30.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    ),
                    modifier = Modifier.testTag("app_wordmark")
                )
            }
        },
        actions = {
            // Subscriptions Page button
            IconButton(
                onClick = onSubscriptionsClick,
                modifier = Modifier.testTag("top_bar_subscriptions_btn")
            ) {
                Icon(
                    imageVector = Icons.Default.Subscriptions,
                    contentDescription = "Subscriptions Page",
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }

            // Notifications / Heart button
            IconButton(
                onClick = onNotificationsClick,
                modifier = Modifier.testTag("notifications_button")
            ) {
                Box {
                    Icon(
                        imageVector = Icons.Outlined.FavoriteBorder,
                        contentDescription = "Activity Notifications",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                    if (hasNewActivity) {
                        Box(
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .size(8.dp)
                                .background(IgRedHeart, CircleShape)
                        )
                    }
                }
            }

            // Direct Messages button
            IconButton(
                onClick = onDirectMessagesClick,
                modifier = Modifier.testTag("direct_messages_button")
            ) {
                Box {
                    Icon(
                        imageVector = Icons.AutoMirrored.Outlined.Send,
                        contentDescription = "Direct Messages",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                    if (unreadMessages > 0) {
                        Box(
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .offset(x = 4.dp, y = (-2).dp)
                                .background(IgRedHeart, RoundedCornerShape(10.dp))
                                .padding(horizontal = 4.dp, vertical = 1.dp)
                        ) {
                            Text(
                                text = unreadMessages.toString(),
                                color = Color.White,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        modifier = modifier
    )
}

@Composable
fun InstagramBottomNav(
    currentTab: MainTab,
    @DrawableRes userAvatarRes: Int,
    onTabSelected: (MainTab) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 4.dp,
        border = androidx.compose.foundation.BorderStroke(0.5.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)),
        modifier = modifier
            .fillMaxWidth()
            .windowInsetsPadding(WindowInsets.navigationBars)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp)
                .padding(horizontal = 12.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Home
            IconButton(
                onClick = { onTabSelected(MainTab.HOME) },
                modifier = Modifier.testTag("nav_home")
            ) {
                Icon(
                    imageVector = if (currentTab == MainTab.HOME) Icons.Filled.Home else Icons.Outlined.Home,
                    contentDescription = "Home Feed",
                    tint = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.size(28.dp)
                )
            }

            // Search
            IconButton(
                onClick = { onTabSelected(MainTab.SEARCH) },
                modifier = Modifier.testTag("nav_search")
            ) {
                Icon(
                    imageVector = if (currentTab == MainTab.SEARCH) Icons.Filled.Search else Icons.Outlined.Search,
                    contentDescription = "Search & Explore",
                    tint = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.size(28.dp)
                )
            }

            // Create Post (+)
            IconButton(
                onClick = { onTabSelected(MainTab.CREATE) },
                modifier = Modifier.testTag("nav_create")
            ) {
                Icon(
                    imageVector = if (currentTab == MainTab.CREATE) Icons.Filled.AddBox else Icons.Outlined.AddBox,
                    contentDescription = "Create Post",
                    tint = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.size(28.dp)
                )
            }

            // Reels
            IconButton(
                onClick = { onTabSelected(MainTab.REELS) },
                modifier = Modifier.testTag("nav_reels")
            ) {
                Icon(
                    imageVector = if (currentTab == MainTab.REELS) Icons.Filled.Movie else Icons.Outlined.Movie,
                    contentDescription = "Reels",
                    tint = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.size(28.dp)
                )
            }

            // Profile
            Box(
                modifier = Modifier
                    .size(34.dp)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) { onTabSelected(MainTab.PROFILE) }
                    .then(
                        if (currentTab == MainTab.PROFILE) {
                            Modifier.border(2.dp, MaterialTheme.colorScheme.onSurface, CircleShape)
                        } else {
                            Modifier
                        }
                    )
                    .padding(if (currentTab == MainTab.PROFILE) 2.dp else 0.dp)
                    .testTag("nav_profile"),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = userAvatarRes),
                    contentDescription = "My Profile",
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
            }
        }
    }
}

/**
 * Filter Color Filter helper to reproduce Instagram-like photo looks
 */
fun getFilterColorFilter(filterName: String): ColorFilter? {
    return when (filterName) {
        "Clarendon" -> {
            val cm = ColorMatrix(
                floatArrayOf(
                    1.1f, 0f, 0f, 0f, 10f,
                    0f, 1.05f, 0f, 0f, 10f,
                    0f, 0f, 1.25f, 0f, 20f,
                    0f, 0f, 0f, 1f, 0f
                )
            )
            ColorFilter.colorMatrix(cm)
        }
        "Juno" -> {
            val cm = ColorMatrix(
                floatArrayOf(
                    1.2f, 0f, 0f, 0f, 15f,
                    0f, 1.15f, 0f, 0f, 10f,
                    0f, 0f, 0.9f, 0f, 0f,
                    0f, 0f, 0f, 1f, 0f
                )
            )
            ColorFilter.colorMatrix(cm)
        }
        "Valencia" -> {
            val cm = ColorMatrix(
                floatArrayOf(
                    1.15f, 0.05f, 0f, 0f, 12f,
                    0.05f, 1.05f, 0f, 0f, 8f,
                    0f, 0.05f, 0.85f, 0f, -5f,
                    0f, 0f, 0f, 1f, 0f
                )
            )
            ColorFilter.colorMatrix(cm)
        }
        "Vintage" -> {
            val cm = ColorMatrix()
            cm.setToSaturation(0.6f)
            ColorFilter.colorMatrix(cm)
        }
        "Noir" -> {
            val cm = ColorMatrix()
            cm.setToSaturation(0.0f)
            ColorFilter.colorMatrix(cm)
        }
        else -> null
    }
}
