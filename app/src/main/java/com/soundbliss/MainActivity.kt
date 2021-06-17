package com.soundbliss

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.SearchView
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.firestore.FirebaseFirestore
import com.soundbliss.Fragments.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_search.*


class MainActivity : AppCompatActivity() {

    private val bottomNavigationView : BottomNavigationView? = null
    private var selectorFragment : Fragment? = null

    private lateinit var firestoreDb : FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        firestoreDb = FirebaseFirestore.getInstance()

        val homeFrag = HomeFragment()
        val searchFrag = SearchFragment()
        val chatFrag = ChatListFragment()
        val profileFrag = ProfileFragment()



        makeCurrentFragment(homeFrag)
        bottom_navigation.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.nav_home -> makeCurrentFragment(homeFrag)
                R.id.nav_search -> makeCurrentFragment(searchFrag)
                R.id.nav_profile -> makeCurrentFragment(profileFrag)
                R.id.nav_chat -> makeCurrentFragment(chatFrag)
                R.id.nav_add -> {
                    startActivity(Intent(this, PostActivity::class.java))

                }
            }
            true
        }


    }

        private fun makeCurrentFragment(fragment:Fragment) = supportFragmentManager.beginTransaction().apply{
            replace (R.id.fragment_container,fragment)
            commit()
        }
}