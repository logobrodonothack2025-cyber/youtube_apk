package com.example.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.model.PlaylistItem
import com.example.data.model.PlaylistItemType
import com.example.ui.components.InstagramBottomNav
import com.example.ui.components.InstagramTopBar
import com.example.ui.screens.*
import com.example.ui.viewmodel.ActiveOverlay
import com.example.ui.viewmodel.CreateMode
import com.example.ui.viewmodel.InstagramViewModel
import com.example.ui.viewmodel.MainTab

@Composable
fun InstagramApp(
    viewModel: InstagramViewModel = viewModel(),
    modifier: Modifier = Modifier
) {
    val currentUser by viewModel.currentUser.collectAsStateWithLifecycle()
    val availableAccounts by viewModel.availableAccounts.collectAsStateWithLifecycle()
    val posts by viewModel.posts.collectAsStateWithLifecycle()
    val stories by viewModel.stories.collectAsStateWithLifecycle()
    val reels by viewModel.reels.collectAsStateWithLifecycle()
    val conversations by viewModel.conversations.collectAsStateWithLifecycle()
    val notifications by viewModel.notifications.collectAsStateWithLifecycle()
    val commentsMap by viewModel.commentsMap.collectAsStateWithLifecycle()
    val playlists by viewModel.playlists.collectAsStateWithLifecycle()

    val currentTab by viewModel.currentTab.collectAsStateWithLifecycle()
    val activeOverlay by viewModel.activeOverlay.collectAsStateWithLifecycle()

    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val selectedCategory by viewModel.selectedCategory.collectAsStateWithLifecycle()

    val createMode by viewModel.createMode.collectAsStateWithLifecycle()
    val draftCaption by viewModel.draftCaption.collectAsStateWithLifecycle()
    val draftLocation by viewModel.draftLocation.collectAsStateWithLifecycle()
    val draftImageRes by viewModel.draftImageRes.collectAsStateWithLifecycle()
    val draftFilter by viewModel.draftFilter.collectAsStateWithLifecycle()

    val videoTitle by viewModel.videoTitle.collectAsStateWithLifecycle()
    val videoDescription by viewModel.videoDescription.collectAsStateWithLifecycle()
    val videoAudioTrack by viewModel.videoAudioTrack.collectAsStateWithLifecycle()
    val videoThumbnailRes by viewModel.videoThumbnailRes.collectAsStateWithLifecycle()
    val isTrialReel by viewModel.isTrialReel.collectAsStateWithLifecycle()
    val channels by viewModel.channels.collectAsStateWithLifecycle()
    val homeErrorActive by viewModel.homeErrorActive.collectAsStateWithLifecycle()
    val copyrightStrikes by viewModel.copyrightStrikes.collectAsStateWithLifecycle()
    val subscriptions by viewModel.subscriptions.collectAsStateWithLifecycle()

    // Back handling
    BackHandler(enabled = activeOverlay !is ActiveOverlay.None || currentTab != MainTab.HOME) {
        if (activeOverlay !is ActiveOverlay.None) {
            viewModel.closeOverlay()
        } else if (currentTab != MainTab.HOME) {
            viewModel.setTab(MainTab.HOME)
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        Scaffold(
            topBar = {
                if (currentTab == MainTab.HOME) {
                    InstagramTopBar(
                        unreadMessages = conversations.count { it.unreadCount > 0 },
                        hasNewActivity = true,
                        onNotificationsClick = { viewModel.openOverlay(ActiveOverlay.Notifications) },
                        onDirectMessagesClick = { viewModel.openOverlay(ActiveOverlay.DirectMessages) },
                        onSubscriptionsClick = { viewModel.openOverlay(ActiveOverlay.SubscriptionsPage) }
                    )
                }
            },
            bottomBar = {
                if (currentTab != MainTab.CREATE) {
                    InstagramBottomNav(
                        currentTab = currentTab,
                        userAvatarRes = currentUser.avatarRes,
                        onTabSelected = { viewModel.setTab(it) }
                    )
                }
            }
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                when (currentTab) {
                    MainTab.HOME -> {
                        HomeScreen(
                            posts = posts,
                            stories = stories,
                            currentUser = currentUser,
                            isErrorState = homeErrorActive,
                            onRetryFeed = { viewModel.setHomeErrorActive(false) },
                            onSimulateErrorClick = { viewModel.triggerHomeTryAgainDialog() },
                            onStoryClick = { index ->
                                viewModel.openOverlay(ActiveOverlay.StoryViewer(index))
                            },
                            onLikePost = { viewModel.toggleLikePost(it) },
                            onSavePost = { viewModel.toggleSavePost(it) },
                            onAddToPlaylist = { post ->
                                viewModel.openOverlay(
                                    ActiveOverlay.AddToPlaylistSheet(
                                        PlaylistItem(
                                            id = "pli_${System.currentTimeMillis()}",
                                            type = PlaylistItemType.POST,
                                            sourceId = post.id,
                                            title = post.caption.take(35),
                                            creatorUsername = post.user.username,
                                            thumbnailRes = post.imageRes
                                        )
                                    )
                                )
                            },
                            onCommentClick = { postId ->
                                viewModel.openOverlay(ActiveOverlay.CommentsSheet(postId))
                            },
                            onShareClick = {
                                viewModel.openOverlay(ActiveOverlay.DirectMessages)
                            },
                            onUserClick = { user ->
                                if (user.id == currentUser.id) {
                                    viewModel.setTab(MainTab.PROFILE)
                                }
                            },
                            onAudioClick = { viewModel.openAudioDetail() }
                        )
                    }

                    MainTab.SEARCH -> {
                        SearchScreen(
                            searchQuery = searchQuery,
                            onSearchQueryChange = { viewModel.setSearchQuery(it) },
                            selectedCategory = selectedCategory,
                            onCategorySelect = { viewModel.setSelectedCategory(it) },
                            posts = posts,
                            onPostClick = { postId ->
                                viewModel.openOverlay(ActiveOverlay.PostDetail(postId))
                            },
                            onUserClick = { user ->
                                if (user.id == currentUser.id) {
                                    viewModel.setTab(MainTab.PROFILE)
                                }
                            },
                            onTriggerActionNotAllowed = { viewModel.triggerSearchActionNotAllowed() },
                            onAudioClick = { viewModel.openAudioDetail() }
                        )
                    }

                    MainTab.CREATE -> {
                        CreatePostScreen(
                            currentUser = currentUser,
                            createMode = createMode,
                            onModeChange = { viewModel.setCreateMode(it) },
                            draftImageRes = draftImageRes,
                            onDraftImageChange = { viewModel.setDraftImage(it) },
                            draftCaption = draftCaption,
                            onDraftCaptionChange = { viewModel.setDraftCaption(it) },
                            draftLocation = draftLocation,
                            onDraftLocationChange = { viewModel.setDraftLocation(it) },
                            draftFilter = draftFilter,
                            onDraftFilterChange = { viewModel.setDraftFilter(it) },
                            onPublish = { viewModel.publishDraftPost() },
                            videoTitle = videoTitle,
                            onVideoTitleChange = { viewModel.setVideoTitle(it) },
                            videoDescription = videoDescription,
                            onVideoDescriptionChange = { viewModel.setVideoDescription(it) },
                            videoAudioTrack = videoAudioTrack,
                            onVideoAudioTrackChange = { viewModel.setVideoAudioTrack(it) },
                            videoThumbnailRes = videoThumbnailRes,
                            onVideoThumbnailChange = { viewModel.setVideoThumbnail(it) },
                            isTrialReel = isTrialReel,
                            onIsTrialReelChange = { viewModel.setIsTrialReel(it) },
                            onPublishVideo = { viewModel.publishDraftVideo() },
                            onCancel = { viewModel.setTab(MainTab.HOME) }
                        )
                    }

                    MainTab.REELS -> {
                        ReelsScreen(
                            reels = reels,
                            onLikeReel = { viewModel.toggleLikeReel(it) },
                            onCommentClick = {
                                viewModel.openOverlay(ActiveOverlay.CommentsSheet(posts.first().id))
                            },
                            onShareClick = {
                                viewModel.openOverlay(ActiveOverlay.DirectMessages)
                            },
                            onAddToPlaylist = { reel ->
                                viewModel.openOverlay(
                                    ActiveOverlay.AddToPlaylistSheet(
                                        PlaylistItem(
                                            id = "pli_${System.currentTimeMillis()}",
                                            type = PlaylistItemType.REEL,
                                            sourceId = reel.id,
                                            title = reel.caption.take(35),
                                            creatorUsername = reel.user.username,
                                            thumbnailRes = reel.thumbnailRes
                                        )
                                    )
                                )
                            },
                            onUserClick = { user ->
                                if (user.id == currentUser.id) {
                                    viewModel.setTab(MainTab.PROFILE)
                                }
                            },
                            onGraduateTrialReel = { viewModel.graduateTrialReel(it) },
                            onAudioClick = { viewModel.openAudioDetail() }
                        )
                    }

                    MainTab.PROFILE -> {
                        ProfileScreen(
                            user = currentUser,
                            posts = posts,
                            playlists = playlists,
                            onEditProfileClick = { viewModel.openOverlay(ActiveOverlay.EditProfile) },
                            onPostClick = { postId ->
                                viewModel.openOverlay(ActiveOverlay.PostDetail(postId))
                            },
                            onPlaylistClick = { playlistId ->
                                viewModel.openOverlay(ActiveOverlay.PlaylistDetail(playlistId))
                            },
                            onCreatePlaylistClick = {
                                viewModel.openOverlay(ActiveOverlay.CreatePlaylistSheet())
                            },
                            onSettingsClick = {
                                viewModel.openOverlay(ActiveOverlay.Settings)
                            },
                            onSwitchAccountClick = {
                                viewModel.openOverlay(ActiveOverlay.SwitchAccountSheet)
                            },
                            onCreatePostClick = {
                                viewModel.setTab(MainTab.CREATE)
                                viewModel.setCreateMode(CreateMode.PHOTO_POST)
                            },
                            onCreateReelClick = {
                                viewModel.setTab(MainTab.CREATE)
                                viewModel.setCreateMode(CreateMode.VIDEO_REEL)
                            },
                            onAccountStatusClick = {
                                viewModel.openOverlay(ActiveOverlay.AccountStatus)
                            }
                        )
                    }
                }
            }
        }

        // Active Overlays
        when (val overlay = activeOverlay) {
            is ActiveOverlay.StoryViewer -> {
                StoryViewerScreen(
                    stories = stories,
                    initialIndex = overlay.initialStoryIndex,
                    onClose = { viewModel.closeOverlay() },
                    onStoryViewed = { viewModel.markStoryViewed(it) },
                    onAddToPlaylist = { story ->
                        viewModel.openOverlay(
                            ActiveOverlay.AddToPlaylistSheet(
                                PlaylistItem(
                                    id = "pli_${System.currentTimeMillis()}",
                                    type = PlaylistItemType.STORY,
                                    sourceId = story.id,
                                    title = story.caption ?: "Story by @${story.user.username}",
                                    creatorUsername = story.user.username,
                                    thumbnailRes = story.imageRes
                                )
                            )
                        )
                    }
                )
            }

            is ActiveOverlay.CommentsSheet -> {
                val postComments = commentsMap[overlay.postId].orEmpty()
                CommentsSheet(
                    postId = overlay.postId,
                    comments = postComments,
                    currentUser = currentUser,
                    onAddComment = { text -> viewModel.addComment(overlay.postId, text) },
                    onLikeComment = { commentId -> viewModel.toggleLikeComment(overlay.postId, commentId) },
                    onDismiss = { viewModel.closeOverlay() }
                )
            }

            is ActiveOverlay.DirectMessages -> {
                DirectMessagesScreen(
                    currentUser = currentUser,
                    conversations = conversations,
                    channels = channels,
                    onSelectConversation = { convId ->
                        viewModel.openOverlay(ActiveOverlay.ChatConversation(convId))
                    },
                    onSelectChannel = { channelId ->
                        viewModel.openOverlay(ActiveOverlay.ChannelMessages(channelId))
                    },
                    onCreateChannelClick = {
                        viewModel.openOverlay(ActiveOverlay.CreateChannelSheet)
                    },
                    onBack = { viewModel.closeOverlay() },
                    onTriggerError = { viewModel.triggerMessagesSessionExpired() }
                )
            }

            is ActiveOverlay.ChatConversation -> {
                val conv = conversations.firstOrNull { it.id == overlay.conversationId }
                if (conv != null) {
                    ChatConversationScreen(
                        conversation = conv,
                        onSendMessage = { text -> viewModel.sendChatMessage(conv.id, text) },
                        onBack = { viewModel.openOverlay(ActiveOverlay.DirectMessages) }
                    )
                } else {
                    viewModel.closeOverlay()
                }
            }

            is ActiveOverlay.Notifications -> {
                NotificationsScreen(
                    notifications = notifications,
                    onToggleFollowBack = { notifId -> viewModel.toggleFollowNotificationUser(notifId) },
                    onBack = { viewModel.closeOverlay() }
                )
            }

            is ActiveOverlay.PostDetail -> {
                val post = posts.firstOrNull { it.id == overlay.postId }
                if (post != null) {
                    PostDetailModal(
                        post = post,
                        onLikePost = { viewModel.toggleLikePost(it) },
                        onSavePost = { viewModel.toggleSavePost(it) },
                        onAddToPlaylist = { p ->
                            viewModel.openOverlay(
                                ActiveOverlay.AddToPlaylistSheet(
                                    PlaylistItem(
                                        id = "pli_${System.currentTimeMillis()}",
                                        type = PlaylistItemType.POST,
                                        sourceId = p.id,
                                        title = p.caption.take(35),
                                        creatorUsername = p.user.username,
                                        thumbnailRes = p.imageRes
                                    )
                                )
                            )
                        },
                        onCommentClick = { postId ->
                            viewModel.openOverlay(ActiveOverlay.CommentsSheet(postId))
                        },
                        onShareClick = {
                            viewModel.openOverlay(ActiveOverlay.DirectMessages)
                        },
                        onUserClick = { user ->
                            if (user.id == currentUser.id) {
                                viewModel.setTab(MainTab.PROFILE)
                            }
                            viewModel.closeOverlay()
                        },
                        onBack = { viewModel.closeOverlay() },
                        onTriggerVideoError = { viewModel.triggerVideoDetailsTryAgain() },
                        onAudioClick = { viewModel.openAudioDetail() }
                    )
                } else {
                    viewModel.closeOverlay()
                }
            }

            is ActiveOverlay.EditProfile -> {
                EditProfileSheet(
                    currentUser = currentUser,
                    onSave = { fullName, username, bio, website ->
                        viewModel.updateProfile(fullName, username, bio, website)
                    },
                    onCancel = { viewModel.closeOverlay() }
                )
            }

            is ActiveOverlay.PlaylistDetail -> {
                val playlist = playlists.firstOrNull { it.id == overlay.playlistId }
                if (playlist != null) {
                    PlaylistDetailScreen(
                        playlist = playlist,
                        onBack = { viewModel.closeOverlay() },
                        onTogglePrivacy = { viewModel.togglePlaylistPrivacy(playlist.id) },
                        onRemoveItem = { itemId -> viewModel.removeItemFromPlaylist(playlist.id, itemId) },
                        onItemClick = { item ->
                            when (item.type) {
                                PlaylistItemType.POST -> viewModel.openOverlay(ActiveOverlay.PostDetail(item.sourceId))
                                PlaylistItemType.REEL -> viewModel.setTab(MainTab.REELS)
                                PlaylistItemType.STORY -> viewModel.openOverlay(ActiveOverlay.StoryViewer(0))
                            }
                        },
                        onDeletePlaylist = { viewModel.deletePlaylist(playlist.id) }
                    )
                } else {
                    viewModel.closeOverlay()
                }
            }

            is ActiveOverlay.AddToPlaylistSheet -> {
                AddToPlaylistSheet(
                    item = overlay.item,
                    playlists = playlists,
                    onSelectPlaylist = { plId ->
                        viewModel.addItemToPlaylist(plId, overlay.item)
                    },
                    onCreateNewPlaylist = {
                        viewModel.openOverlay(ActiveOverlay.CreatePlaylistSheet(initialItem = overlay.item))
                    },
                    onDismiss = { viewModel.closeOverlay() }
                )
            }

            is ActiveOverlay.CreatePlaylistSheet -> {
                CreatePlaylistSheet(
                    initialItem = overlay.initialItem,
                    onSave = { title, desc, isPrivate, cover ->
                        viewModel.createPlaylist(title, desc, isPrivate, cover, overlay.initialItem)
                    },
                    onCancel = { viewModel.closeOverlay() }
                )
            }

            is ActiveOverlay.Settings -> {
                SettingsScreen(
                    currentUser = currentUser,
                    availableAccounts = availableAccounts,
                    onBack = { viewModel.closeOverlay() },
                    onOpenSwitchAccount = { viewModel.openOverlay(ActiveOverlay.SwitchAccountSheet) },
                    onOpenAddAccount = { viewModel.openOverlay(ActiveOverlay.AddAccountSheet) },
                    onSwitchDirectly = { userId -> viewModel.switchAccount(userId) },
                    onOpenDeleteDeactivate = { viewModel.openOverlay(ActiveOverlay.DeleteDeactivateDialog) },
                    onReactivateAccount = { viewModel.reactivateAccount() },
                    onOpenAccountStatus = { viewModel.openOverlay(ActiveOverlay.AccountStatus) },
                    onOpenSubscriptions = { viewModel.openOverlay(ActiveOverlay.SubscriptionsPage) }
                )
            }

            is ActiveOverlay.SwitchAccountSheet -> {
                SwitchAccountSheet(
                    currentUserId = currentUser.id,
                    availableAccounts = availableAccounts,
                    onSelectAccount = { userId -> viewModel.switchAccount(userId) },
                    onAddNewAccount = { viewModel.openOverlay(ActiveOverlay.AddAccountSheet) },
                    onDismiss = { viewModel.closeOverlay() }
                )
            }

            is ActiveOverlay.AddAccountSheet -> {
                AddAccountSheet(
                    onAddAccount = { username, fullName, bio, avatarRes ->
                        viewModel.addAccount(username, fullName, bio, avatarRes)
                    },
                    onDismiss = { viewModel.closeOverlay() }
                )
            }

            is ActiveOverlay.DeleteDeactivateDialog -> {
                DeleteDeactivateDialog(
                    currentUser = currentUser,
                    onConfirm = { isPermanent ->
                        viewModel.deactivateAccount(isPermanent)
                    },
                    onDismiss = { viewModel.closeOverlay() }
                )
            }

            is ActiveOverlay.DeletionStatusDialog -> {
                DeletionStatusDialog(
                    daysLeft = overlay.daysLeft,
                    deletionDate = overlay.deletionDate,
                    onReactivate = { viewModel.reactivateAccount() },
                    onDismiss = { viewModel.closeOverlay() }
                )
            }

            is ActiveOverlay.ErrorDialog -> {
                ErrorDialog(
                    title = overlay.title,
                    message = overlay.message,
                    onDismiss = { viewModel.closeOverlay() }
                )
            }

            is ActiveOverlay.TryAgainErrorDialog -> {
                TryAgainErrorDialog(
                    title = overlay.title,
                    message = overlay.message,
                    onTryAgain = overlay.onTryAgain,
                    onDismiss = { viewModel.closeOverlay() }
                )
            }

            is ActiveOverlay.ActionNotAllowedDialog -> {
                ActionNotAllowedDialog(
                    title = overlay.title,
                    message = overlay.message,
                    onRetry = overlay.onRetry,
                    onDismiss = { viewModel.closeOverlay() }
                )
            }

            is ActiveOverlay.SessionExpiredDialog -> {
                SessionExpiredDialog(
                    title = overlay.title,
                    message = overlay.message,
                    onOk = {
                        overlay.onOk()
                        viewModel.closeOverlay()
                    }
                )
            }

            is ActiveOverlay.AccountStatus -> {
                AccountStatusScreen(
                    currentUser = currentUser,
                    copyrightStrikes = copyrightStrikes,
                    onToggleTermination = { viewModel.toggleChannelTermination(it) },
                    onSubmitCounterNotice = { viewModel.submitCounterNotification(it) },
                    onBack = { viewModel.closeOverlay() }
                )
            }

            is ActiveOverlay.SubscriptionsPage -> {
                SubscriptionsScreen(
                    currentUser = currentUser,
                    subscriptions = subscriptions,
                    onTriggerSessionExpired = { viewModel.triggerSubscriptionsSessionExpired() },
                    onBack = { viewModel.closeOverlay() }
                )
            }

            is ActiveOverlay.ChannelMessages -> {
                val channel = channels.firstOrNull { it.id == overlay.channelId }
                if (channel != null) {
                    ChannelMessagesScreen(
                        channel = channel,
                        currentUser = currentUser,
                        onSendMessage = { text, mediaRes, isVideo, caption ->
                            viewModel.sendChannelMessage(channel.id, text, mediaRes, isVideo, caption)
                        },
                        onToggleReaction = { msgId, emoji ->
                            viewModel.toggleChannelReaction(channel.id, msgId, emoji)
                        },
                        onBack = { viewModel.openOverlay(ActiveOverlay.DirectMessages) }
                    )
                } else {
                    viewModel.closeOverlay()
                }
            }

            is ActiveOverlay.CreateChannelSheet -> {
                CreateChannelDialog(
                    currentUser = currentUser,
                    onConfirm = { title, desc, isSubOnly ->
                        viewModel.createBroadcastChannel(title, desc, isSubOnly, currentUser.avatarRes)
                    },
                    onDismiss = { viewModel.closeOverlay() }
                )
            }

            is ActiveOverlay.TrialReelInsights -> {
                val trialReel = reels.firstOrNull { it.id == overlay.reelId }
                if (trialReel != null) {
                    TrialReelInsightsDialog(
                        reel = trialReel,
                        onGraduate = {
                            viewModel.graduateTrialReel(trialReel.id)
                        },
                        onDismiss = { viewModel.closeOverlay() }
                    )
                } else {
                    viewModel.closeOverlay()
                }
            }

            is ActiveOverlay.AudioDetail -> {
                AudioDetailScreen(
                    track = overlay.audioTrack,
                    reels = reels,
                    onUseAudio = {
                        viewModel.closeOverlay()
                        viewModel.setTab(MainTab.CREATE)
                        viewModel.setCreateMode(CreateMode.VIDEO_REEL)
                        viewModel.setVideoAudioTrack("${overlay.audioTrack.artist} • ${overlay.audioTrack.title}")
                    },
                    onSelectReel = { reelId ->
                        viewModel.closeOverlay()
                        viewModel.setTab(MainTab.REELS)
                    },
                    onBack = { viewModel.closeOverlay() }
                )
            }

            ActiveOverlay.None -> { /* No overlay */ }
        }
    }
}
