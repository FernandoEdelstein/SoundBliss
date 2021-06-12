package com.soundbliss.Fragments

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.ktx.Firebase
import com.soundbliss.Adapters.PostAdapter
import com.soundbliss.Login.SignUp
import com.soundbliss.Model.AllPost
import com.soundbliss.Model.User
import com.soundbliss.R
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.activity_main.*
import org.w3c.dom.Text


class ProfileFragment() : Fragment() {
    private var signedInUser: User? = null

    private lateinit var auth : FirebaseAuth
    private lateinit var firestoreDb: FirebaseFirestore
    private lateinit var documentReference: DocumentReference

    private lateinit var posts : MutableList<AllPost>
    private lateinit var postAdapter: PostAdapter

    private lateinit var recyclerView: RecyclerView


    private lateinit var username: TextView
    private lateinit var editProfile : Button
//    private lateinit var descriptionProfile : TextView



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view : View = inflater.inflate(R.layout.fragment_profile, container, false)



        editProfile = view.findViewById(R.id.editProfile)
        username= view.findViewById(R.id.username)

        auth = FirebaseAuth.getInstance()

        //per ottenere lo username

        firestoreDb = FirebaseFirestore.getInstance()
        documentReference = firestoreDb.collection("users").document()
             documentReference.get()
             .addOnSuccessListener { documentSnapshot ->
                 if(documentSnapshot.exists()) {
                     username.text = documentSnapshot.getString("username")
                 }
            }
             .addOnFailureListener{ exception ->
                 Log.i("HomeFragment","Failure fetching signed in user" , exception)
             }


        //EDIT PROFILE
            editProfile.setOnClickListener {
                fragmentManager?.beginTransaction()
                    ?.replace(R.id.fragmentEdit_profile, EditProfileFragment())
                    ?.commit()

            }

        //POSTS RECYCLER VIEW
        recyclerView = view!!.findViewById(R.id.rvProfilePosts)

        posts = mutableListOf()

        postAdapter = PostAdapter(context!!, posts)

        recyclerView.adapter = postAdapter

        recyclerView.layoutManager = LinearLayoutManager(context)


        firestoreDb = FirebaseFirestore.getInstance()
        val postReference = firestoreDb.collection("posts")
            .limit(20).orderBy("creation_time_ms", Query.Direction.DESCENDING)

            postReference.whereEqualTo("userid",FirebaseAuth.getInstance().currentUser?.uid)

        postReference.addSnapshotListener{snapshot,exception ->
            if(exception != null || snapshot == null){
                Log.e(ContentValues.TAG,"Exception when querying posts" , exception)
                return@addSnapshotListener
            }
            posts.clear()
            for (documentSnapshot in snapshot){
                var documentid = documentSnapshot.id
                var post = documentSnapshot.toObject(AllPost::class.java)
                post.setDocumentId(documentid)
                posts.add(post)
            }
            postAdapter.notifyDataSetChanged()
        }

        return view
    }

}