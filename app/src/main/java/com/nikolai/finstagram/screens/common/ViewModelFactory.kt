package com.nikolai.finstagram.screens.common

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.nikolai.finstagram.screens.FinstagramApp
import com.nikolai.finstagram.screens.addfriends.AddFriendsViewModel
import com.nikolai.finstagram.screens.comments.CommentsViewModel
import com.nikolai.finstagram.screens.editprofile.EditProfileViewModel
import com.nikolai.finstagram.screens.home.HomeViewModel
import com.nikolai.finstagram.screens.login.LoginViewModel
import com.nikolai.finstagram.screens.notifications.NotificationsViewModel
import com.nikolai.finstagram.screens.profile.ProfileViewModel
import com.nikolai.finstagram.screens.profilesettings.ProfileSettingsViewModel
import com.nikolai.finstagram.screens.register.RegisterViewModel
import com.nikolai.finstagram.screens.search.SearchViewModel
import com.nikolai.finstagram.screens.share.ShareViewModel
import com.google.android.gms.tasks.OnFailureListener
import com.nikolai.finstagram.screens.chat.ChatViewModel
import com.nikolai.finstagram.screens.directmessage.DirectMessageViewModel

@Suppress("UNCHECKED_CAST")
class ViewModelFactory(private val app: FinstagramApp,
                       private val commonViewModel: CommonViewModel,
                       private val onFailureListener: OnFailureListener) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        val usersRepo = app.usersRepo
        val feedPostsRepo = app.feedPostsRepo
        val authManager = app.authManager
        val notificationsRepo = app.notificationsRepo
        val searchRepo = app.searchRepo
        val messagesRepo = app.messagesRepo
        val chatsRepo = app.chatsRepo
        val igUsersRepo = app.igUsersRepo

        if (modelClass.isAssignableFrom(AddFriendsViewModel::class.java)) {
            return AddFriendsViewModel(onFailureListener, usersRepo, feedPostsRepo) as T
        } else if (modelClass.isAssignableFrom(EditProfileViewModel::class.java)) {
            return EditProfileViewModel(onFailureListener, usersRepo) as T
        } else if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return HomeViewModel(onFailureListener, feedPostsRepo, usersRepo) as T
        } else if (modelClass.isAssignableFrom(ProfileSettingsViewModel::class.java)) {
            return ProfileSettingsViewModel(authManager, onFailureListener) as T
        } else if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(authManager, app, commonViewModel, onFailureListener, usersRepo) as T
        } else if (modelClass.isAssignableFrom(ProfileViewModel::class.java)) {
            return ProfileViewModel(usersRepo, onFailureListener) as T
        } else if (modelClass.isAssignableFrom(RegisterViewModel::class.java)) {
            return RegisterViewModel(commonViewModel, app, onFailureListener, usersRepo) as T
        } else if (modelClass.isAssignableFrom(ShareViewModel::class.java)) {
            return ShareViewModel(feedPostsRepo, usersRepo, onFailureListener) as T
        } else if (modelClass.isAssignableFrom(CommentsViewModel::class.java)) {
            return CommentsViewModel(feedPostsRepo, usersRepo, onFailureListener) as T
        } else if (modelClass.isAssignableFrom(NotificationsViewModel::class.java)) {
            return NotificationsViewModel(notificationsRepo, onFailureListener) as T
        } else if (modelClass.isAssignableFrom(SearchViewModel::class.java)) {
            return SearchViewModel(searchRepo, onFailureListener) as T
        } else if (modelClass.isAssignableFrom(DirectMessageViewModel::class.java)){
            return DirectMessageViewModel(onFailureListener, usersRepo, chatsRepo, igUsersRepo) as T
        } else if (modelClass.isAssignableFrom(ChatViewModel::class.java)){
            return ChatViewModel(onFailureListener, usersRepo, messagesRepo, chatsRepo) as T
        } else {
            error("Unknown view model class $modelClass")
        }
    }
}