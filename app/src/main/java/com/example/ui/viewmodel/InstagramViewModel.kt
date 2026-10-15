package com.example.ui.viewmodel

import androidx.annotation.DrawableRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.R
import com.example.data.model.*
import com.example.data.repository.InstagramRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

enum class MainTab {
    HOME,
    SEARCH,
    CREATE,
    REELS,
    PROFILE
}

enum class CreateMode {
    PHOTO_POST,
    VIDEO_REEL
}

sealed class ActiveOverlay {
    data object None : ActiveOverlay()
    data class StoryViewer(val initialStoryIndex: Int) : ActiveOverlay()
    data class CommentsSheet(val postId: String) : ActiveOverlay()
    data object DirectMessages : ActiveOverlay()
    data class ChatConversation(val conversationId: String) : ActiveOverlay()
    data object Notifications : ActiveOverlay()
    data class PostDetail(val postId: String) : ActiveOverlay()
    data object EditProfile : ActiveOverlay()
    data class PlaylistDetail(val playlistId: String) : ActiveOverlay()
    data class AddToPlaylistSheet(val item: PlaylistItem) : ActiveOverlay()
    data class CreatePlaylistSheet(val initialItem: PlaylistItem? = null) : ActiveOverlay()
    data object Settings : ActiveOverlay()
    data object SwitchAccountSheet : ActiveOverlay()
    data object AddAccountSheet : ActiveOverlay()
    data object DeleteDeactivateDialog : ActiveOverlay()
    data class DeletionStatusDialog(val daysLeft: Int = 51, val deletionDate: String = "December 1, 2026") : ActiveOverlay()
    data class ErrorDialog(val title: String = "Something went wrong", val message: String = "Please try again later") : ActiveOverlay()
    data class TryAgainErrorDialog(
        val title: String = "Something went wrong",
        val message: String = "Please try again later. Something went wrong loading your content.",
        val onTryAgain: () -> Unit = {}
    ) : ActiveOverlay()
    data class ActionNotAllowedDialog(
        val title: String = "This action isn't allowed",
        val message: String = "This action isn't allowed right now. Please retry.",
        val onRetry: () -> Unit = {}
    ) : ActiveOverlay()
    data class SessionExpiredDialog(
        val title: String = "Something went wrong",
        val message: String = "Session expired. Please log in.",
        val onOk: () -> Unit = {}
    ) : ActiveOverlay()
    data class ChannelMessages(val channelId: String) : ActiveOverlay()
    data object CreateChannelSheet : ActiveOverlay()
    data class TrialReelInsights(val reelId: String) : ActiveOverlay()
    data object AccountStatus : ActiveOverlay()
    data object SubscriptionsPage : ActiveOverlay()
    data class AudioDetail(val audioTrack: AudioTrackInfo) : ActiveOverlay()
}

