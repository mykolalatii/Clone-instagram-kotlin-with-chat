package com.nikolai.finstagram.screens

import android.app.Application
import com.nikolai.finstagram.common.firebase.FirebaseAuthManager
import com.nikolai.finstagram.data.firebase.*
import com.nikolai.finstagram.screens.notifications.NotificationsCreator
import com.nikolai.finstagram.screens.search.SearchPostsCreator

class FinstagramApp : Application() {
    val usersRepo by lazy { FirebaseUsersRepository() }
    val feedPostsRepo by lazy { FirebaseFeedPostsRepository() }
    val notificationsRepo by lazy { FirebaseNotificationsRepository() }
    val authManager by lazy { FirebaseAuthManager() }
    val searchRepo by lazy { FirebaseSearchRepository() }
    val messagesRepo by lazy { FirebaseMessagesRepository() }
    val chatsRepo by lazy {FirebaseChatsRepository()}
    val igUsersRepo by lazy { FirebaseIGUsersRepository() }

    override fun onCreate() {
        super.onCreate()
        NotificationsCreator(notificationsRepo, usersRepo, feedPostsRepo)
        SearchPostsCreator(searchRepo)
    }
}