package com.nikolai.finstagram.data

import android.arch.lifecycle.LiveData
import com.google.android.gms.tasks.Task
import com.nikolai.finstagram.models.Message

interface MessagesRepository {
    fun getChats(uid:String): LiveData<List<Message>>
    fun getMessages(currentuid: String, uid:String, limit: Int) : LiveData<List<Message>>
    fun sendMessage(currentuid: String, uid: String ,message: Message): Task<Unit>
    fun createChat(currentuid: String, uid: String)
    fun setReadMessage(currentuid: String, uid: String, msgId: String) : Task<Unit>
}