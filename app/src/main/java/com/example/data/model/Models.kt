package com.example.data.model

import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.Color

data class User(
    val id: String,
    val username: String,
    val fullName: String,
    @DrawableRes val avatarRes: Int,
    val isVerified: Boolean = false,
    val bio: String = "",
    val website: String = "",
    val followersCount: Int = 0,
    val followingCount: Int = 0,
    val postsCount: Int = 0,
    val isFollowing: Boolean = false,
    val note: String? = null,
    val isDeactivated: Boolean = false,
    val isScheduledForDeletion: Boolean = false,
    val deletionDaysLeft: Int = 51,
    val deletionDate: String = "December 1, 2026",
    val isChannelTerminated: Boolean = true,
    val copyrightStrikesCount: Int = 3,
    val totalStrikesLimit: Int = 3,
    val hasContentRemoved: Boolean = true
)

data class Post(
    val id: String,
    val user: User,
    @DrawableRes val imageRes: Int,
    val caption: String,
    val likesCount: Int,
    val commentsCount: Int,
    val timeAgo: String,
    val isLiked: Boolean = false,
    val isSaved: Boolean = false,
    val location: String? = null,
    val filterName: String = "Normal",
    val isCopyrightRemoved: Boolean = false,
    val isReposted: Boolean = false,
    val repostsCount: Int = 14,
    val repostedBy: String? = null
)

data class Story(
    val id: String,
    val user: User,
    @DrawableRes val imageRes: Int,
    val timeAgo: String,
    val isViewed: Boolean = false,
    val isCloseFriend: Boolean = false,
    val caption: String? = null,
    val isReposted: Boolean = false,
    val repostedFromUsername: String? = null
)

data class Comment(
    val id: String,
    val postId: String,
    val user: User,
    val text: String,
    val timeAgo: String,
    val likesCount: Int = 0,
    val isLiked: Boolean = false
)

data class ChatMessage(
    val id: String,
    val senderId: String,
    val text: String,
    val timestamp: String,
    val isMine: Boolean,
    val isLiked: Boolean = false
)

data class ChatConversation(
    val id: String,
    val user: User,
    val lastMessage: String,
    val timestamp: String,
    val unreadCount: Int = 0,
    val isOnline: Boolean = false,
    val messages: List<ChatMessage> = emptyList()
)

enum class NotificationType {
    LIKE,
    COMMENT,
    FOLLOW
}

data class NotificationItem(
    val id: String,
    val user: User,
    val type: NotificationType,
    val text: String,
    val timeAgo: String,
    @DrawableRes val postImageRes: Int? = null,
    val isFollowedBack: Boolean = false
)

data class ReelItem(
    val id: String,
    val user: User,
    @DrawableRes val thumbnailRes: Int,
    val audioTrack: String,
    val caption: String,
    val likesCount: Int,
    val commentsCount: Int,
    val sharesCount: Int,
    val isLiked: Boolean = false,
    val isSaved: Boolean = false,
    val isTrialReel: Boolean = false,
    val trialViewsCount: Int = 0,
    val trialNonFollowerPercentage: Int = 92,
    val trialDurationHoursLeft: Int = 48,
    val isGraduatedFromTrial: Boolean = false,
    val isReposted: Boolean = false,
    val repostsCount: Int = 184
)

data class ChannelMessage(
    val id: String,
    val sender: User,
    val text: String,
    val timestamp: String,
    @DrawableRes val mediaRes: Int? = null,
    val isVideo: Boolean = false,
    val mediaCaption: String? = null,
    val reactions: Map<String, Int> = mapOf("❤️" to 12, "🔥" to 28, "👏" to 9),
    val userReactions: Set<String> = emptySet()
)

data class BroadcastChannel(
    val id: String,
    val title: String,
    val description: String,
    val creator: User,
    val membersCount: Int,
    @DrawableRes val avatarRes: Int,
    val isSubscriberOnly: Boolean = false,
    val messages: List<ChannelMessage> = emptyList(),
    val isJoined: Boolean = true,
    val category: String = "Creator Broadcast"
)

data class PhotoFilter(
    val name: String,
    val displayName: String,
    val colorFilter: Color? = null,
    val saturationScale: Float = 1.0f,
    val contrastScale: Float = 1.0f
)

enum class PlaylistItemType {
    POST,
    REEL,
    STORY
}

data class PlaylistItem(
    val id: String,
    val type: PlaylistItemType,
    val sourceId: String,
    val title: String,
    val creatorUsername: String,
    @DrawableRes val thumbnailRes: Int
)

data class Playlist(
    val id: String,
    val title: String,
    val description: String = "",
    @DrawableRes val coverRes: Int,
    val isPrivate: Boolean = false, // Public or Private
    val items: List<PlaylistItem> = emptyList(),
    val createdAt: String = "Recently"
)

data class CopyrightStrike(
    val id: String,
    val contentTitle: String,
    val strikeNumber: Int,
    val totalStrikes: Int = 3,
    val dateReceived: String,
    val claimantName: String,
    val claimType: String,
    val isContentRemoved: Boolean = true,
    @DrawableRes val thumbnailRes: Int,
    val videoDuration: String = "4:15",
    val statusText: String = "Active strike (Content removed)",
    val counterNoticeEligible: Boolean = true
)

data class CreatorSubscription(
    val id: String,
    val creator: User,
    val channelTitle: String,
    val tier: String = "All Access Insiders",
    val monthlyPrice: String = "$4.99/mo",
    val memberSince: String = "August 2026",
    val unreadPostsCount: Int = 3,
    @DrawableRes val latestPostRes: Int,
    val latestPostTitle: String = "Behind the scenes 4K footage drop"
)

data class AudioTrackInfo(
    val id: String = "bruno_major_nothing",
    val title: String = "Nothing",
    val artist: String = "Bruno Major",
    val duration: String = "2:42",
    val reelsCount: String = "1.4M reels",
    val isTrending: Boolean = true,
    val lyrics: List<String> = listOf(
        "Tracksuit, some red wine, movies for two",
        "We'll take off our phones and we'll turn off our shoes",
        "We'll play Nintendo though I always lose",
        "'Cause you watch the TV while I'm watching you",
        "There's not many people I'd honestly say",
        "I don't mind losing to",
        "But there's nothing like doing nothing with you ✨",
        "Dumb conversation, we lose track of time",
        "Have I told you lately I'm grateful you're mine?",
        "We'll watch The Notebook for the seventeenth time",
        "I'll say it's stupid, then you'll catch me crying",
        "We're not making out on a boat in the rain",
        "Or in a house I painted blue",
        "But there's nothing like doing nothing with you 🎶"
    ),
    @DrawableRes val albumArtRes: Int = 0,
    val isSaved: Boolean = false
)

