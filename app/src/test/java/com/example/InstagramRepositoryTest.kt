package com.example

import com.example.data.model.PlaylistItem
import com.example.data.model.PlaylistItemType
import com.example.data.repository.InstagramRepository
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class InstagramRepositoryTest {

    private lateinit var repository: InstagramRepository

    @Before
    fun setUp() {
        repository = InstagramRepository()
    }

    @Test
    fun testToggleLikePost() {
        val initialPosts = repository.posts.value
        val firstPost = initialPosts.first()
        val initialLiked = firstPost.isLiked
        val initialCount = firstPost.likesCount

        repository.toggleLikePost(firstPost.id)

        val updatedPost = repository.posts.value.first { it.id == firstPost.id }
        assertEquals(!initialLiked, updatedPost.isLiked)
        val expectedCount = if (!initialLiked) initialCount + 1 else initialCount - 1
        assertEquals(expectedCount, updatedPost.likesCount)
    }

    @Test
    fun testToggleSavePost() {
        val firstPost = repository.posts.value.first()
        val initialSaved = firstPost.isSaved

        repository.toggleSavePost(firstPost.id)

        val updatedPost = repository.posts.value.first { it.id == firstPost.id }
        assertEquals(!initialSaved, updatedPost.isSaved)
    }

    @Test
    fun testAddComment() {
        val firstPost = repository.posts.value.first()
        val initialCommentCount = firstPost.commentsCount

        repository.addComment(firstPost.id, "Stunning view!")

        val updatedPost = repository.posts.value.first { it.id == firstPost.id }
        assertEquals(initialCommentCount + 1, updatedPost.commentsCount)

        val comments = repository.comments.value[firstPost.id].orEmpty()
        assertTrue(comments.any { it.text == "Stunning view!" })
    }

    @Test
    fun testCreatePost() {
        val initialCount = repository.posts.value.size

        repository.createPost(
            caption = "Test caption #sample",
            imageRes = R.drawable.img_post_travel,
            location = "Positano",
            filterName = "Clarendon"
        )

        val newPosts = repository.posts.value
        assertEquals(initialCount + 1, newPosts.size)
        assertEquals("Test caption #sample", newPosts.first().caption)
        assertEquals("Clarendon", newPosts.first().filterName)
    }

    @Test
    fun testSendChatMessage() {
        val initialConv = repository.conversations.value.first()
        val initialMsgCount = initialConv.messages.size

        repository.sendMessage(initialConv.id, "Are you there?")

        val updatedConv = repository.conversations.value.first { it.id == initialConv.id }
        assertEquals(initialMsgCount + 1, updatedConv.messages.size)
        assertEquals("Are you there?", updatedConv.lastMessage)
    }

    @Test
    fun testPlaylistPublicAndPrivateCreation() {
        val initialCount = repository.playlists.value.size

        // Create Public Playlist
        val publicPlaylist = repository.createPlaylist(
            title = "Travel Memories",
            description = "Public adventure series",
            isPrivate = false,
            coverRes = R.drawable.img_post_travel
        )
        assertFalse(publicPlaylist.isPrivate)
        assertEquals("Travel Memories", publicPlaylist.title)

        // Create Private Playlist
        val privatePlaylist = repository.createPlaylist(
            title = "Secret Moodboard",
            description = "Private ideas",
            isPrivate = true,
            coverRes = R.drawable.img_post_coffee
        )
        assertTrue(privatePlaylist.isPrivate)

        assertEquals(initialCount + 2, repository.playlists.value.size)
    }

    @Test
    fun testTogglePlaylistPrivacy() {
        val playlist = repository.playlists.value.first()
        val initialPrivacy = playlist.isPrivate

        repository.togglePlaylistPrivacy(playlist.id)

        val updated = repository.playlists.value.first { it.id == playlist.id }
        assertEquals(!initialPrivacy, updated.isPrivate)
    }

    @Test
    fun testAddPostReelStoryToPlaylist() {
        val playlist = repository.playlists.value.first()
        val initialItemCount = playlist.items.size

        // Add Post
        val postItem = PlaylistItem("test_p1", PlaylistItemType.POST, "src_post_99", "Cool Post", "user_1", R.drawable.img_post_travel)
        repository.addItemToPlaylist(playlist.id, postItem)

        // Add Reel
        val reelItem = PlaylistItem("test_r1", PlaylistItemType.REEL, "src_reel_99", "Awesome Reel", "user_2", R.drawable.img_post_city)
        repository.addItemToPlaylist(playlist.id, reelItem)

        // Add Story
        val storyItem = PlaylistItem("test_s1", PlaylistItemType.STORY, "src_story_99", "Sunset Story", "user_3", R.drawable.img_post_coffee)
        repository.addItemToPlaylist(playlist.id, storyItem)

        val updated = repository.playlists.value.first { it.id == playlist.id }
        assertEquals(initialItemCount + 3, updated.items.size)
        assertTrue(updated.items.any { it.type == PlaylistItemType.POST })
        assertTrue(updated.items.any { it.type == PlaylistItemType.REEL })
        assertTrue(updated.items.any { it.type == PlaylistItemType.STORY })
    }

    @Test
    fun testRemoveItemAndDeletPlaylist() {
        val playlist = repository.playlists.value.first()
        val firstItem = playlist.items.first()

        repository.removeItemFromPlaylist(playlist.id, firstItem.id)

        val updated = repository.playlists.value.first { it.id == playlist.id }
        assertFalse(updated.items.any { it.id == firstItem.id })

        // Delete playlist
        val countBeforeDelete = repository.playlists.value.size
        repository.deletePlaylist(playlist.id)
        assertEquals(countBeforeDelete - 1, repository.playlists.value.size)
        assertFalse(repository.playlists.value.any { it.id == playlist.id })
    }

    @Test
    fun testSwitchAccountAndAddAccount() {
        val initialUser = repository.currentUser.value
        val initialAccountsCount = repository.availableAccounts.value.size

        // Add a new account
        val newAccount = repository.addAccount(
            username = "creative_nomad",
            fullName = "Jordan Reed",
            bio = "Wanderlust photographer",
            avatarRes = R.drawable.img_user_avatar
        )

        assertEquals("creative_nomad", newAccount.username)
        assertEquals(newAccount.id, repository.currentUser.value.id)
        assertEquals(initialAccountsCount + 1, repository.availableAccounts.value.size)

        // Switch back to initial user
        repository.switchAccount(initialUser.id)
        assertEquals(initialUser.id, repository.currentUser.value.id)
    }

    @Test
    fun testCreateReelWithTitleAndDescription() {
        val initialReelsCount = repository.reels.value.size

        val newReel = repository.createReel(
            title = "Tokyo Rain Reflections",
            description = "Moody neon lights after sunset #tokyo #cinematic",
            thumbnailRes = R.drawable.img_post_city,
            audioTrack = "Night Beats • Lo-Fi"
        )

        val updatedReels = repository.reels.value
        assertEquals(initialReelsCount + 1, updatedReels.size)
        assertEquals(newReel.id, updatedReels.first().id)
        assertTrue(updatedReels.first().caption.contains("Tokyo Rain Reflections"))
        assertTrue(updatedReels.first().caption.contains("Moody neon lights after sunset"))
        assertEquals("Night Beats • Lo-Fi", updatedReels.first().audioTrack)
    }
}
