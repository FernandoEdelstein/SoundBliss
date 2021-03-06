package com.soundbliss


import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*
import com.soundbliss.Fragments.PeopleChatFragment


import kotlinx.android.synthetic.main.activity_chat.*

class ChatActivity : AppCompatActivity() {

    private lateinit var firestoreDb: FirebaseFirestore

    private lateinit var auth: FirebaseAuth
    private lateinit var currentUser: DocumentReference

    override fun onDestroy() {
        super.onDestroy()
        overridePendingTransition(R.anim.slide_from_left,R.anim.slide_to_right)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        this.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        auth = FirebaseAuth.getInstance()
        firestoreDb = FirebaseFirestore.getInstance()
        currentUser  = firestoreDb.collection("users").document(auth.currentUser!!.uid)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = intent.getStringExtra("username")

        makeCurrentFragment(PeopleChatFragment())

        navigation.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.navigation_people -> {
                    makeCurrentFragment(PeopleChatFragment())
                    true
                }
                else -> false
            }
        }
    }


    private fun makeCurrentFragment(fragment: androidx.fragment.app.Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragment_layout, fragment)
            commit()
        }

}







