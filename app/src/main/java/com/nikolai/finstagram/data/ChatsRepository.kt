package com.nikolai.finstagram.data

import android.arch.lifecycle.LiveData
import com.google.android.gms.tasks.Task
import com.nikolai.finstagram.models.Chat

interface ChatsRepository {
    fun createChat(uid: String, chat : Chat) : Task<Unit>
    fun getChats(uid: String) : LiveData<List<Chat>>
    fun setLastMessageSeen(uid: String, cid : String) : Task<Unit>
    fun updateLastMessage(uid: String,cid: String, message: String) : Task<Unit>
    fun updateChatProfilePicture(url: String, uid: String, cid: String) : Task<Unit>
}