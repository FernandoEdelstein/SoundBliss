package com.soundbliss

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.soundbliss.PostFragments.PhotoVideoFragment
import com.soundbliss.PostFragments.RequestFragment
import com.soundbliss.PostFragments.TrackFragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.bottom_navigation
import kotlinx.android.synthetic.main.activity_post.*
import kotlinx.android.synthetic.main.fragment_photovideo.*
import org.w3c.dom.Text


class PostActivity : AppCompatActivity() {

    private val StoragePermissionCode = 1

    private lateinit var closeButton : ImageView
    private lateinit var postButton : TextView
    private lateinit var uploadTrackButton : Button
    private lateinit var fragmentManager: FragmentManager

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

        postButton = findViewById(R.id.post)

        postButton.setOnClickListener{
            if(photoFrag.isVisible && photoFrag != null){
                photoFrag.uploadImage()
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }else if(trackFrag.isVisible && trackFrag != null){

                startActivity(Intent(this, MainActivity::class.java))
                finish()

            }else if(requestFrag.isVisible && requestFrag != null){

                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
        }



    }


    fun pickAudioFile(){
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "audio/*"
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        startActivityForResult(Intent.createChooser(intent,"Choose Audio File"), 111)
    }

    private fun makeCurrentFragment(fragment:Fragment) = supportFragmentManager.beginTransaction().apply{
        replace (R.id.fragment_container,fragment)
        commit()
    }


}
