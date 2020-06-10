package com.nikolai.finstagram.screens.directmessage

import android.os.Bundle
import android.view.View
import com.nikolai.finstagram.R
import com.nikolai.finstagram.models.IGUser
import com.nikolai.finstagram.screens.common.BaseActivity
import kotlinx.android.synthetic.main.activity_add_i_g_user.*

class AddIGUserActivity : BaseActivity() , View.OnClickListener {

    private lateinit var mViewModel : DirectMessageViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_i_g_user)
        add_btn.setOnClickListener(this)

        mViewModel = initViewModel()
    }

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.add_btn -> {
                val fullName = full_name.text.toString()
                val userName = username.text.toString()
                val url = profile_picture.text.toString()
                val id = insta_id.text.toString()
                val isVerified = verified.isChecked
                val igUser = IGUser(userName, fullName, isVerified, url, id)
                mViewModel.createIgUser(igUser)
            }
        }
    }
}
