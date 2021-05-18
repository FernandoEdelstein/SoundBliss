package com.soundbliss

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.soundbliss.Adapters.PostAdapter
import com.soundbliss.PostFragments.PhotoVideoFragment
import com.soundbliss.PostFragments.RequestFragment
import com.soundbliss.PostFragments.TrackFragment
import kotlinx.android.synthetic.main.activity_main.bottom_navigation

class PostActivity : AppCompatActivity() {

    private val StoragePermissionCode = 1

    private lateinit var closeButton : ImageView
    private lateinit var fragmentManager: FragmentManager
    private lateinit var adapter: PostAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post)

        val trackFrag = TrackFragment()
        val photoFrag = PhotoVideoFragment()
        val requestFrag = RequestFragment()

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




}
