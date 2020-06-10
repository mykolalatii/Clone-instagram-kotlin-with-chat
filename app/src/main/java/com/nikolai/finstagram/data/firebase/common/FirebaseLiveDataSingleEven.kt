package com.nikolai.finstagram.data.firebase.common

import android.arch.lifecycle.LiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.Query
import com.nikolai.finstagram.common.ValueEventListenerAdapter

class FirebaseLiveDataSingleEven(private val query: Query) : LiveData<DataSnapshot>() {
    private val listener = ValueEventListenerAdapter {
        value = it
    }

    override fun onActive() {
        super.onActive()
        query.addListenerForSingleValueEvent(listener)
    }

    override fun onInactive() {
        super.onInactive()
        query.removeEventListener(listener)
    }
}