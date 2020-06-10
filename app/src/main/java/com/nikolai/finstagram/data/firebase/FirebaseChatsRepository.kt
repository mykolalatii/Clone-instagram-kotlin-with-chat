package com.nikolai.finstagram.data.firebase

import android.arch.lifecycle.LiveData
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ServerValue
import com.nikolai.finstagram.common.Event
import com.nikolai.finstagram.common.EventBus
import com.nikolai.finstagram.common.toUnit
import com.nikolai.finstagram.data.ChatsRepository
import com.nikolai.finstagram.data.common.map
import com.nikolai.finstagram.data.firebase.common.FirebaseLiveData
import com.nikolai.finstagram.data.firebase.common.database
import com.nikolai.finstagram.models.Chat

class FirebaseChatsRepository : ChatsRepository {
    override fun createChat(uid: String, chat : Chat): Task<Unit> {
        val reference = database.child("chats").child(uid).child(chat.cid)
        return reference.setValue(chat).toUnit().addOnSuccessListener {
            EventBus.publish(Event.CreateChat(chat))
        }
    }

    override fun getChats(uid: String): LiveData<List<Chat>> {
        return FirebaseLiveData(database.child("chats").child(uid)).map { it.children.map { it.asChat()!! } }
    }

    override fun setLastMessageSeen(uid: String, cid : String): Task<Unit> {
        val updatesMap = mutableMapOf<String, Any?>()
        updatesMap["seen"] = true
        return database.child("chats").child(uid).child(cid).updateChildren(updatesMap).toUnit()
    }

    override fun updateLastMessage(uid: String, cid: String, message: String): Task<Unit> {
        val updatesMap = mutableMapOf<String, Any?>()
        updatesMap["last_message"] = message
        updatesMap["time"] = ServerValue.TIMESTAMP
        updatesMap["seen"] = false
        return database.child("chats").child(uid).child(cid).updateChildren(updatesMap).toUnit()
    }

    override fun updateChatProfilePicture(url: String, uid: String, cid: String): Task<Unit> {
        return database.child("chats/${uid}/${cid}/profile_picture").setValue(url).toUnit()
    }

    private fun DataSnapshot.asChat() : Chat? = getValue(Chat::class.java)?.copy(cid = key)
}