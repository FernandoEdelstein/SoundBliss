package com.soundbliss.Fragments

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
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

    private lateinit var firestoreDb: FirebaseFirestore

    private lateinit var posts : MutableList<AllPost>
    private lateinit var postAdapter: PostAdapter

    private lateinit var recyclerView: RecyclerView


    private val mProfilePhoto: ImageView? = null
    private lateinit var name: TextView
    private lateinit var lastName : TextView
    private lateinit var editProfile : TextView
//    private lateinit var descriptionProfile : TextView


    private lateinit var databaseReference: DatabaseReference
    private lateinit var database : FirebaseDatabase


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view : View = inflater.inflate(R.layout.fragment_profile, container, false)


        /*
        firestoreDb = FirebaseFirestore.getInstance()

        firestoreDb.collection("users")
            .document(FirebaseAuth.getInstance().currentUser?.uid as String)
            .get()
            .addOnSuccessListener { userSnapshot ->
                signedInUser = userSnapshot.toObject(User::class.java)!!
            }
            .addOnFailureListener{ exception ->
                Log.i("HomeFragment","Failure fetching signed in user" , exception)
            }
*/


        editProfile = view.findViewById(R.id.textEditProfile)
        name = view.findViewById(R.id.display_name)
        lastName = view.findViewById(R.id.display_lastname)

        database = FirebaseDatabase.getInstance()
        databaseReference = database.reference.child("users")

//      descriptionProfile = view.findViewById(R.id.descriptionProfile)


        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                name.text = snapshot.child("id").child("name").value?.toString()
                lastName.text = snapshot.child("id").child("lastname").value?.toString()
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })

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
            val postList = snapshot.toObjects(AllPost::class.java)
            posts.clear()
            posts.addAll(postList)
            postAdapter.notifyDataSetChanged()


            for(post in postList){
                Log.i(ContentValues.TAG,"Post ${post}")
            }

        }

        return view
    }

}