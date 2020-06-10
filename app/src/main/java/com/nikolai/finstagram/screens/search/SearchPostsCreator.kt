package com.nikolai.finstagram.screens.search

import android.arch.lifecycle.Observer
import android.util.Log
import com.nikolai.finstagram.common.BaseEventListener
import com.nikolai.finstagram.common.Event
import com.nikolai.finstagram.common.EventBus
import com.nikolai.finstagram.data.SearchRepository
import com.nikolai.finstagram.models.SearchPost

class SearchPostsCreator(searchRepo: SearchRepository) : BaseEventListener() {
    init {
        EventBus.events.observe(this, Observer {
            it?.let { event ->
                when (event) {
                    is Event.CreateFeedPost -> {
                        val searchPost = with(event.post) {
                            SearchPost(
                                    image = image,
                                    caption = caption,
                                    postId = id)
                        }
                        searchRepo.createPost(searchPost).addOnFailureListener {
                            Log.d(TAG, "Failed to create search post for event: $event", it)
                        }
                    }
                    else -> {
                    }
                }
            }
        })
    }

    companion object {
        const val TAG = "SearchPostsCreator"
    }
}