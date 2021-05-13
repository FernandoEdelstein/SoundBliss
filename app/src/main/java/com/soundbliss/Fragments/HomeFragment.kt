package com.soundbliss.Fragments

import android.os.Bundle
import android.renderscript.Sampler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.soundbliss.R
import java.util.*

class HomeFragment : Fragment() {

    private lateinit var firebasedb: FirebaseDatabase
    private lateinit var reference: DatabaseReference


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view : View = inflater.inflate(R.layout.fragment_home, container, false)

        firebasedb = FirebaseDatabase.getInstance()
        reference = firebasedb.getReference()


        return view
    }

}