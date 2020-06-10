package com.nikolai.finstagram.screens.chat

import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.nikolai.finstagram.R
import com.nikolai.finstagram.models.Message
import com.nikolai.finstagram.screens.common.SimpleCallback
import com.nikolai.finstagram.screens.common.loadUserPhoto
import kotlinx.android.synthetic.main.chat_sent_item.view.*
import kotlinx.android.synthetic.main.message_contact_item.view.*

class ChatAdapter (val listener: Listener) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    interface Listener {
        fun onItemClick(index : Int)
    }

    private var messages = listOf<Message>()
    private lateinit var uid: String
    private var friendProfilePic = ""

    class SentViewHolder(val view: View) : RecyclerView.ViewHolder(view)
    class ReceiveViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    override fun getItemViewType(position: Int): Int {
        val message = messages[position]
        return when(message.from) {
            uid -> R.layout.chat_sent_item
            else -> R.layout.chat_received_item
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val sentViewHolder = LayoutInflater.from(parent.context).inflate(R.layout.chat_sent_item, parent, false)
        val receivedViewHolder = LayoutInflater.from(parent.context).inflate(R.layout.chat_received_item, parent, false)
        return when(viewType) {
            R.layout.chat_sent_item -> {
                SentViewHolder(sentViewHolder)
            }

            R.layout.chat_received_item -> {
                ReceiveViewHolder(receivedViewHolder)
            }
            else -> throw IllegalArgumentException("Unsupported layout")
        }
    }

    override fun getItemCount(): Int {
        return  messages.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val msg = messages.get(position)
        when(holder) {
            is SentViewHolder -> {
                with(holder.itemView) {
                    message.text = msg.message
                }
            }

            is ReceiveViewHolder -> {
                with(holder.itemView) {
                    message.text = msg.message
                    if (!friendProfilePic.isEmpty())
                        user_image.loadUserPhoto(friendProfilePic)
                }
            }
        }
    }

    fun setProfilePicURL(url : String) {
        friendProfilePic = url
    }

    fun updateList(newList : List<Message>, uid:String) {
        this.uid = uid
        val diff = DiffUtil.calculateDiff(SimpleCallback(this.messages, newList){it.msgId})
        this.messages = newList
        diff.dispatchUpdatesTo(this)
    }
}