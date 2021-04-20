package com.soundbliss

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.soundbliss.Fragments.*
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private val bottomNavigationView : BottomNavigationView? = null
    private var selectorFragment : Fragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


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

        makeCurrentFragment(homeFrag)
        bottom_navigation.setOnNavigationItemSelectedListener {
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
    }

        private fun makeCurrentFragment(fragment:Fragment) = supportFragmentManager.beginTransaction().apply{
            replace (R.id.fragment_container,fragment)
            commit()
        }

}