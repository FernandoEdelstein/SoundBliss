package com.soundbliss.Login

import android.content.Intent
import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.soundbliss.Fragments.ProfileFragment
import com.soundbliss.MainActivity
import com.soundbliss.Model.User
import com.soundbliss.R
import kotlin.collections.HashMap

class SignUp : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    //private lateinit var dbReference: DatabaseReference
    //private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var emailUser: EditText
    private lateinit var passUser: EditText
    private lateinit var nameUser : EditText
    private lateinit var lastNameUser: EditText
    private lateinit var userName: EditText
    private lateinit var registBtn: Button
    private lateinit var backLog: Button


    //Database
    private lateinit var firestoreDb : FirebaseFirestore
    private lateinit var document: DocumentReference
    private lateinit var id: String





    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        this.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        auth = FirebaseAuth.getInstance()
        emailUser = findViewById(R.id.emailField)
        passUser = findViewById(R.id.passField)
        nameUser = findViewById(R.id.nameField)
        lastNameUser = findViewById(R.id.lastnameField)
        userName = findViewById(R.id.usernameField)
        registBtn = findViewById(R.id.registerButton)
        backLog = findViewById(R.id.backLogButton)

        firestoreDb = FirebaseFirestore.getInstance()

//evento al click del bottone per la registrazione
        registBtn.setOnClickListener{

            //Disable button so that it can't be clicked multiple times causing the system to register multiple instances of the same user
            registBtn.isEnabled = false

            var email: String = emailUser.text.toString()
            var pass: String = passUser.text.toString()
            var user: String = userName.text.toString()
            var name: String = nameUser.text.toString()
            var lastName: String = lastNameUser.text.toString()



            if(TextUtils.isEmpty(email)||TextUtils.isEmpty(pass) || TextUtils.isEmpty(user)||TextUtils.isEmpty(name) || TextUtils.isEmpty(lastName)){
                Toast.makeText(this, R.string.FillAllFields,Toast.LENGTH_LONG).show()
            } else{

                auth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(this, OnCompleteListener { task ->
                    // Re enable button
                    registBtn.isEnabled = true

                    if(task.isSuccessful){
                        Toast.makeText(this, R.string.SuccessRegister, Toast.LENGTH_LONG).show()
                        //val intent = Intent(this, MainActivity::class.java)
                        val intent = Intent(applicationContext, MainActivity::class.java)

                        id = auth.currentUser!!.uid
                        //val userIdentity = User (name, lastName, user, auth.currentUser!!.uid,"",email,"")

                        val utenti = User(
                            name,
                            lastName,
                            user,
                            id,
                            "",
                            email,
                            ""
                        )
                        firestoreDb.collection("users").document(id).set(utenti)
                            .addOnSuccessListener { Log.d("Ok", "Registration success!") }
                            .addOnFailureListener { e -> Log.w("Error", "Error writing user", e) }
                        startActivity(intent)
                        finish()
                    }else {
                        Toast.makeText(this, R.string.FailRegister, Toast.LENGTH_LONG).show()
                    }
                });
            }
        }
        //per tornare nell'activity di login
        backLog.setOnClickListener{
            val intent = Intent(this, LogIn::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_from_left,R.anim.slide_to_right)
            finish()
        }
    }

}