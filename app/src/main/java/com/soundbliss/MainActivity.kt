package com.soundbliss

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.internal.ContextUtils.getActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.soundbliss.Fragments.*
//import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private var bottomNavigationView : BottomNavigationView? = null
    private var selectorFragment : Fragment? = null

    private lateinit var auth: FirebaseAuth
    private var databaseReference: DatabaseReference? = null
    private lateinit var database : FirebaseDatabase

    private lateinit var nameText: TextView
    private lateinit var lastnameText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        databaseReference = Firebase.database("https://soundbliss-8ba73-default-rtdb.europe-west1.firebasedatabase.app").reference;
        databaseReference?.child("rvmF9RWAXzbjgg5FHEkaX9oHLDt1")


/*
        bottomNavigationView = findViewById(R.id.bottom_navigation)
        bottomNavigationView.setOnNavigationItemSelectedListener{
                    when(it.itemId){
                        R.id.nav_home -> selectorFragment = HomeFragment()
                        R.id.nav_search -> selectorFragment = SearchFragment()
                        R.id.nav_add -> selectorFragment = null
                        R.id.nav_like -> selectorFragment = NotificationFragment()
                        R.id.nav_profile -> selectorFragment = ProfileFragment()
                    }

                    if(selectorFragment != null)
                        supportFragmentManager.beginTransaction().replace(R.id.fragment_container, selectorFragment!!).commit()
                    true
            }
        supportFragmentManager.beginTransaction().replace(R.id.fragment_container, HomeFragment()).commit()*/



        val homeFrag = HomeFragment()
        val searchFrag = SearchFragment()
        val notificationFrag = NotificationFragment()
        val profileFrag = ProfileFragment()

        bottomNavigationView = findViewById(R.id.bottom_navigation)

        makeCurrentFragment(homeFrag)
        bottomNavigationView?.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.nav_home -> makeCurrentFragment(homeFrag)
                R.id.nav_search -> makeCurrentFragment(searchFrag)
                R.id.nav_profile -> makeCurrentFragment(profileFrag)
                R.id.nav_like -> makeCurrentFragment(notificationFrag)
                R.id.nav_add -> {
                    null
                    startActivity(Intent(this, PostActivity::class.java))
                }
            }
            true
        }

//        val user = auth.currentUser
//        val userReference = databaseReference?.child(user?.uid!!)
//
//        val manager = supportFragmentManager
//        val trans = manager.beginTransaction()
//        val bundle = Bundle()
//
//        userReference?.addValueEventListener(object : ValueEventListener {
//
//            override fun onDataChange(snapshot: DataSnapshot) {
//
//                 nameText = findViewById<TextView>(R.id.nameText)
//                lastnameText = findViewById<TextView>(R.id.lastnameText)
//                nameText.text = snapshot.child("name").value?.toString()
//                lastnameText.text = snapshot.child("lastname").value?.toString()
//                bundle.putString("name", nameText.toString())
//                bundle.putString("lastname", lastnameText.toString())
//                profileFrag.arguments = bundle
//                trans.replace(R.id.fragment123, profileFrag)
//                trans.commit()
//
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//
//                TODO("Not yet implemented")
//            }
//        })
    }

        private fun makeCurrentFragment(fragment:Fragment) = supportFragmentManager.beginTransaction().apply{
            replace (R.id.fragment_container,fragment)
            commit()

        }

//    private fun loadProfile() {
//        val user = auth.currentUser
//        val userReference = databaseReference?.child(user?.uid!!)
//
//        val manager = supportFragmentManager
//        val trans = manager.beginTransaction()
//        val bundle = Bundle()
//
//        userReference?.addValueEventListener(object : ValueEventListener {
//
//            override fun onDataChange(snapshot: DataSnapshot) {
//
//                val nameText = findViewById<TextView>(R.id.nameField)
//                val lastnameText = findViewById<TextView>(R.id.lastnameField)
//                nameText?.text = snapshot.child("name").value?.toString()
//                lastnameText?.text = snapshot.child("lastname").value?.toString()
//                bundle?.putString("name", nameText.text.toString())
//                bundle?.putString("lastname", lastnameText.text.toString())
//                profileFrag.arguments = bundle
//                trans.add(R.id.fragment123, profileFrag!!)
//                trans.commit()
//
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//
//                TODO("Not yet implemented")
//            }
//        })
//
//    }
}