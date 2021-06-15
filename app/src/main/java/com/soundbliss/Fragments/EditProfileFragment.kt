package com.soundbliss.Fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuth.AuthStateListener
import com.google.firebase.auth.FirebaseAuth.getInstance
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.soundbliss.R
import de.hdodenhof.circleimageview.CircleImageView



class EditProfileFragment : Fragment() {
    private val TAG = "EditProfileFragment"
    //firebase

    private lateinit var auth: FirebaseAuth

    //Database
    private lateinit var firestoreDb: FirebaseFirestore
    private lateinit var document: DocumentReference


    //EditProfile Fragment
    private lateinit var mUsername: EditText //EditProfile Fragment widgets
    private lateinit var mPassword: EditText
    private lateinit var mDescription: EditText  //EditProfile Fragment widgets
    private lateinit var mEmail: EditText//EditProfile Fragment widgets
    private lateinit var mChangeProfilePhoto: TextView
    private var mProfilePhoto: CircleImageView? = null
    private var back: ImageView? = null
    private var checkMark: ImageView? = null
    private var userId: String? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_edit_profile, container, false)
        auth = getInstance()
        userId = auth.currentUser!!.uid
        mProfilePhoto = view.findViewById(R.id.profile_photo) as CircleImageView
        mUsername = view.findViewById(R.id.username) as EditText
        mDescription = view.findViewById(R.id.description) as EditText
        mEmail = view.findViewById(R.id.email) as EditText
        mPassword = view.findViewById(R.id.password) as EditText
        mChangeProfilePhoto = view.findViewById(R.id.changeProfilePhoto) as TextView
        back = view.findViewById(R.id.backArrow) as ImageView
        checkMark = view.findViewById(R.id.saveChanges) as ImageView

        firestoreDb = FirebaseFirestore.getInstance()
        document = firestoreDb.collection("users").document(userId!!)


        //setupFirebaseAuth()

        back!!.setOnClickListener {
            Log.d(TAG, "onClick: navigating back to FragmentProfile")
            fragmentManager?.beginTransaction()?.replace(R.id.fragment_profile, ProfileFragment())
                ?.hide(this)?.commit()
        };

        checkMark!!.setOnClickListener {
            fragmentManager?.beginTransaction()?.replace(R.id.fragment_profile, ProfileFragment())
                ?.commit()
            saveChangesSetting()
        }

        return view
    }

    fun saveChangesSetting() {
        val username = mUsername.text.toString()
        val description = mDescription.text.toString()
        val email = mEmail.text.toString()
        val password = mPassword.text.toString()

       if(username.isNotEmpty()) {
           document.update(mapOf("uname" to username))
           Toast.makeText(this.context, R.string.User, Toast.LENGTH_SHORT).show()
       }
        if(description.isNotEmpty()) {
            document.update(mapOf("bio" to description))
            Toast.makeText(this.context, R.string.Description, Toast.LENGTH_SHORT).show()
        }
        if(email.isNotEmpty()) {
            document.update(mapOf("mail" to email))
            auth.currentUser!!.updateEmail(mEmail.toString())
            Toast.makeText(this.context, R.string.Email, Toast.LENGTH_SHORT).show()
        }
        if(password.isNotEmpty()) {
           auth.currentUser!!.updatePassword(mPassword.toString())
            Toast.makeText(this.context, R.string.Pass, Toast.LENGTH_SHORT).show()
        }

    }


}




