package com.nikolai.finstagram.data.firebase

import android.arch.lifecycle.LiveData
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DataSnapshot
import com.nikolai.finstagram.common.toUnit
import com.nikolai.finstagram.data.IGUsersRepository
import com.nikolai.finstagram.data.common.map
import com.nikolai.finstagram.data.firebase.common.database
import com.nikolai.finstagram.data.firebase.common.liveData
import com.nikolai.finstagram.models.IGUser

class FirebaseIGUsersRepository : IGUsersRepository {
    override fun getIgUser(username: String): LiveData<IGUser> {
        return database.child("ig_users").child(username).liveData().map {
            it.asIGUser()!!
        }
    }

    override fun createIGUser(igUser: IGUser): Task<Unit> {
        return database.child("ig_users").child(igUser.username).setValue(igUser).toUnit()
    }

    private fun DataSnapshot.asIGUser(): IGUser? =
            getValue(IGUser::class.java)?.copy(username = key)
}