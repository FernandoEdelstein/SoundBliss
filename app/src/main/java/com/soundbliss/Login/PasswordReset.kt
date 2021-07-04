package com.soundbliss.Login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.soundbliss.R

class PasswordReset : AppCompatActivity() {

    private lateinit var backBtn : Button
    private lateinit var sendBtn : Button
    private lateinit var mailText : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_password_reset)

        backBtn = findViewById(R.id.forgotPassBack)
        sendBtn = findViewById(R.id.forgotPassSend)
        mailText = findViewById(R.id.forgotPassMail)

        backBtn.setOnClickListener {
            startActivity(Intent(this,LogIn::class.java))
            overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right)
            finish()
        }

        sendBtn.setOnClickListener {
            resetPassword()
            startActivity(Intent(this,LogIn::class.java))
            overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right)
            finish()
        }
    }

    private fun resetPassword(){
        if(mailText.text.isBlank())
            Toast.makeText(this, R.string.fillMailField, Toast.LENGTH_SHORT).show()
        if(!Patterns.EMAIL_ADDRESS.matcher(mailText.text).matches())
            Toast.makeText(this, R.string.provideValidEmail, Toast.LENGTH_SHORT).show()

        FirebaseAuth.getInstance().sendPasswordResetEmail(mailText.text.toString())
            .addOnSuccessListener {
                Toast.makeText(this, R.string.checkEmail,Toast.LENGTH_SHORT).show()

        }.addOnFailureListener {
                Toast.makeText(this, R.string.errorEmail,Toast.LENGTH_SHORT).show()
            }
    }
}