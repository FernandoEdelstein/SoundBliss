package com.soundbliss.Fragments

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.soundbliss.MainActivity
import com.soundbliss.R

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


//evento al click del bottone per la registrazione
        registBtn.setOnClickListener{
            var email: String = emailUser.text.toString()
            var pass: String = passUser.text.toString()
            var user: String = userName.text.toString()
            var name: String = nameUser.text.toString()
            var lastName: String = lastNameUser.text.toString()

            if(TextUtils.isEmpty(email)||TextUtils.isEmpty(pass)){
                Toast.makeText(this, "Please fill all the fields",Toast.LENGTH_LONG).show()
            } else{
                auth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(this, OnCompleteListener { task ->
                    if(task.isSuccessful){
                        Toast.makeText(this, "Successfully Registered", Toast.LENGTH_LONG).show()
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    }else {
                        Toast.makeText(this, "Registration Failed", Toast.LENGTH_LONG).show()
                    }
                });
            }
        }
        //per tornare nell'activity di login
        backLog.setOnClickListener{
            val intent = Intent(this, LogIn::class.java)
            startActivity(intent)
            finish()
        }
    }
}