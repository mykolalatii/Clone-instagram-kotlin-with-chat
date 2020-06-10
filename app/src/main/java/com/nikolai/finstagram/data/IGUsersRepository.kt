package com.nikolai.finstagram.data

import android.arch.lifecycle.LiveData
import com.google.android.gms.tasks.Task
import com.nikolai.finstagram.models.IGUser

interface IGUsersRepository {
    fun getIgUser(username : String) : LiveData<IGUser>
    fun createIGUser(igUser: IGUser) : Task<Unit>
}