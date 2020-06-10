package com.nikolai.finstagram.screens.home

import android.arch.lifecycle.Observer
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.View
import com.nikolai.finstagram.R
import com.nikolai.finstagram.screens.comments.CommentsActivity
import com.nikolai.finstagram.screens.common.BaseActivity
import com.nikolai.finstagram.screens.common.setupAuthGuard
import com.nikolai.finstagram.screens.common.setupBottomNavigation
import com.nikolai.finstagram.screens.directmessage.DirectMessageActivity
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : BaseActivity(), FeedAdapter.Listener, StoryAdapter.Listener, View.OnClickListener {
    private lateinit var mAdapter: FeedAdapter
    private lateinit var mStoryAdapter: StoryAdapter
    private lateinit var mViewModel: HomeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        Log.d(TAG, "onCreate")

        mAdapter = FeedAdapter(this)
        feed_recycler.adapter = mAdapter
        feed_recycler.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        feed_recycler.isNestedScrollingEnabled = false

        mStoryAdapter = StoryAdapter(this)
        recycler_view_story_section.adapter = mStoryAdapter
        recycler_view_story_section.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recycler_view_story_section.isNestedScrollingEnabled = false

        direct.setOnClickListener(this)

        setupAuthGuard { uid ->
            setupBottomNavigation(uid, 0)
            mViewModel = initViewModel()
            mViewModel.init(uid)
            mViewModel.feedPosts.observe(this, Observer {
                it?.let {
                    mAdapter.updatePosts(it)
                }
            })
            mViewModel.goToCommentsScreen.observe(this, Observer {
                it?.let { postId ->
                    CommentsActivity.start(this, postId)
                }
            })
            mViewModel.users.observe(this, Observer { it?.let {
                mStoryAdapter.updateList(it)
            } })
        }
    }

    override fun toggleLike(postId: String) {
        Log.d(TAG, "toggleLike: $postId")
        mViewModel.toggleLike(postId)
    }

    override fun loadLikes(postId: String, position: Int) {
        if (mViewModel.getLikes(postId) == null) {
            mViewModel.loadLikes(postId).observe(this, Observer {
                it?.let { postLikes ->
                    mAdapter.updatePostLikes(position, postLikes)
                }
            })
        }
    }

    override fun openComments(postId: String) {
        mViewModel.openComments(postId)
    }

    companion object {
        const val TAG = "HomeActivity"
    }

    override fun onItemClick(index: Int) {
        TODO("Not yet implemented")
    }

    override fun onClick(v: View) {
        when(v.id) {
            R.id.direct -> {
                val intent = Intent(this, DirectMessageActivity::class.java)
                startActivity(intent)
            }
        }
     }
}