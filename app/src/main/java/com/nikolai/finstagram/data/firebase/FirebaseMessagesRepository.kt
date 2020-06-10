package com.nikolai.finstagram.data.firebase

import android.arch.lifecycle.LiveData
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DataSnapshot
import com.nikolai.finstagram.common.toUnit
import com.nikolai.finstagram.data.MessagesRepository
import com.nikolai.finstagram.data.common.map
import com.nikolai.finstagram.data.firebase.common.FirebaseLiveData
import com.nikolai.finstagram.data.firebase.common.database
import com.nikolai.finstagram.data.firebase.common.liveData
import com.nikolai.finstagram.models.Message

class FirebaseMessagesRepository : MessagesRepository{
    override fun getChats(uid: String): LiveData<List<Message>> {
        return  database.child("messages").child(uid).liveData().map { it.children.map { it.asMessage()!! } }
    }

    override fun getMessages(currentuid: String, uid: String, limit: Int): LiveData<List<Message>> {
        return FirebaseLiveData(database.child("messages").child(currentuid).child(uid).orderByChild("time").limitToLast(limit)).map { it.children.map { it.asMessage()!! } }
    }

    override fun sendMessage(currentuid: String, uid: String ,message: Message): Task<Unit> {
        val current_user_ref = "${currentuid}/${uid}"
        val chat_user_ref =  "${uid}/${currentuid}"

        val user_message_push = database.child("messages").child(currentuid).child(uid).push();
        val updatesMap = mutableMapOf<String, Any?>()
        updatesMap["message"] = message.message
        updatesMap["seen"] = message.seen
        updatesMap["type"] = message.type
        updatesMap["time"] = message.time
        updatesMap["from"] = message.from
        val key = user_message_push.key
        val msgMap = mutableMapOf<String, Any?>()
        msgMap["${current_user_ref}/${key}"] = updatesMap
        msgMap["${chat_user_ref}/${key}"] = updatesMap
        return database.child("messages").updateChildren(msgMap).toUnit()
    }

    override fun createChat(currentuid: String, uid: String) {
        database.child("messages").child(currentuid).child(uid).push()
    }

    override fun setReadMessage(currentuid: String, uid: String, msgId: String): Task<Unit> {
        val updatesMap = mutableMapOf<String, Any?>()
        updatesMap["seen"] = true
        return database.child("messages").child(currentuid).child(uid).child(msgId).updateChildren(updatesMap).toUnit()
    }

    private fun DataSnapshot.asMessage(): Message? =
            getValue(Message::class.java)?.copy(msgId = key)
}