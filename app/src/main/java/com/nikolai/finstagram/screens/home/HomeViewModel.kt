package com.nikolai.finstagram.screens.home

import android.arch.lifecycle.LiveData
import com.nikolai.finstagram.common.SingleLiveEvent
import com.nikolai.finstagram.data.FeedPostsRepository
import com.nikolai.finstagram.data.common.map
import com.nikolai.finstagram.models.FeedPost
import com.nikolai.finstagram.screens.common.BaseViewModel
import com.google.android.gms.tasks.OnFailureListener
import com.nikolai.finstagram.data.UsersRepository
import com.nikolai.finstagram.models.User

class HomeViewModel(onFailureListener: OnFailureListener,
                    private val feedPostsRepo: FeedPostsRepository, private val usersRepo: UsersRepository) : BaseViewModel(onFailureListener) {
    lateinit var uid: String
    lateinit var feedPosts: LiveData<List<FeedPost>>
    lateinit var users : LiveData<List<User>>
    private var loadedLikes = mapOf<String, LiveData<FeedPostLikes>>()
    private val _goToCommentsScreen = SingleLiveEvent<String>()
    val goToCommentsScreen = _goToCommentsScreen

    fun init(uid: String) {
        if (!this::uid.isInitialized) {
            this.uid = uid
            feedPosts = feedPostsRepo.getFeedPosts(uid).map {
                it.sortedByDescending { it.timestampDate() }
            }
            users = usersRepo.getUsers().map { it.sortedByDescending {it.uid } }
        }
    }

    fun toggleLike(postId: String) {
        feedPostsRepo.toggleLike(postId, uid).addOnFailureListener(onFailureListener)
    }

    fun getLikes(postId: String): LiveData<FeedPostLikes>? = loadedLikes[postId]

    fun loadLikes(postId: String): LiveData<FeedPostLikes> {
        val existingLoadedLikes = loadedLikes[postId]
        if (existingLoadedLikes == null) {
            val liveData = feedPostsRepo.getLikes(postId).map { likes ->
                FeedPostLikes(
                        likesCount = likes.size,
                        likedByUser = likes.find { it.userId == uid } != null)
            }
            loadedLikes += postId to liveData
            return liveData
        } else {
            return existingLoadedLikes
        }
    }

    fun openComments(postId: String) {
        _goToCommentsScreen.value = postId
    }

    fun onDirectClick() {
        // open direct message page here.

    }
}