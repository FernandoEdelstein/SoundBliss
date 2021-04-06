package com.soundbliss.Fragments

import android.content.Intent
import android.os.Build.ID
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserInfo
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.soundbliss.MainActivity
import com.soundbliss.R
import org.w3c.dom.Text

class SignUp : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var dbReference: DatabaseReference
    private lateinit var firebaseDatabase: FirebaseDatabase

    private lateinit var emailUser: EditText
    private lateinit var passUser: EditText
    private lateinit var nameUser : EditText
    private lateinit var lastNameUser: EditText
    private lateinit var userName: EditText
    private lateinit var registBtn: Button
    private lateinit var backLog: Button








    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        auth = FirebaseAuth.getInstance()
        emailUser = findViewById(R.id.emailField)
        passUser = findViewById(R.id.passField)
        nameUser = findViewById(R.id.nameField)
        lastNameUser = findViewById(R.id.lastnameField)
        userName = findViewById(R.id.usernameField)
        registBtn = findViewById(R.id.registerButton)
        backLog = findViewById(R.id.backLogButton)


        firebaseDatabase = FirebaseDatabase.getInstance()
        dbReference = Firebase.database("https://soundbliss-8ba73-default-rtdb.europe-west1.firebasedatabase.app").reference;
        dbReference.child("rvmF9RWAXzbjgg5FHEkaX9oHLDt1").setValue("First users")

        register()

        //per tornare nell'activity di login
        backLog.setOnClickListener {
            val intent = Intent(this, LogIn::class.java)
            startActivity(intent)
            finish()
        }
    }




    //evento al click del bottone per la registrazione
    private fun register(){
        registBtn.setOnClickListener{
            var email: String = emailUser.text.toString()
            var pass: String = passUser.text.toString()
            var user: String = userName.text.toString()
            var name: String = nameUser.text.toString()
            var lastName: String = lastNameUser.text.toString()

            if(TextUtils.isEmpty(email)||TextUtils.isEmpty(pass)||
                    TextUtils.isEmpty(user)|| TextUtils.isEmpty(name) || TextUtils.isEmpty(lastName)){
                Toast.makeText(this, "Please fill all the fields",Toast.LENGTH_LONG).show()
                Log.d(this.toString(), "You didn't write anything")
            } else {
                auth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(this, OnCompleteListener { task ->
                    if (task.isSuccessful ) {
                        Log.d("Main", "is a test: ")
                        val currentUser = auth.currentUser
                        val currentDb = dbReference.child((currentUser?.uid!!))
                        currentDb.child("name").setValue(name)
                        currentDb.child("lastname").setValue(lastName)
                        currentDb.child("user").setValue(user)
                        currentDb.child("email").setValue(email)
                        currentDb.child("pass").setValue(pass)

                        Toast.makeText(this, "Successfully Registered ", Toast.LENGTH_LONG).show()
                        val intent = Intent(this, Profile::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(this, "Registration Failed", Toast.LENGTH_LONG).show()
                    }
                })
            }
        }
    }

















}