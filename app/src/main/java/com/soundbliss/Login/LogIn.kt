package com.soundbliss.Login

import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.soundbliss.MainActivity
import com.soundbliss.R


class LogIn : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var emailUserText: EditText
    private lateinit var passwordUserText: EditText
    private lateinit var buttonLogin: Button
    private lateinit var regButton: Button
    private lateinit var passForget: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_in)

        this.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        auth = FirebaseAuth.getInstance()
        emailUserText = findViewById(R.id.userField)
        passwordUserText = findViewById(R.id.passwordField)
        buttonLogin = findViewById(R.id.loginButton)
        regButton = findViewById(R.id.regButton)
        passForget = findViewById(R.id.passwordForget)

        buttonLogin.setOnClickListener {
            buttonLogin.isEnabled = false

            var email: String = emailUserText.text.toString()
            var password: String = passwordUserText.text.toString()

            if(TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                Toast.makeText(this@LogIn, R.string.FillAllFields, Toast.LENGTH_LONG).show()
            } else{
                auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, OnCompleteListener { task ->
                    buttonLogin.isEnabled = true
                    if(task.isSuccessful) {
                        val user = auth.currentUser
                        val intent = Intent(this, MainActivity::class.java)
                        updateUI(user)
                        startActivity(intent)
                        finish()
                    }else {
                        Toast.makeText(this, R.string.LogInFailed, Toast.LENGTH_LONG).show()
                    }
                });
            }
        }

        //cliccando il bottone si va verso l'activity di SignUp
        regButton.setOnClickListener {
            val intent = Intent(this, SignUp::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left)
            finish()
        }

        passForget.setOnClickListener {
            startActivity(Intent(this, PasswordReset::class.java))
            overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left)
            finish()
        }
    }

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if(currentUser != null){

        }
    }
    private fun updateUI(user: FirebaseUser?) {

    }

    companion object {
        private const val TAG = "EmailPassword"
    }
}