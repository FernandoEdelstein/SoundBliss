package com.soundbliss.Fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.soundbliss.R
import com.theartofdev.edmodo.cropper.CropImage
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.activity_main.*
import org.w3c.dom.Text


class ProfileFragment : Fragment() {


    private val mProfilePhoto: ImageView? = null
    private lateinit var name: TextView
    private lateinit var lastName : TextView
    private lateinit var editProfile : TextView
//    private lateinit var descriptionProfile : TextView

    private lateinit var auth: FirebaseAuth
    private var databaseReference: DatabaseReference? = null
    private lateinit var database : FirebaseDatabase


//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        // Inflate the layout for this fragment
//       val view : View = inflater.inflate(R.layout.fragment_profile, container, false)
//
//        name = view.findViewById(R.id.display_name)
//        lastName = view.findViewById(R.id.display_lastname)
//        val bund : Bundle? = arguments
//        name.text = bund?.getString("name")
//        lastName.text = bund?.getString("lastname")
//
//
//
//        return view
//    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view : View = inflater.inflate(R.layout.fragment_profile, container, false)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        databaseReference = Firebase.database("https://soundbliss-8ba73-default-rtdb.europe-west1.firebasedatabase.app").reference;
        databaseReference?.child("rvmF9RWAXzbjgg5FHEkaX9oHLDt1")

        editProfile = view.findViewById(R.id.textEditProfile)




        val user = auth.currentUser
        val userReference = databaseReference?.child(user?.uid!!)

        val bundle: Bundle? = this.arguments



        userReference?.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                name = view.findViewById(R.id.display_name)
                lastName = view.findViewById(R.id.display_lastname)
//                descriptionProfile = view.findViewById(R.id.descriptionProfile)
                name.text=  snapshot.child("name").value?.toString()
                lastName.text=  snapshot.child("lastname").value?.toString()
//                descriptionProfile.text = bundle?.getString("description", "")

            }

            override fun onCancelled(error: DatabaseError) {

                TODO("Not yet implemented")
            }
        })

    editProfile.setOnClickListener {
        fragmentManager?.beginTransaction()
            ?.replace(R.id.fragmentEdit_profile, EditProfileFragment())
            ?.commit()

    }
        return view
    }




}












