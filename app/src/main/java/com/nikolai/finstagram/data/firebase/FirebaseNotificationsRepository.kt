package com.nikolai.finstagram.data.firebase

import android.arch.lifecycle.LiveData
import com.nikolai.finstagram.common.toUnit
import com.nikolai.finstagram.data.NotificationsRepository
import com.nikolai.finstagram.data.common.map
import com.nikolai.finstagram.data.firebase.common.FirebaseLiveData
import com.nikolai.finstagram.data.firebase.common.database
import com.nikolai.finstagram.models.Notification
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DataSnapshot

class FirebaseNotificationsRepository : NotificationsRepository {
    override fun createNotification(uid: String, notification: Notification): Task<Unit> =
            notificationsRef(uid).push().setValue(notification).toUnit()

    override fun getNotifications(uid: String): LiveData<List<Notification>> =
            FirebaseLiveData(notificationsRef(uid)).map {
                it.children.map { it.asNotification()!! }
            }

    override fun setNotificationsRead(uid: String, ids: List<String>, read: Boolean): Task<Unit> {
        val updatesMap = ids.map { "$it/read" to read }.toMap()
        return notificationsRef(uid).updateChildren(updatesMap).toUnit()
    }

    private fun notificationsRef(uid: String) =
            database.child("notifications").child(uid)

    private fun DataSnapshot.asNotification() =
            getValue(Notification::class.java)?.copy(id = key)
}