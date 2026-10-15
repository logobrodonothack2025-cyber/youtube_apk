package com.example.data.repository

import androidx.annotation.DrawableRes
import com.example.R
import com.example.data.model.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class InstagramRepository {

    private val _currentUser = MutableStateFlow(
        User(
            id = "user_me",
            username = "maya_lens",
            fullName = "Maya Sterling",
            avatarRes = R.drawable.img_user_avatar,
            isVerified = true,
            bio = "Visual storyteller & travel photographer 📷✨\nExploring hidden corners of the globe 🌍\nTokyo • Milan • NYC",
            website = "mayasterling.portfolio.me",
            followersCount = 14280,
            followingCount = 384,
            postsCount = 9,
            note = "Coffee time in Tokyo ☕",
            isChannelTerminated = true,
            copyrightStrikesCount = 3,
            totalStrikesLimit = 3,
            hasContentRemoved = true
        )
    )
    val currentUser: StateFlow<User> = _currentUser.asStateFlow()

    private val _copyrightStrikes = MutableStateFlow<List<CopyrightStrike>>(
        listOf(
            CopyrightStrike(
                id = "strike_1",
                contentTitle = "Night Drive Tokyo Soundtrack 4K",
                strikeNumber = 1,
                totalStrikes = 3,
                dateReceived = "September 14, 2026",
                claimantName = "Sony Music Entertainment Japan",
                claimType = "Sound Recording Master Rights",
                isContentRemoved = true,
                thumbnailRes = R.drawable.img_post_city,
                videoDuration = "3:42",
                statusText = "Content Removed (Strike 1 of 3)"
            ),
            CopyrightStrike(
                id = "strike_2",
                contentTitle = "Summer Festival DJ Live Session",
                strikeNumber = 2,
                totalStrikes = 3,
                dateReceived = "September 28, 2026",
                claimantName = "Universal Music Group",
                claimType = "Commercial Audio Synchronization",
                isContentRemoved = true,
                thumbnailRes = R.drawable.img_post_travel,
                videoDuration = "5:18",
                statusText = "Content Removed (Strike 2 of 3)"
            ),
            CopyrightStrike(
                id = "strike_3",
                contentTitle = "Stadium Tour Live Stream Broadcast",
                strikeNumber = 3,
                totalStrikes = 3,
                dateReceived = "October 11, 2026",
                claimantName = "Warner Music Group / LiveNation Global",
                claimType = "Unauthorized Live Concert Broadcast",
                isContentRemoved = true,
                thumbnailRes = R.drawable.img_post_coffee,
                videoDuration = "12:04",
                statusText = "Content Removed (Strike 3 of 3 • Terminal)"
            )
        )
    )
    val copyrightStrikes: StateFlow<List<CopyrightStrike>> = _copyrightStrikes.asStateFlow()

    private val initialUsers = listOf(
        User("u1", "alex_wander", "Alex Rivera", R.drawable.img_post_travel, isVerified = false, followersCount = 4520, followingCount = 412, note = "Golden hour vibes ✨"),
        User("u2", "sophia_cafe", "Sophia Chen", R.drawable.img_post_coffee, isVerified = true, followersCount = 28900, followingCount = 590, note = "Fresh brews daily 🥐"),
        User("u3", "kenji_streets", "Kenji Sato", R.drawable.img_post_city, isVerified = true, followersCount = 89400, followingCount = 210, note = "Shibuya night shoot 🌃"),
        User("u4", "elena_creative", "Elena Rostova", R.drawable.img_user_avatar, isVerified = true, followersCount = 31200, followingCount = 670, note = "Art gallery preview 🎨"),
        User("u5", "design_studio", "Studio Artisan", R.drawable.ic_launcher_img, isVerified = false, followersCount = 12000, followingCount = 150)
    )

    private val _subscriptions = MutableStateFlow<List<CreatorSubscription>>(
        listOf(
            CreatorSubscription(
                id = "sub_1",
                creator = initialUsers[0],
                channelTitle = "Alex's Travel Club Insiders",
                tier = "VIP Wanderer",
                monthlyPrice = "$4.99/mo",
                memberSince = "July 2026",
                unreadPostsCount = 4,
                latestPostRes = R.drawable.img_post_travel,
                latestPostTitle = "Unreleased Amalfi 4K Drone Footage & RAW Presets"
            ),
            CreatorSubscription(
                id = "sub_2",
                creator = initialUsers[1],
                channelTitle = "Sophia's Coffee Roastery Lab",
                tier = "Master Barista Tier",
                monthlyPrice = "$2.99/mo",
                memberSince = "August 2026",
                unreadPostsCount = 2,
                latestPostRes = R.drawable.img_post_coffee,
                latestPostTitle = "Secret pour-over temperature curves recipe card"
            ),
            CreatorSubscription(
                id = "sub_3",
                creator = initialUsers[2],
                channelTitle = "Kenji's Tokyo Streets Night Shooters",
                tier = "Cyberpunk Supporter",
                monthlyPrice = "$9.99/mo",
                memberSince = "September 2026",
                unreadPostsCount = 6,
                latestPostRes = R.drawable.img_post_city,
                latestPostTitle = "Night photography LUT package & location map"
            )
        )
    )
    val subscriptions: StateFlow<List<CreatorSubscription>> = _subscriptions.asStateFlow()

    private val _availableAccounts = MutableStateFlow<List<User>>(
        listOf(_currentUser.value) + initialUsers.take(3)
    )
    val availableAccounts: StateFlow<List<User>> = _availableAccounts.asStateFlow()

    private val _posts = MutableStateFlow<List<Post>>(
        listOf(
            Post(
                id = "post_1",
                user = initialUsers[0],
                imageRes = R.drawable.img_post_travel,
                caption = "Golden hour across the Amalfi Coast cliffside 🌊 Pastel houses cascading into the crystal turquoise waters. Truly felt like stepping into an Italian dream.",
                likesCount = 3421,
                commentsCount = 84,
                timeAgo = "2 HOURS AGO",
                isLiked = false,
                isSaved = false,
                location = "Positano, Amalfi Coast"
            ),
            Post(
                id = "post_2",
                user = initialUsers[1],
                imageRes = R.drawable.img_post_coffee,
                caption = "Morning rituals matter ☕ Fresh pour-over latte art with a buttery croissant and film camera ready for today's adventures. What's your morning go-to?",
                likesCount = 2154,
                commentsCount = 42,
                timeAgo = "4 HOURS AGO",
                isLiked = true,
                isSaved = true,
                location = "Saint-Germain, Paris"
            ),
            Post(
                id = "post_3",
                user = initialUsers[2],
                imageRes = R.drawable.img_post_city,
                caption = "Rainy twilight reflections in Shibuya 🇯🇵 The neon glow after the storm brings out a whole new energy. High shutter speed on 35mm lens.",
                likesCount = 5892,
                commentsCount = 138,
                timeAgo = "6 HOURS AGO",
                isLiked = false,
                isSaved = false,
                location = "Shibuya Crossing, Tokyo"
            ),
            Post(
                id = "post_4",
                user = _currentUser.value,
                imageRes = R.drawable.img_user_avatar,
                caption = "Golden hour portrait session behind the lens ✨ Grateful for every creative opportunity and the amazing humans I get to photograph.",
                likesCount = 1290,
                commentsCount = 29,
                timeAgo = "1 DAY AGO",
                isLiked = true,
                isSaved = false,
                location = "Brooklyn, New York"
            )
        )
    )
    val posts: StateFlow<List<Post>> = _posts.asStateFlow()

    private val _stories = MutableStateFlow<List<Story>>(
        listOf(
            Story(
                id = "story_me",
                user = _currentUser.value,
                imageRes = R.drawable.img_user_avatar,
                timeAgo = "Add Story",
                isViewed = false,
                isCloseFriend = false
            ),
            Story(
                id = "story_1",
                user = initialUsers[0],
                imageRes = R.drawable.img_post_travel,
                timeAgo = "18m",
                isViewed = false,
                isCloseFriend = false,
                caption = "Cliffside sunset view 🌅"
            ),
            Story(
                id = "story_2",
                user = initialUsers[1],
                imageRes = R.drawable.img_post_coffee,
                timeAgo = "45m",
                isViewed = false,
                isCloseFriend = true,
                caption = "Best bakery in town! 🥐"
            ),
            Story(
                id = "story_3",
                user = initialUsers[2],
                imageRes = R.drawable.img_post_city,
                timeAgo = "2h",
                isViewed = false,
                isCloseFriend = false,
                caption = "Tokyo after midnight 🌙"
            ),
            Story(
                id = "story_4",
                user = initialUsers[3],
                imageRes = R.drawable.img_user_avatar,
                timeAgo = "4h",
                isViewed = true,
                isCloseFriend = false,
                caption = "Studio session live ✨"
            )
        )
    )
    val stories: StateFlow<List<Story>> = _stories.asStateFlow()

    private val _comments = MutableStateFlow<Map<String, List<Comment>>>(
        mapOf(
            "post_1" to listOf(
                Comment("c1", "post_1", initialUsers[1], "This composition is breathtaking! 😍", "1h", 12, true),
                Comment("c2", "post_1", initialUsers[2], "The colors on the water are unreal 🙌", "45m", 4, false),
                Comment("c3", "post_1", _currentUser.value, "Added to my travel bucket list immediately! ✈️", "20m", 2, false)
            ),
            "post_2" to listOf(
                Comment("c4", "post_2", initialUsers[0], "Nothing beats Parisian pastries 🥐", "3h", 18, false),
                Comment("c5", "post_2", initialUsers[3], "What film camera is that? Looks so classic!", "2h", 6, false)
            ),
            "post_3" to listOf(
                Comment("c6", "post_3", initialUsers[1], "Cyberpunk aesthetic at its absolute finest 🤖✨", "5h", 35, true),
                Comment("c7", "post_3", initialUsers[0], "Such crispy reflections! 📸", "4h", 14, false)
            )
        )
    )
    val comments: StateFlow<Map<String, List<Comment>>> = _comments.asStateFlow()

    private val _reels = MutableStateFlow<List<ReelItem>>(
        listOf(
            ReelItem(
                id = "reel_bruno_major",
                user = initialUsers[1],
                thumbnailRes = R.drawable.img_post_coffee,
                audioTrack = "Bruno Major • Nothing",
                caption = "Tracksuit, some red wine, movies for two 🍷 Couch Sundays with Nintendo & rain outside. 🎶 Bruno Major - Nothing",
                likesCount = 124800,
                commentsCount = 2140,
                sharesCount = 9850,
                isLiked = true
            ),
            ReelItem(
                id = "reel_trial_me",
                user = _currentUser.value,
                thumbnailRes = R.drawable.img_post_city,
                audioTrack = "Maya Sterling • Testing Audio Clip (Original)",
                caption = "Testing new night grading curve 🌃 What do you think of this color palette? Testing with non-followers first!",
                likesCount = 312,
                commentsCount = 48,
                sharesCount = 64,
                isLiked = false,
                isTrialReel = true,
                trialViewsCount = 1840,
                trialNonFollowerPercentage = 93,
                trialDurationHoursLeft = 46
            ),
            ReelItem(
                id = "reel_1",
                user = initialUsers[2],
                thumbnailRes = R.drawable.img_post_city,
                audioTrack = "Kenji Sato • Night Vibes Lo-Fi (Original Audio)",
                caption = "Walking through Tokyo during a rain shower ☔ Neon lights hit different.",
                likesCount = 42800,
                commentsCount = 890,
                sharesCount = 1420,
                isLiked = false
            ),
            ReelItem(
                id = "reel_2",
                user = initialUsers[0],
                thumbnailRes = R.drawable.img_post_travel,
                audioTrack = "Amalfi Coast Summer • Coastal Chill Beats",
                caption = "Waking up to this ocean view in Southern Italy 🇮🇹 Save for your summer trip!",
                likesCount = 85200,
                commentsCount = 1430,
                sharesCount = 3890,
                isLiked = true
            ),
            ReelItem(
                id = "reel_3",
                user = initialUsers[1],
                thumbnailRes = R.drawable.img_post_coffee,
                audioTrack = "Acoustic Morning • Cozy Cafe Sessions",
                caption = "Latte art pour in slow motion ☕ Practice makes progress!",
                likesCount = 19400,
                commentsCount = 310,
                sharesCount = 490,
                isLiked = false
            )
        )
    )
    val reels: StateFlow<List<ReelItem>> = _reels.asStateFlow()

    private val _featuredAudioTrack = MutableStateFlow(
        AudioTrackInfo(
            id = "bruno_major_nothing",
            title = "Nothing",
            artist = "Bruno Major",
            duration = "2:42",
            reelsCount = "1.4M reels",
            isTrending = true,
            albumArtRes = R.drawable.img_post_coffee,
            isSaved = false
        )
    )
    val featuredAudioTrack: StateFlow<AudioTrackInfo> = _featuredAudioTrack.asStateFlow()

    private val _channels = MutableStateFlow<List<BroadcastChannel>>(
        listOf(
            BroadcastChannel(
                id = "chan_1",
                title = "Maya's Visual Studio & Presets 📸",
                description = "Exclusive behind-the-scenes, preset giveaways, and photo walk announcements!",
                creator = _currentUser.value,
                membersCount = 14280,
                avatarRes = R.drawable.img_user_avatar,
                isSubscriberOnly = false,
                messages = listOf(
                    ChannelMessage(
                        id = "cmsg_1",
                        sender = _currentUser.value,
                        text = "Hey everyone! Welcome to my official broadcast channel ✨ Dropping sneak peeks of my upcoming Tokyo photo presets here first!",
                        timestamp = "Yesterday at 4:15 PM",
                        reactions = mapOf("❤️" to 142, "🔥" to 289, "👏" to 95)
                    ),
                    ChannelMessage(
                        id = "cmsg_2",
                        sender = _currentUser.value,
                        text = "Raw before & after from Shibuya during yesterday's rain! Check out how the neon reflections pop with the warm amber tone.",
                        timestamp = "Today at 10:20 AM",
                        mediaRes = R.drawable.img_post_city,
                        isVideo = false,
                        mediaCaption = "Shibuya Crossing • Rainy Twilight Preset #04",
                        reactions = mapOf("❤️" to 310, "🔥" to 542, "👏" to 180)
                    )
                )
            ),
            BroadcastChannel(
                id = "chan_2",
                title = "Travel Insiders Club 🌍",
                description = "Hidden travel spots, itinerary downloads, and flight tips from Alex Rivera.",
                creator = initialUsers[0],
                membersCount = 28400,
                avatarRes = R.drawable.img_post_travel,
                isSubscriberOnly = false,
                messages = listOf(
                    ChannelMessage(
                        id = "cmsg_3",
                        sender = initialUsers[0],
                        text = "Good morning adventurers! Here is a clip from the private cliffside path in Positano 🌊",
                        timestamp = "2 hours ago",
                        mediaRes = R.drawable.img_post_travel,
                        isVideo = true,
                        mediaCaption = "Positano Hidden Cove 4K Clip",
                        reactions = mapOf("❤️" to 420, "🔥" to 612, "👏" to 135)
                    )
                )
            ),
            BroadcastChannel(
                id = "chan_3",
                title = "Specialty Coffee & Roasters ☕",
                description = "Daily latte art techniques, bean origins, and cafe recommendations.",
                creator = initialUsers[1],
                membersCount = 9850,
                avatarRes = R.drawable.img_post_coffee,
                isSubscriberOnly = false,
                messages = listOf(
                    ChannelMessage(
                        id = "cmsg_4",
                        sender = initialUsers[1],
                        text = "Today's Ethiopia Yirgacheffe pour-over notes: peach blossom, bergamot, and honey sweetness 🍯",
                        timestamp = "3 hours ago",
                        mediaRes = R.drawable.img_post_coffee,
                        isVideo = false,
                        mediaCaption = "V60 Pour-over flow rate demo",
                        reactions = mapOf("❤️" to 198, "🔥" to 144, "👏" to 82)
                    )
                )
            )
        )
    )
    val channels: StateFlow<List<BroadcastChannel>> = _channels.asStateFlow()

    private val _conversations = MutableStateFlow<List<ChatConversation>>(
        listOf(
            ChatConversation(
                id = "conv_1",
                user = initialUsers[0],
                lastMessage = "Are you joining the Amalfi photo tour next month?",
                timestamp = "12m",
                unreadCount = 1,
                isOnline = true,
                messages = listOf(
                    ChatMessage("m1", "u1", "Hey Maya! Loved your recent shots from Brooklyn 👏", "10:30 AM", isMine = false),
                    ChatMessage("m2", "user_me", "Thanks Alex! Appreciate it so much 😊", "10:35 AM", isMine = true),
                    ChatMessage("m3", "u1", "Are you joining the Amalfi photo tour next month?", "10:48 AM", isMine = false)
                )
            ),
            ChatConversation(
                id = "conv_2",
                user = initialUsers[1],
                lastMessage = "Shared a photo.",
                timestamp = "1h",
                unreadCount = 0,
                isOnline = false,
                messages = listOf(
                    ChatMessage("m4", "user_me", "That cafe you recommended was fantastic!", "Yesterday", isMine = true),
                    ChatMessage("m5", "u2", "Told you! Best croissants in town! 🥐", "Yesterday", isMine = false)
                )
            ),
            ChatConversation(
                id = "conv_3",
                user = initialUsers[2],
                lastMessage = "Let's definitely shoot together when you're in Tokyo!",
                timestamp = "3h",
                unreadCount = 0,
                isOnline = true,
                messages = listOf(
                    ChatMessage("m6", "u3", "Let's definitely shoot together when you're in Tokyo!", "7:15 AM", isMine = false)
                )
            )
        )
    )
    val conversations: StateFlow<List<ChatConversation>> = _conversations.asStateFlow()

    private val _notifications = MutableStateFlow<List<NotificationItem>>(
        listOf(
            NotificationItem(
                id = "n1",
                user = initialUsers[0],
                type = NotificationType.LIKE,
                text = "liked your photo.",
                timeAgo = "15m",
                postImageRes = R.drawable.img_user_avatar
            ),
            NotificationItem(
                id = "n2",
                user = initialUsers[1],
                type = NotificationType.COMMENT,
                text = "commented: \"This composition is breathtaking! 😍\"",
                timeAgo = "1h",
                postImageRes = R.drawable.img_post_travel
            ),
            NotificationItem(
                id = "n3",
                user = initialUsers[2],
                type = NotificationType.FOLLOW,
                text = "started following you.",
                timeAgo = "3h",
                isFollowedBack = true
            ),
            NotificationItem(
                id = "n4",
                user = initialUsers[3],
                type = NotificationType.LIKE,
                text = "and 48 others liked your reel.",
                timeAgo = "1d",
                postImageRes = R.drawable.img_post_city
            )
        )
    )
    val notifications: StateFlow<List<NotificationItem>> = _notifications.asStateFlow()

    fun toggleLikePost(postId: String) {
        _posts.update { list ->
            list.map { post ->
                if (post.id == postId) {
                    val newLiked = !post.isLiked
                    val newCount = if (newLiked) post.likesCount + 1 else (post.likesCount - 1).coerceAtLeast(0)
                    post.copy(isLiked = newLiked, likesCount = newCount)
                } else post
            }
        }
    }

    fun toggleSavePost(postId: String) {
        _posts.update { list ->
            list.map { post ->
                if (post.id == postId) {
                    post.copy(isSaved = !post.isSaved)
                } else post
            }
        }
    }

    fun toggleLikeReel(reelId: String) {
        _reels.update { list ->
            list.map { reel ->
                if (reel.id == reelId) {
                    val newLiked = !reel.isLiked
                    val newCount = if (newLiked) reel.likesCount + 1 else (reel.likesCount - 1).coerceAtLeast(0)
                    reel.copy(isLiked = newLiked, likesCount = newCount)
                } else reel
            }
        }
    }

    fun addComment(postId: String, text: String) {
        if (text.isBlank()) return
        val newComment = Comment(
            id = "c_${System.currentTimeMillis()}",
            postId = postId,
            user = _currentUser.value,
            text = text.trim(),
            timeAgo = "Just now",
            likesCount = 0,
            isLiked = false
        )
        _comments.update { map ->
            val list = map[postId].orEmpty() + newComment
            map + (postId to list)
        }
        _posts.update { list ->
            list.map { post ->
                if (post.id == postId) {
                    post.copy(commentsCount = post.commentsCount + 1)
                } else post
            }
        }
    }

    fun toggleLikeComment(postId: String, commentId: String) {
        _comments.update { map ->
            val list = map[postId].orEmpty().map { comment ->
                if (comment.id == commentId) {
                    val newLiked = !comment.isLiked
                    val newCount = if (newLiked) comment.likesCount + 1 else (comment.likesCount - 1).coerceAtLeast(0)
                    comment.copy(isLiked = newLiked, likesCount = newCount)
                } else comment
            }
            map + (postId to list)
        }
    }

    fun markStoryViewed(storyId: String) {
        _stories.update { list ->
            list.map { story ->
                if (story.id == storyId) {
                    story.copy(isViewed = true)
                } else story
            }
        }
    }

    fun createPost(
        caption: String,
        @DrawableRes imageRes: Int,
        location: String?,
        filterName: String
    ) {
        val newPost = Post(
            id = "post_${System.currentTimeMillis()}",
            user = _currentUser.value,
            imageRes = imageRes,
            caption = caption,
            likesCount = 1,
            commentsCount = 0,
            timeAgo = "Just now",
            isLiked = false,
            isSaved = false,
            location = location,
            filterName = filterName
        )
        _posts.update { listOf(newPost) + it }
        _currentUser.update { it.copy(postsCount = it.postsCount + 1) }
    }

    fun updateProfile(fullName: String, username: String, bio: String, website: String) {
        _currentUser.update {
            it.copy(
                fullName = fullName,
                username = username,
                bio = bio,
                website = website
            )
        }
    }

    fun sendMessage(conversationId: String, text: String) {
        if (text.isBlank()) return
        val newMsg = ChatMessage(
            id = "m_${System.currentTimeMillis()}",
            senderId = "user_me",
            text = text.trim(),
            timestamp = "Just now",
            isMine = true
        )
        _conversations.update { list ->
            list.map { conv ->
                if (conv.id == conversationId) {
                    conv.copy(
                        lastMessage = text.trim(),
                        timestamp = "Just now",
                        messages = conv.messages + newMsg
                    )
                } else conv
            }
        }
    }

    fun toggleFollowNotificationUser(notificationId: String) {
        _notifications.update { list ->
            list.map { notif ->
                if (notif.id == notificationId) {
                    notif.copy(isFollowedBack = !notif.isFollowedBack)
                } else notif
            }
        }
    }

    private val _playlists = MutableStateFlow<List<Playlist>>(
        listOf(
            Playlist(
                id = "pl_1",
                title = "Amalfi Summer 🌊",
                description = "Coastal Italian dreams, cliffside sunsets, and Mediterranean vibes.",
                coverRes = R.drawable.img_post_travel,
                isPrivate = false,
                items = listOf(
                    PlaylistItem("pli_1", PlaylistItemType.POST, "post_1", "Amalfi Coast Golden Hour", "alex_wander", R.drawable.img_post_travel),
                    PlaylistItem("pli_2", PlaylistItemType.REEL, "reel_2", "Coastal Chill Beats", "alex_wander", R.drawable.img_post_travel),
                    PlaylistItem("pli_3", PlaylistItemType.STORY, "story_1", "Cliffside sunset view", "alex_wander", R.drawable.img_post_travel)
                ),
                createdAt = "2 days ago"
            ),
            Playlist(
                id = "pl_2",
                title = "Tokyo Night Life 🌃",
                description = "Rainy Shibuya reflections, cyberpunk neon, and late night walks.",
                coverRes = R.drawable.img_post_city,
                isPrivate = false,
                items = listOf(
                    PlaylistItem("pli_4", PlaylistItemType.POST, "post_3", "Shibuya Crossing Twilight", "kenji_streets", R.drawable.img_post_city),
                    PlaylistItem("pli_5", PlaylistItemType.REEL, "reel_1", "Night Vibes Lo-Fi", "kenji_streets", R.drawable.img_post_city),
                    PlaylistItem("pli_6", PlaylistItemType.STORY, "story_3", "Tokyo after midnight", "kenji_streets", R.drawable.img_post_city)
                ),
                createdAt = "Last week"
            ),
            Playlist(
                id = "pl_3",
                title = "Secret Moodboard & Inspo 🔒",
                description = "Private aesthetic ideas, cafe interiors, and creative lighting.",
                coverRes = R.drawable.img_post_coffee,
                isPrivate = true,
                items = listOf(
                    PlaylistItem("pli_7", PlaylistItemType.POST, "post_2", "Morning rituals pour-over", "sophia_cafe", R.drawable.img_post_coffee),
                    PlaylistItem("pli_8", PlaylistItemType.REEL, "reel_3", "Latte art in slow motion", "sophia_cafe", R.drawable.img_post_coffee),
                    PlaylistItem("pli_9", PlaylistItemType.STORY, "story_2", "Best bakery in town", "sophia_cafe", R.drawable.img_post_coffee)
                ),
                createdAt = "3 days ago"
            ),
            Playlist(
                id = "pl_4",
                title = "Portrait Archives 📷",
                description = "Golden hour portraits and natural bokeh lighting studies.",
                coverRes = R.drawable.img_user_avatar,
                isPrivate = false,
                items = listOf(
                    PlaylistItem("pli_10", PlaylistItemType.POST, "post_4", "Brooklyn Portrait Session", "maya_lens", R.drawable.img_user_avatar),
                    PlaylistItem("pli_11", PlaylistItemType.STORY, "story_4", "Studio session live", "elena_creative", R.drawable.img_user_avatar)
                ),
                createdAt = "Yesterday"
            )
        )
    )
    val playlists: StateFlow<List<Playlist>> = _playlists.asStateFlow()

    fun createPlaylist(
        title: String,
        description: String,
        isPrivate: Boolean,
        @DrawableRes coverRes: Int,
        initialItem: PlaylistItem? = null
    ): Playlist {
        val newPlaylist = Playlist(
            id = "pl_${System.currentTimeMillis()}",
            title = title.trim(),
            description = description.trim(),
            coverRes = coverRes,
            isPrivate = isPrivate,
            items = if (initialItem != null) listOf(initialItem) else emptyList(),
            createdAt = "Just now"
        )
        _playlists.update { listOf(newPlaylist) + it }
        return newPlaylist
    }

    fun togglePlaylistPrivacy(playlistId: String) {
        _playlists.update { list ->
            list.map { pl ->
                if (pl.id == playlistId) {
                    pl.copy(isPrivate = !pl.isPrivate)
                } else pl
            }
        }
    }

    fun addItemToPlaylist(playlistId: String, item: PlaylistItem) {
        _playlists.update { list ->
            list.map { pl ->
                if (pl.id == playlistId) {
                    if (pl.items.any { it.sourceId == item.sourceId && it.type == item.type }) {
                        pl
                    } else {
                        pl.copy(items = pl.items + item)
                    }
                } else pl
            }
        }
    }

    fun removeItemFromPlaylist(playlistId: String, itemId: String) {
        _playlists.update { list ->
            list.map { pl ->
                if (pl.id == playlistId) {
                    pl.copy(items = pl.items.filterNot { it.id == itemId })
                } else pl
            }
        }
    }

    fun deletePlaylist(playlistId: String) {
        _playlists.update { list -> list.filterNot { it.id == playlistId } }
    }

    fun switchAccount(userId: String) {
        val target = _availableAccounts.value.firstOrNull { it.id == userId }
        if (target != null) {
            _currentUser.value = target
        }
    }

    fun addAccount(
        username: String,
        fullName: String,
        bio: String,
        @DrawableRes avatarRes: Int
    ): User {
        val cleanUsername = username.trim().removePrefix("@")
        val newAccount = User(
            id = "user_${System.currentTimeMillis()}",
            username = cleanUsername,
            fullName = fullName.trim(),
            avatarRes = avatarRes,
            isVerified = false,
            bio = bio.trim(),
            website = "",
            followersCount = 1,
            followingCount = 0,
            postsCount = 0,
            isFollowing = false
        )
        _availableAccounts.update { it + newAccount }
        _currentUser.value = newAccount
        return newAccount
    }

    fun createReel(
        title: String,
        description: String,
        @DrawableRes thumbnailRes: Int,
        audioTrack: String,
        isTrialReel: Boolean = false
    ): ReelItem {
        val fullCaption = if (title.isNotBlank()) {
            "🎬 $title\n${description.trim()}"
        } else {
            description.trim()
        }

        val newReel = ReelItem(
            id = "reel_${System.currentTimeMillis()}",
            user = _currentUser.value,
            thumbnailRes = thumbnailRes,
            audioTrack = audioTrack.ifBlank { "${_currentUser.value.username} • Original Audio" },
            caption = fullCaption,
            likesCount = 1,
            commentsCount = 0,
            sharesCount = 0,
            isLiked = false,
            isTrialReel = isTrialReel,
            trialViewsCount = if (isTrialReel) 1 else 0,
            trialNonFollowerPercentage = 95,
            trialDurationHoursLeft = 48
        )
        _reels.update { listOf(newReel) + it }
        return newReel
    }

    fun graduateTrialReel(reelId: String) {
        _reels.update { list ->
            list.map { reel ->
                if (reel.id == reelId) {
                    reel.copy(
                        isTrialReel = false,
                        isGraduatedFromTrial = true
                    )
                } else reel
            }
        }
    }

    fun createBroadcastChannel(
        title: String,
        description: String,
        isSubscriberOnly: Boolean,
        @DrawableRes avatarRes: Int
    ): BroadcastChannel {
        val newChan = BroadcastChannel(
            id = "chan_${System.currentTimeMillis()}",
            title = title.trim(),
            description = description.trim(),
            creator = _currentUser.value,
            membersCount = 1,
            avatarRes = avatarRes,
            isSubscriberOnly = isSubscriberOnly,
            messages = listOf(
                ChannelMessage(
                    id = "cmsg_${System.currentTimeMillis()}",
                    sender = _currentUser.value,
                    text = "Welcome to $title! 🎉 Thank you for joining my channel. Stay tuned for updates!",
                    timestamp = "Just now",
                    reactions = mapOf("❤️" to 1, "🔥" to 1, "👏" to 1)
                )
            ),
            isJoined = true
        )
        _channels.update { listOf(newChan) + it }
        return newChan
    }

    fun sendChannelMessage(
        channelId: String,
        text: String,
        @DrawableRes mediaRes: Int? = null,
        isVideo: Boolean = false,
        mediaCaption: String? = null
    ) {
        if (text.isBlank() && mediaRes == null) return
        val newMsg = ChannelMessage(
            id = "cmsg_${System.currentTimeMillis()}",
            sender = _currentUser.value,
            text = text.trim(),
            timestamp = "Just now",
            mediaRes = mediaRes,
            isVideo = isVideo,
            mediaCaption = mediaCaption,
            reactions = mapOf("❤️" to 0, "🔥" to 0, "👏" to 0)
        )
        _channels.update { list ->
            list.map { chan ->
                if (chan.id == channelId) {
                    chan.copy(messages = chan.messages + newMsg)
                } else chan
            }
        }
    }

    fun toggleChannelReaction(channelId: String, messageId: String, emoji: String) {
        _channels.update { list ->
            list.map { chan ->
                if (chan.id == channelId) {
                    val updatedMessages = chan.messages.map { msg ->
                        if (msg.id == messageId) {
                            val userHasReacted = msg.userReactions.contains(emoji)
                            val currentCount = msg.reactions[emoji] ?: 0
                            val newCount = if (userHasReacted) (currentCount - 1).coerceAtLeast(0) else currentCount + 1
                            val newMap = msg.reactions.toMutableMap().apply { put(emoji, newCount) }
                            val newSet = if (userHasReacted) msg.userReactions - emoji else msg.userReactions + emoji
                            msg.copy(reactions = newMap, userReactions = newSet)
                        } else msg
                    }
                    chan.copy(messages = updatedMessages)
                } else chan
            }
        }
    }

    fun deactivateCurrentUser(isPermanentDeletion: Boolean = true) {
        val updated = _currentUser.value.copy(
            isDeactivated = true,
            isScheduledForDeletion = isPermanentDeletion,
            deletionDaysLeft = 51,
            deletionDate = "December 1, 2026"
        )
        _currentUser.value = updated
        _availableAccounts.update { list ->
            list.map { if (it.id == updated.id) updated else it }
        }
    }

    fun reactivateCurrentUser() {
        val updated = _currentUser.value.copy(
            isDeactivated = false,
            isScheduledForDeletion = false
        )
        _currentUser.value = updated
        _availableAccounts.update { list ->
            list.map { if (it.id == updated.id) updated else it }
        }
    }

    fun toggleChannelTermination(terminated: Boolean) {
        val updated = _currentUser.value.copy(
            isChannelTerminated = terminated,
            copyrightStrikesCount = if (terminated) 3 else 0,
            hasContentRemoved = terminated
        )
        _currentUser.value = updated
        _availableAccounts.update { list ->
            list.map { if (it.id == updated.id) updated else it }
        }
    }

    fun submitCounterNotification(strikeId: String) {
        _copyrightStrikes.update { list ->
            list.map {
                if (it.id == strikeId) it.copy(statusText = "Counter-Notification Submitted (Under Legal Review)")
                else it
            }
        }
    }
}
