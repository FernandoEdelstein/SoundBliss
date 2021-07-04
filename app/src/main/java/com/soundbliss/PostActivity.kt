package com.soundbliss

import android.content.Intent
import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.soundbliss.Adapters.PostAdapter
import com.soundbliss.PostFragments.PhotoFragment
import com.soundbliss.PostFragments.RequestFragment
import com.soundbliss.PostFragments.TrackFragment
import kotlinx.android.synthetic.main.activity_main.bottom_navigation

class PostActivity : AppCompatActivity() {

    private val StoragePermissionCode = 1

    private lateinit var closeButton : ImageView
    private lateinit var fragmentManager: FragmentManager
    private lateinit var adapter: PostAdapter

    private var userid = ""
    private var username = ""
    private var userpic = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post)

        this.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        userid = intent.getStringExtra("userId")!!
        username = intent.getStringExtra("username")!!
        userpic = intent.getStringExtra("userPic")!!


        val trackFrag = TrackFragment(userid,username,userpic)
        val photoFrag = PhotoFragment(userid,username,userpic)
        val requestFrag = RequestFragment(userid,username,userpic)

        makeCurrentFragment(requestFrag)
        bottom_navigation.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.up_request -> makeCurrentFragment(requestFrag)
                R.id.up_track -> makeCurrentFragment(trackFrag)
                R.id.up_photo -> makeCurrentFragment(photoFrag)
            }
            true
        }

        closeButton = findViewById(R.id.close)

        closeButton.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

    }

    private fun makeCurrentFragment(fragment:Fragment) = supportFragmentManager.beginTransaction().apply{
        replace (R.id.fragment_container,fragment)
        commit()
    }

    fun getUserid(): String {
        return userid
    }

    fun getUserPic(): String {
        return userpic
    }

    fun getUserName(): String {
        return username
    }

}
