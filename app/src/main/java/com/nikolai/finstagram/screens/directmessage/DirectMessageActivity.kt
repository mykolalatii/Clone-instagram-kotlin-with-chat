package com.nikolai.finstagram.screens.directmessage

import android.app.AlertDialog
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Observer
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.TextInputEditText
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.View
import android.widget.Toast
import com.nikolai.finstagram.R
import com.nikolai.finstagram.common.Event
import com.nikolai.finstagram.common.EventBus
import com.nikolai.finstagram.models.Chat
import com.nikolai.finstagram.models.IGUser
import com.nikolai.finstagram.models.User
import com.nikolai.finstagram.screens.chat.ChatActivity
import com.nikolai.finstagram.screens.common.BaseActivity
import com.nikolai.finstagram.screens.common.setupAuthGuard
import kotlinx.android.synthetic.main.activity_direct_message.*
import kotlinx.android.synthetic.main.activity_direct_message.video
import kotlinx.android.synthetic.main.activity_share.back_image
import okhttp3.*
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException

class DirectMessageActivity : BaseActivity() , View.OnClickListener , DirectMessageAdapter.Listener{

    private lateinit var mAdapter : DirectMessageAdapter
    private var isFakeEntered = false
    private var fakeFriend = ""
    private var realFriend = ""
    private lateinit var mViewModel : DirectMessageViewModel
    private var friend : LiveData<User?>? = null
    private var igFriend : LiveData<IGUser>? = null

    var realFriendId = ""
    var client = OkHttpClient()
    var insta_name = ""
    var profile_pic_url = ""
    var isVerified = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_direct_message)
        back_image.setOnClickListener(this)
        video.setOnClickListener(this)
        send_message.setOnClickListener(this)

        mAdapter = DirectMessageAdapter(this)
        contact_recycler.layoutManager = LinearLayoutManager(this)
        contact_recycler.adapter = mAdapter

        setupAuthGuard {uid ->
            mViewModel = initViewModel()
            mViewModel.init(uid)
            mViewModel.chats.observe(this, Observer {it?.let {
                mAdapter.updateList(it)
            }})
            mViewModel.getUser().observe(this, Observer {it?.let {
                username_text.text = it.name
            }})
        }

        EventBus.events.observe(this, Observer { it?.let {event ->
            when(event) {
                is Event.CreateChat -> {
                    //goToChat(event.chat.cid)
                    realFriendId = event.chat.cid
                    getIGUser(fakeFriend)
                    //getFakeUserProfilePicture(fakeFriend)
                }
            }
        }})
    }

    override fun onClick(v: View) {
        when(v.id) {
            R.id.back_image -> {
                this.finish()
            }

            R.id.video -> {
                isFakeEntered = false
                openDialog(!isFakeEntered)
            }

            R.id.send_message -> {
                val intent = Intent(this, AddIGUserActivity::class.java)
                startActivity(intent)
            }
        }
    }

    override fun onItemClick(chat: Chat) {
        realFriend = chat.real_name
        realFriendId = chat.cid
        fakeFriend = chat.fake_name
        getIGUser(chat.fake_name)
        if(!chat.seen)
            mViewModel.setSeenLastMessage(chat.cid)
    }

    fun openDialog(isFake : Boolean) {
        val builder = AlertDialog.Builder(this)
        val inflater = layoutInflater
        if (isFake)
            builder.setTitle("Enter your fake friend name")
        else builder.setTitle("Enter your real friend name")
        val dialogLayout = inflater.inflate(R.layout.dialog_input_name, null)
        val editText  = dialogLayout.findViewById<TextInputEditText>(R.id.ig_name_input)
        builder.setView(dialogLayout)
        builder.setPositiveButton("OK") { dialogInterface, i ->
            if (isFake) {
                isFakeEntered = true
                fakeFriend = editText.text.toString()
                dialogInterface.dismiss()
                openDialog(false)
            } else {
                realFriend = editText.text.toString()
                friend = mViewModel.getUserBy(realFriend)
                friend?.observe(this, Observer { it?.let {
                        mViewModel.createChat(fakeFriend, realFriend, it)
                        if (!hud.isShowing) hud.show()
                    }
                    if (it == null) openAlertMessage("Please enter correct username of your partner")
                })
            }
        }
        builder.show()
    }

    fun goToChatWithIgUser(chat : IGUser) {
        var intent = Intent(this, ChatActivity::class.java)
        intent.putExtra("user_id", realFriendId)
        intent.putExtra("ig_name", chat.full_name)
        intent.putExtra("ig_username", chat.username)
        intent.putExtra("picUrl", chat.profile_picture)
        intent.putExtra("isVerified", chat.verified)
        friend?.removeObservers(this)
        igFriend?.removeObservers(this)
        startActivity(intent)
    }

    fun openAlertMessage(msg : String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Can't find your partner")
        builder.setMessage(msg)
        builder.setPositiveButton("OK"){dialog, which -> }
        builder.create().show()
    }

    fun getIGUser(username: String) {
        igFriend = mViewModel.getIGUser(username)
        igFriend?.observe(this, Observer {
            if(hud.isShowing) hud.dismiss()
            if(it != null){
                mViewModel.updateChatProfilePicture(it.profile_picture, realFriendId)
                goToChatWithIgUser(it)
            }
            else Toast.makeText(this, "Can't find Instagram user", Toast.LENGTH_SHORT).show()
        })
    }

    fun getFakeUserProfilePicture(username: String) {
        //val regex = Regex("^(?!.*\\.\\.)(?!.*\\.\$)[^\\W][\\w.]{0,29}\$")
        val url = "https://www.instagram.com/${username}"//?__a=1"
        val httpBuilder = url.toHttpUrlOrNull()?.newBuilder()
        httpBuilder?.addQueryParameter("__a", "1")
        val request = Request.Builder()
                .url(httpBuilder!!.build())
                .build()
        try {
            val response = client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: java.io.IOException) {
                    Log.d("Fail", e.localizedMessage)
                    if(hud.isShowing) hud.dismiss()
                    Toast.makeText(this@DirectMessageActivity, "Instagram username is not valid", Toast.LENGTH_LONG)
                }

                override fun onResponse(call: Call, response: Response) {
                    try {
                        val responseString = response.body?.string()
                        val jsonObject = JSONObject(responseString).getJSONObject("graphql").getJSONObject("user")
                        JSONObject(responseString).getJSONObject("graphql").getJSONObject("user").getBoolean("is_verified")
                        JSONObject(responseString).getJSONObject("graphql").getJSONObject("user").getString("profile_pic_url")
                        insta_name = jsonObject.getString("full_name")
                        profile_pic_url = jsonObject.getString("profile_pic_url")
                        isVerified = jsonObject.getBoolean("is_verified")

                        runOnUiThread{
                           // goToChat(realFriendId)
                            if(hud.isShowing) hud.dismiss()
                        }

                    } catch (e : JSONException) {
                        Log.d("Response-profile", e.localizedMessage)
                        if(hud.isShowing) hud.dismiss()
                        runOnUiThread {
                            Toast.makeText(this@DirectMessageActivity, "Instagram username is not valid", Toast.LENGTH_LONG).show()
                        }
                    }
                }

            })
        } catch (e : IOException) {

        }
    }
}
