package com.nikolai.finstagram.models

import com.google.firebase.database.Exclude
import com.google.firebase.database.ServerValue
import java.util.*

data class Chat(val time : Any = ServerValue.TIMESTAMP, val seen : Boolean = true, @Exclude val cid : String = "",
                val fake_name : String = "", val real_name : String = "",
                val last_message : String = "", val profile_picture : String = "",
                val verified : Boolean = false) {
    fun timestampDate() = Date(time as Long)
}