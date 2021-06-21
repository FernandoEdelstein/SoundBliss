package com.soundbliss

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.soundbliss.Fragments.*
import com.soundbliss.Model.User
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_search.*


class MainActivity : AppCompatActivity() {

    private val bottomNavigationView : BottomNavigationView? = null
    private var selectorFragment : Fragment? = null
    private lateinit var firestoreDb : FirebaseFirestore
    private lateinit var documentReference : DocumentReference
    private lateinit var auth: FirebaseAuth
    private lateinit var user:User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Instance the current User
        auth = FirebaseAuth.getInstance()
        var id = auth.currentUser!!.uid

        firestoreDb = FirebaseFirestore.getInstance()
        documentReference = firestoreDb.collection("users").document(id)
        documentReference.get()
            .addOnSuccessListener { documentSnapshot ->
                user = documentSnapshot.toObject(User::class.java)!!
                Log.i("MAIN", user.toString())
            }.addOnFailureListener{ exception ->
                Log.i("HomeFragment","Failure fetching signed in user" , exception)
            }


        val homeFrag = HomeFragment()
        val searchFrag = SearchFragment()
        //val chatFrag = ChatListFragment()
        //val profileFrag = ProfileFragment()

        makeCurrentFragment(homeFrag)
        bottom_navigation.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.nav_home -> makeCurrentFragment(homeFrag)
                R.id.nav_search -> makeCurrentFragment(searchFrag)
                R.id.nav_profile -> makeCurrentFragment(ProfileFragment())
                R.id.nav_chat -> {
                    startActivity(Intent(this, ChatActivity::class.java))
                    overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left)
                }
                R.id.nav_add -> {
                    startActivity(Intent(this, PostActivity::class.java))

                }
            }
            true
        }


    }

        private fun makeCurrentFragment(fragment:Fragment) = supportFragmentManager.beginTransaction().apply{
            replace (R.id.fragment_container,fragment)
            addToBackStack(null)
            commit()
        }

}