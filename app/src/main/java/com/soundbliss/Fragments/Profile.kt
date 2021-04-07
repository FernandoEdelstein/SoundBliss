package com.soundbliss.Fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.media.Image
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.soundbliss.MainActivity
import com.soundbliss.R

class Profile : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private var databaseReference: DatabaseReference? = null
    private lateinit var database : FirebaseDatabase
    private lateinit var buttonLogOut: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        buttonLogOut = findViewById(R.id.buttonLogOut)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        databaseReference = Firebase.database("https://soundbliss-8ba73-default-rtdb.europe-west1.firebasedatabase.app").reference;
        databaseReference?.child("rvmF9RWAXzbjgg5FHEkaX9oHLDt1")

        loadProfile()

        buttonLogOut.setOnClickListener {
            auth.signOut()
            val intent = Intent(this, LogIn::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun loadProfile(){
        val user = auth.currentUser
        val userReference = databaseReference?.child(user?.uid!!)

        userReference?.addValueEventListener(object: ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {

                val usernameText = findViewById<TextView>(R.id.usernameText)
                val nameText = findViewById<TextView>(R.id.nameText)
                val lastnameText = findViewById<TextView>(R.id.lastnameText)
                usernameText.text =  snapshot.child("user").value?.toString()
                nameText.text=  snapshot.child("name").value?.toString()
                lastnameText.text=  snapshot.child("lastname").value?.toString()

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })

//    logoutButton.setOnClickListener {
//        auth.signOut()
//        startActivity(Intent(this, LogIn::class.java))
//        finish()
//    }
    }
}