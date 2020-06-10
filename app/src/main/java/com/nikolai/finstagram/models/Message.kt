package com.nikolai.finstagram.models

import com.google.firebase.database.Exclude
import com.google.firebase.database.ServerValue
import java.util.*

data class Message(val message: String? =  null, val type: String? = null, val time : Any = ServerValue.TIMESTAMP, val seen : Boolean = false, val from : String? = "", @Exclude val msgId: String = "") {
    fun timestampDate(): Date = Date(time as Long)
}