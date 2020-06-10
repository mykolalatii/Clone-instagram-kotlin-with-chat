package com.nikolai.finstagram.screens.chat

import android.arch.lifecycle.LiveData
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.Task
import com.nikolai.finstagram.common.toUnit
import com.nikolai.finstagram.data.ChatsRepository
import com.nikolai.finstagram.data.MessagesRepository
import com.nikolai.finstagram.data.UsersRepository
import com.nikolai.finstagram.models.Message
import com.nikolai.finstagram.models.User
import com.nikolai.finstagram.screens.common.BaseViewModel

class ChatViewModel(onFailureListener: OnFailureListener, private val usersRepo: UsersRepository, private val messagesRepo: MessagesRepository, private val  chatsRepo: ChatsRepository) : BaseViewModel(onFailureListener) {
    lateinit var messages : LiveData<List<Message>>


    fun sendMessage(uid: String, msg : Message) : Task<Unit> {
        chatsRepo.updateLastMessage(uid, usersRepo.currentUid()!!, msg.message!!)
        return messagesRepo.sendMessage(usersRepo.currentUid()!!, uid , msg).toUnit()
    }

    fun loadMessages(uid: String, limit: Int){
        messages = messagesRepo.getMessages(usersRepo.currentUid()!!, uid, limit)
    }

    fun getUser() : LiveData<User> {
        return  usersRepo.getUser()
    }
}