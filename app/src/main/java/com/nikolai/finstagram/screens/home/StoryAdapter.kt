package com.nikolai.finstagram.screens.home

import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.nikolai.finstagram.R
import com.nikolai.finstagram.models.User
import com.nikolai.finstagram.screens.common.SimpleCallback
import com.nikolai.finstagram.screens.common.loadUserPhoto
import kotlinx.android.synthetic.main.story_item.view.*

class StoryAdapter(val listener : Listener) : RecyclerView.Adapter<StoryAdapter.ViewHolder>() {

    interface Listener {
        fun onItemClick(index : Int)
    }

    private var users = listOf<User>()

    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.story_item, parent, false)
        return StoryAdapter.ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return if (users.isEmpty()) 0
        else users.size + 1
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder.view) {
            if (position == 0) {
                story_image.loadUserPhoto(null)
                story_user_name.text = "your story"
            } else {
                val user = users.get(position - 1)
                story_image.loadUserPhoto(user.photo)
                story_user_name.text = user.name
            }
        }
    }

    fun updateList(newList : List<User>) {
        val diff = DiffUtil.calculateDiff(SimpleCallback(this.users, newList){it.uid})
        this.users = newList
        diff.dispatchUpdatesTo(this)
    }
}