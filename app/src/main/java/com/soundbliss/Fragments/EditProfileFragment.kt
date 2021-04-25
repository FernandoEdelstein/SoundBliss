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
import com.google.firebase.ktx.Firebase
import com.soundbliss.R
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.snippet_center_edit_profile.*


class EditProfileFragment : Fragment() {
    private val TAG = "EditProfileFragment"
    //firebase

    private lateinit var auth: FirebaseAuth
    private var databaseReference: DatabaseReference? = null
    private lateinit var database : FirebaseDatabase
    private var mAuthListener: AuthStateListener? = null


    //EditProfile Fragment
    private lateinit var mUsername: EditText //EditProfile Fragment widgets
    private lateinit var mPassword: EditText
    private lateinit var mDescription: EditText  //EditProfile Fragment widgets
    private lateinit var mEmail: EditText//EditProfile Fragment widgets
    private lateinit var mChangeProfilePhoto: TextView
    private var mProfilePhoto: CircleImageView? = null
    private var back : ImageView? = null
    private var checkMark: ImageView? = null
    private var userId: String? = null



    @SuppressLint("WrongViewCast")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_edit_profile, container, false)

        auth = getInstance()

        mProfilePhoto = view.findViewById(R.id.profile_photo) as CircleImageView
        mUsername = view.findViewById(R.id.username) as EditText
        mDescription = view.findViewById(R.id.description) as EditText
        mEmail = view.findViewById(R.id.email) as EditText
        mPassword = view.findViewById(R.id.password) as EditText
        mChangeProfilePhoto = view.findViewById(R.id.changeProfilePhoto) as TextView
        back = view.findViewById(R.id.backArrow) as ImageView
        checkMark = view.findViewById(R.id.saveChanges) as ImageView


        setupFirebaseAuth()

        back!!.setOnClickListener{
            Log.d(TAG, "onClick: navigating back to FragmentProfile")
            fragmentManager?.beginTransaction()?.replace(R.id.fragment_profile, ProfileFragment())?.commit()
        };

        checkMark!!.setOnClickListener {
            Log.d(TAG, "onClick: attempting to save changes to FragmentProfile");
            fragmentManager?.beginTransaction()?.replace(R.id.fragment_profile, ProfileFragment())?.commit()
            saveProfileSettings()
        }



        return view
    }

// method to save the update of the profile settings
    private fun saveProfileSettings() {
        val username = mUsername!!.text.toString()
        val email = mEmail!!.text.toString()
        val password = mPassword!!.text.toString()
        val description = mDescription!!.text.toString()

        //case1: if the user made a change to their username
        if (!auth.currentUser.equals(username) ) {
            checkIfUsernameExists(username)
        }
        //case2: if the user made a change to their email
        if (!auth.currentUser.email.equals(email)) {

            // step1) Reauthenticate
            Toast.makeText(activity, R.string.LogInFailed, Toast.LENGTH_LONG)
            if (email.isNotEmpty()) {
                auth.currentUser.verifyBeforeUpdateEmail(email)
                auth.currentUser.updateEmail(email)
            }
        }
        if(password.isNotEmpty())
            auth.currentUser.updatePassword(password)

        if(description.isNotEmpty())
            databaseReference?.child("rvmF9RWAXzbjgg5FHEkaX9oHLDt1")?.child(description)
           val bundle: Bundle? = arguments
            val profileFragment: ProfileFragment? = null
            bundle?.putString("description", description)
             profileFragment?.arguments = bundle




    }

//method to check if the username already exists or not, using a query object
    private fun  checkIfUsernameExists(username: String){
        Log.d(TAG, "checkIfUsernameExists: Checking if  " + username + " already exists.");
        val user = auth.currentUser
        database = FirebaseDatabase.getInstance()
        databaseReference = Firebase.database("https://soundbliss-8ba73-default-rtdb.europe-west1.firebasedatabase.app").reference;
        databaseReference?.child("rvmF9RWAXzbjgg5FHEkaX9oHLDt1")
        val query: Query = databaseReference!!
            .child(getString(R.string.dbname_users))
            .orderByChild(getString(R.string.field_username))
            .equalTo(username)
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (!dataSnapshot.exists()) {
                    //add the username
                    auth.updateCurrentUser(user)
                    Toast.makeText(activity, "saved username.", Toast.LENGTH_SHORT).show()
                }
                for (singleSnapshot in dataSnapshot.children) {
                    if (singleSnapshot.exists()) {
                        Log.d(
                            TAG, "checkIfUsernameExists: FOUND A MATCH: " + auth.currentUser.displayName
                        )
                        Toast.makeText(
                            activity, //return the activity where this fragment is associated with (MainActivity)
                            "That username already exists.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        });

    }


//initialized the firebase object
    private fun setupFirebaseAuth() {
        Log.d(TAG, "setupFirebaseAuth: setting up firebase auth.")
        auth = getInstance()
        database = FirebaseDatabase.getInstance()
        databaseReference = Firebase.database("https://soundbliss-8ba73-default-rtdb.europe-west1.firebasedatabase.app").reference;
        databaseReference?.child("rvmF9RWAXzbjgg5FHEkaX9oHLDt1")
        userId = auth.currentUser.uid
         mAuthListener = AuthStateListener { firebaseAuth ->

            val user = firebaseAuth.currentUser
            if (user != null) {
                /**
                 * User is signed in.
                 * User is able to edit and re-edit the profile, going throw the Fragment Profile 
                 * */
                Log.d(TAG, "onAuthStateChanged:signed_in:" + user.uid)
                fragmentManager?.beginTransaction()
                    ?.replace(R.id.fragment_profile, EditProfileFragment())
                    ?.commit()
            } else {
                // User is signed out
                Log.d(TAG, "onAuthStateChanged:signed_out")
            }
            // ...
        }

    }

    /**
     * onStart() makes the fragment visible to the user (based on its containing activity being started)
     * */
    override fun onStart() {
        super.onStart()
        auth.addAuthStateListener(mAuthListener)
    }

    /**
     * onStop() fragment is no longer visible to the user either because
     * its activity is being stopped or a fragment operation is modifying it in the activity.
     * */
    override fun onStop() {
        super.onStop()
        if (mAuthListener != null) {
            auth.removeAuthStateListener(mAuthListener)
        }
    }


}


