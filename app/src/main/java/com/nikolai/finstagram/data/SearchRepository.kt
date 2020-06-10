package com.nikolai.finstagram.data

import android.arch.lifecycle.LiveData
import com.nikolai.finstagram.models.SearchPost
import com.google.android.gms.tasks.Task

interface SearchRepository {
    fun searchPosts(text: String): LiveData<List<SearchPost>>
    fun createPost(post: SearchPost): Task<Unit>
}