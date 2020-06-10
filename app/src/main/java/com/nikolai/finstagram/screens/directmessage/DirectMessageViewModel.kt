package com.nikolai.finstagram.screens.directmessage

import android.arch.lifecycle.LiveData
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.Task
import com.nikolai.finstagram.common.SingleLiveEvent
import com.nikolai.finstagram.data.ChatsRepository
import com.nikolai.finstagram.data.IGUsersRepository
import com.nikolai.finstagram.data.UsersRepository
import com.nikolai.finstagram.data.common.map
import com.nikolai.finstagram.models.Chat
import com.nikolai.finstagram.models.IGUser
import com.nikolai.finstagram.models.User
import com.nikolai.finstagram.screens.common.BaseViewModel

class DirectMessageViewModel(onFailureListener: OnFailureListener, private val usersRepo: UsersRepository, private val chatsRepo: ChatsRepository, private val igUsersRepo: IGUsersRepository) : BaseViewModel(onFailureListener)  {
    lateinit var chats : LiveData<List<Chat>>
    lateinit var uid: String
    private val _goToChat = SingleLiveEvent<String>()
    val goToChat = _goToChat
    fun init(uid : String) {
        if (!this::uid.isInitialized) {
            this.uid = uid
            chats = chatsRepo.getChats(uid)
        }
    }

    fun getUser() : LiveData<User> {
        return  usersRepo.getUser()
    }

    fun getUserBy(username : String) : LiveData<User?> {
        return usersRepo.getUserBy(username).map {
            if (!it?.isEmpty()!!)
                it.first()
            else null
        }
    }

    fun createChat(fake : String, real : String, user : User) : Task<Unit> {
        val chat = Chat(seen = false,cid = user.uid, fake_name = fake, real_name = real)
        return chatsRepo.createChat(uid, chat)
    }

    fun getIGUser(username : String) : LiveData<IGUser> {
        return igUsersRepo.getIgUser(username)
    }

    fun updateChatProfilePicture(url : String, uid: String) {
        chatsRepo.updateChatProfilePicture(url, usersRepo.currentUid()!!, uid)
    }

    fun setSeenLastMessage(cid : String) : Task<Unit> {
        return chatsRepo.setLastMessageSeen(usersRepo.currentUid()!!, cid)
    }

    fun createIgUser(igUser: IGUser) : Task<Unit> {
        return igUsersRepo.createIGUser(igUser)
    }
}
