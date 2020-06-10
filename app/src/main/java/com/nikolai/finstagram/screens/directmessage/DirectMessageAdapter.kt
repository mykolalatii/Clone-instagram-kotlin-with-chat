package com.nikolai.finstagram.screens.directmessage

import android.graphics.Typeface
import android.support.v4.content.ContextCompat
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.nikolai.finstagram.R
import com.nikolai.finstagram.models.Chat
import com.nikolai.finstagram.screens.common.SimpleCallback
import com.nikolai.finstagram.screens.common.loadUserPhoto
import kotlinx.android.synthetic.main.message_contact_item.view.*

class DirectMessageAdapter (val listener: Listener) : RecyclerView.Adapter<DirectMessageAdapter.ViewHolder>() {

    interface Listener {
        fun onItemClick(chat : Chat)
    }

    private var friends = listOf<Chat>()

    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.message_contact_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return  friends.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val chat = friends.get(position)
        with(holder.view) {
            user_image.loadUserPhoto(chat.profile_picture)
            user_name.text = chat.fake_name
            last_message.text = chat.last_message
            if (!chat.seen) {
                last_message.setTypeface(last_message.typeface, Typeface.BOLD)
                last_message.setTextColor(ContextCompat.getColor(context, R.color.black))
                blue_dot.visibility = View.VISIBLE
            } else {
                last_message.setTypeface(last_message.typeface, Typeface.NORMAL)
                last_message.setTextColor(ContextCompat.getColor(context, android.R.color.darker_gray))
                blue_dot.visibility = View.INVISIBLE
            }
            if (chat.verified) verified.visibility = View.VISIBLE
            else verified.visibility = View.INVISIBLE
            this.setOnClickListener{
                listener.onItemClick(friends[position])
            }
        }
    }

    fun updateList(newList : List<Chat>) {
        val diff = DiffUtil.calculateDiff(SimpleCallback(this.friends, newList){it.cid})
        this.friends = newList
        diff.dispatchUpdatesTo(this)
    }
}