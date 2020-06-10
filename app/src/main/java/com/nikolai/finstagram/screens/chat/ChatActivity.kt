package com.nikolai.finstagram.screens.chat

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import com.google.firebase.database.ServerValue
import com.nikolai.finstagram.R
import com.nikolai.finstagram.models.Message
import com.nikolai.finstagram.screens.common.BaseActivity
import com.nikolai.finstagram.screens.common.loadUserPhoto
import com.nikolai.finstagram.screens.common.setupAuthGuard
import kotlinx.android.synthetic.main.activity_chat.*

class ChatActivity : BaseActivity(), View.OnClickListener , ChatAdapter.Listener{

    private lateinit var mViewModel: ChatViewModel
    private lateinit var mAdapter: ChatAdapter
    private var limit = 10
    private lateinit var uid : String
    private lateinit var currentuid : String
    var insta_name = ""
    var insta_username = ""
    var profile_pic_url = ""
    var isVerified = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        send.setOnClickListener(this)
        camera.setOnClickListener(this)
        back_image.setOnClickListener(this)
        layout_attach_icons.visibility = View.VISIBLE
        send.visibility = View.GONE

        input.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(s: Editable?) {
                if(input.text.isEmpty()) {
                    layout_attach_icons.visibility = View.VISIBLE
                    send.visibility = View.GONE
                } else {
                    layout_attach_icons.visibility = View.GONE
                    send.visibility = View.VISIBLE
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

        })

        mAdapter = ChatAdapter(this)
        chat_list.adapter = mAdapter
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        layoutManager.stackFromEnd = true
        chat_list.layoutManager = layoutManager
        setupAuthGuard {currentuid ->
            this.currentuid = currentuid
            mViewModel = initViewModel()
            uid = intent.getStringExtra("user_id")
            insta_name =intent.getStringExtra("ig_name")
            insta_username = intent.getStringExtra("ig_username")
            profile_pic_url = intent.getStringExtra("picUrl")
            isVerified = intent.getBooleanExtra("isVerified", false)
            mViewModel.loadMessages(uid, limit)

            full_name.text = insta_name
            username.text = insta_username
            profile_image.loadUserPhoto(profile_pic_url)
            if(isVerified) {
                verified.visibility = View.VISIBLE
            } else {
                verified.visibility = View.INVISIBLE
            }
            mAdapter.setProfilePicURL(profile_pic_url)

            mViewModel.messages.observe(this, Observer { it?.let {
                mAdapter.updateList(it, currentuid)
                chat_list.scrollToPosition(it.size - 1)
            } })
        }
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.send -> {
                if (validateMessage()) {
                    val message = Message(input.text.toString(), "text", ServerValue.TIMESTAMP , false, currentuid, "")
                    mViewModel.sendMessage(uid, message)
                    input.text.clear()
                }
            }
            R.id.back_image -> {
                finish()
            }
        }
    }

    fun validateMessage():Boolean {
        return !input.text.isEmpty()
    }

    override fun onItemClick(index: Int) {
        TODO("Not yet implemented")
    }
}