class InstagramViewModel(
    private val repository: InstagramRepository = InstagramRepository()
) : ViewModel() {

    val currentUser: StateFlow<User> = repository.currentUser
    val availableAccounts: StateFlow<List<User>> = repository.availableAccounts
    val posts: StateFlow<List<Post>> = repository.posts
    val stories: StateFlow<List<Story>> = repository.stories
    val reels: StateFlow<List<ReelItem>> = repository.reels
    val conversations: StateFlow<List<ChatConversation>> = repository.conversations
    val notifications: StateFlow<List<NotificationItem>> = repository.notifications
    val commentsMap: StateFlow<Map<String, List<Comment>>> = repository.comments
    val playlists: StateFlow<List<Playlist>> = repository.playlists
    val channels: StateFlow<List<BroadcastChannel>> = repository.channels
    val copyrightStrikes: StateFlow<List<CopyrightStrike>> = repository.copyrightStrikes
    val subscriptions: StateFlow<List<CreatorSubscription>> = repository.subscriptions
    val featuredAudioTrack: StateFlow<AudioTrackInfo> = repository.featuredAudioTrack

    private val _homeErrorActive = MutableStateFlow(false)
    val homeErrorActive: StateFlow<Boolean> = _homeErrorActive.asStateFlow()

    private val _currentTab = MutableStateFlow(MainTab.HOME)
    val currentTab: StateFlow<MainTab> = _currentTab.asStateFlow()

    private val _activeOverlay = MutableStateFlow<ActiveOverlay>(ActiveOverlay.None)
    val activeOverlay: StateFlow<ActiveOverlay> = _activeOverlay.asStateFlow()

    // Search query & category state
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedCategory = MutableStateFlow("For You")
    val selectedCategory: StateFlow<String> = _selectedCategory.asStateFlow()

    // Create mode (Photo vs Video)
    private val _createMode = MutableStateFlow(CreateMode.PHOTO_POST)
    val createMode: StateFlow<CreateMode> = _createMode.asStateFlow()

    // Create photo post draft state
    private val _draftCaption = MutableStateFlow("")
    val draftCaption: StateFlow<String> = _draftCaption.asStateFlow()

    private val _draftLocation = MutableStateFlow("")
    val draftLocation: StateFlow<String> = _draftLocation.asStateFlow()

    private val _draftImageRes = MutableStateFlow(R.drawable.img_post_travel)
    val draftImageRes: StateFlow<Int> = _draftImageRes.asStateFlow()

    private val _draftFilter = MutableStateFlow("Normal")
    val draftFilter: StateFlow<String> = _draftFilter.asStateFlow()

    // Video / Reel upload draft state
    private val _videoTitle = MutableStateFlow("")
    val videoTitle: StateFlow<String> = _videoTitle.asStateFlow()

    private val _videoDescription = MutableStateFlow("")
    val videoDescription: StateFlow<String> = _videoDescription.asStateFlow()

    private val _videoAudioTrack = MutableStateFlow("Trending Lo-Fi • Chill Session")
    val videoAudioTrack: StateFlow<String> = _videoAudioTrack.asStateFlow()

    private val _videoThumbnailRes = MutableStateFlow(R.drawable.img_post_city)
    val videoThumbnailRes: StateFlow<Int> = _videoThumbnailRes.asStateFlow()

    private val _isTrialReel = MutableStateFlow(false)
    val isTrialReel: StateFlow<Boolean> = _isTrialReel.asStateFlow()

    fun setIsTrialReel(value: Boolean) {
        _isTrialReel.value = value
    }

    fun setTab(tab: MainTab) {
        _currentTab.value = tab
    }

    fun openOverlay(overlay: ActiveOverlay) {
        _activeOverlay.value = overlay
    }

    fun closeOverlay() {
        _activeOverlay.value = ActiveOverlay.None
    }

    fun setCreateMode(mode: CreateMode) {
        _createMode.value = mode
    }

    fun toggleLikePost(postId: String) {
        repository.toggleLikePost(postId)
    }

    fun toggleSavePost(postId: String) {
        repository.toggleSavePost(postId)
    }

    fun toggleLikeReel(reelId: String) {
        repository.toggleLikeReel(reelId)
    }

    fun addComment(postId: String, text: String) {
        repository.addComment(postId, text)
    }

    fun toggleLikeComment(postId: String, commentId: String) {
        repository.toggleLikeComment(postId, commentId)
    }

    fun markStoryViewed(storyId: String) {
        repository.markStoryViewed(storyId)
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun setSelectedCategory(cat: String) {
        _selectedCategory.value = cat
    }

    fun setDraftImage(@DrawableRes res: Int) {
        _draftImageRes.value = res
    }

    fun setDraftCaption(text: String) {
        _draftCaption.value = text
    }

    fun setDraftLocation(text: String) {
        _draftLocation.value = text
    }

    fun setDraftFilter(filter: String) {
        _draftFilter.value = filter
    }

    // Video Draft setters
    fun setVideoTitle(title: String) {
        _videoTitle.value = title
    }

    fun setVideoDescription(description: String) {
        _videoDescription.value = description
    }

    fun setVideoAudioTrack(audio: String) {
        _videoAudioTrack.value = audio
    }

    fun setVideoThumbnail(@DrawableRes res: Int) {
        _videoThumbnailRes.value = res
    }

    fun publishDraftPost() {
        val caption = _draftCaption.value.ifBlank { "Sharing a new perspective ✨" }
        val location = _draftLocation.value.ifBlank { null }
        repository.createPost(
            caption = caption,
            imageRes = _draftImageRes.value,
            location = location,
            filterName = _draftFilter.value
        )
        // Reset draft
        _draftCaption.value = ""
        _draftLocation.value = ""
        _draftFilter.value = "Normal"
        // Return to home feed
        _currentTab.value = MainTab.HOME
    }

    fun publishDraftVideo() {
        val title = _videoTitle.value.ifBlank { "New Reel" }
        val description = _videoDescription.value.ifBlank { "Excited to share this video clip ✨ #reels #viral" }
        repository.createReel(
            title = title,
            description = description,
            thumbnailRes = _videoThumbnailRes.value,
            audioTrack = _videoAudioTrack.value,
            isTrialReel = _isTrialReel.value
        )
        // Reset draft
        _videoTitle.value = ""
        _videoDescription.value = ""
        _isTrialReel.value = false
        // Navigate to Reels to see the new video!
        _currentTab.value = MainTab.REELS
    }

    fun graduateTrialReel(reelId: String) {
        repository.graduateTrialReel(reelId)
        closeOverlay()
    }

    fun createBroadcastChannel(
        title: String,
        description: String,
        isSubscriberOnly: Boolean,
        @DrawableRes avatarRes: Int
    ) {
        val created = repository.createBroadcastChannel(title, description, isSubscriberOnly, avatarRes)
        _activeOverlay.value = ActiveOverlay.ChannelMessages(created.id)
    }

    fun sendChannelMessage(
        channelId: String,
        text: String,
        @DrawableRes mediaRes: Int? = null,
        isVideo: Boolean = false,
        mediaCaption: String? = null
    ) {
        repository.sendChannelMessage(channelId, text, mediaRes, isVideo, mediaCaption)
    }

    fun toggleChannelReaction(channelId: String, messageId: String, emoji: String) {
        repository.toggleChannelReaction(channelId, messageId, emoji)
    }

    fun updateProfile(fullName: String, username: String, bio: String, website: String) {
        repository.updateProfile(fullName, username, bio, website)
        closeOverlay()
    }

    fun sendChatMessage(conversationId: String, text: String) {
        repository.sendMessage(conversationId, text)
    }

    fun toggleFollowNotificationUser(notifId: String) {
        repository.toggleFollowNotificationUser(notifId)
    }

    // Playlist actions
    fun createPlaylist(
        title: String,
        description: String,
        isPrivate: Boolean,
        @DrawableRes coverRes: Int,
        initialItem: PlaylistItem? = null
    ) {
        repository.createPlaylist(title, description, isPrivate, coverRes, initialItem)
        closeOverlay()
    }

    fun togglePlaylistPrivacy(playlistId: String) {
        repository.togglePlaylistPrivacy(playlistId)
    }

    fun addItemToPlaylist(playlistId: String, item: PlaylistItem) {
        repository.addItemToPlaylist(playlistId, item)
    }

    fun removeItemFromPlaylist(playlistId: String, itemId: String) {
        repository.removeItemFromPlaylist(playlistId, itemId)
    }

    fun deletePlaylist(playlistId: String) {
        repository.deletePlaylist(playlistId)
        closeOverlay()
    }

    // Account Switcher & Add Account
    fun switchAccount(userId: String) {
        repository.switchAccount(userId)
        closeOverlay()
    }

    fun addAccount(
        username: String,
        fullName: String,
        bio: String,
        @DrawableRes avatarRes: Int
    ) {
        repository.addAccount(username, fullName, bio, avatarRes)
        closeOverlay()
    }

    fun deactivateAccount(isPermanentDeletion: Boolean = true) {
        repository.deactivateCurrentUser(isPermanentDeletion)
        _activeOverlay.value = ActiveOverlay.DeletionStatusDialog(51, "December 1, 2026")
    }

    fun reactivateAccount() {
        repository.reactivateCurrentUser()
        closeOverlay()
    }

    fun triggerSomethingWentWrong() {
        _activeOverlay.value = ActiveOverlay.ErrorDialog()
    }

    fun toggleChannelTermination(terminated: Boolean) {
        repository.toggleChannelTermination(terminated)
    }

    fun submitCounterNotification(strikeId: String) {
        repository.submitCounterNotification(strikeId)
    }

    fun setHomeErrorActive(active: Boolean) {
        _homeErrorActive.value = active
    }

    fun triggerHomeTryAgainDialog() {
        _activeOverlay.value = ActiveOverlay.TryAgainErrorDialog(
            title = "Something went wrong",
            message = "Can't load home feed right now. Please check your network and try again.",
            onTryAgain = {
                _homeErrorActive.value = false
                closeOverlay()
            }
        )
    }

    fun triggerSearchActionNotAllowed() {
        _activeOverlay.value = ActiveOverlay.ActionNotAllowedDialog(
            title = "This action isn't allowed",
            message = "You cannot perform this action due to policy restrictions or a network error. Tap RETRY to attempt again.",
            onRetry = {
                closeOverlay()
            }
        )
    }

    fun triggerVideoDetailsTryAgain() {
        _activeOverlay.value = ActiveOverlay.TryAgainErrorDialog(
            title = "Something went wrong",
            message = "Playback or content retrieval error. Please try again.",
            onTryAgain = {
                closeOverlay()
            }
        )
    }

    fun triggerMessagesSessionExpired() {
        _activeOverlay.value = ActiveOverlay.SessionExpiredDialog(
            title = "Something went wrong",
            message = "Session expired. Please log in.",
            onOk = {
                closeOverlay()
            }
        )
    }

    fun triggerSubscriptionsSessionExpired() {
        _activeOverlay.value = ActiveOverlay.SessionExpiredDialog(
            title = "Something went wrong",
            message = "Session expired. Please log in.",
            onOk = {
                closeOverlay()
            }
        )
    }

    fun openAudioDetail(track: AudioTrackInfo = repository.featuredAudioTrack.value) {
        _activeOverlay.value = ActiveOverlay.AudioDetail(track)
    }
}
