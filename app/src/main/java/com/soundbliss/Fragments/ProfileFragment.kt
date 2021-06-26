package com.soundbliss.Fragments

import android.content.ContentValues
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.soundbliss.Adapters.PostAdapter
import com.soundbliss.Model.AllPost
import com.soundbliss.Model.User
import com.soundbliss.R
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.item_post_image.view.*
import java.net.URI


class ProfileFragment(user:User?) : Fragment(), PostAdapter.onUserListener {

    constructor() : this(null){}

    private lateinit var auth : FirebaseAuth
    private lateinit var firestoreDb: FirebaseFirestore
    private lateinit var documentReference: DocumentReference

    private lateinit var editProfileButton: Button
    private lateinit var messageButton: Button

    private lateinit var posts : MutableList<AllPost>
    private lateinit var postAdapter: PostAdapter

    private lateinit var recyclerView: RecyclerView


    private lateinit var username: TextView
    private lateinit var editProfile : Button
    private lateinit var descriptionProfile : TextView

    private var initUser = user



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view : View = inflater.inflate(R.layout.fragment_profile, container, false)

        //POSTS RECYCLER VIEW
        recyclerView = view.findViewById(R.id.rvProfilePosts)

        posts = mutableListOf()

        postAdapter = PostAdapter(requireContext(), posts,this)

        recyclerView.adapter = postAdapter

        recyclerView.layoutManager = LinearLayoutManager(context)

        editProfile = view.findViewById(R.id.editProfile)
        username= view.findViewById(R.id.usernameProfile)
        descriptionProfile = view.findViewById(R.id.descriptionBio)
        editProfileButton = view.findViewById(R.id.editProfile)
        messageButton = view.findViewById(R.id.messageButton)

        auth = FirebaseAuth.getInstance()
        var id = auth.currentUser!!.uid


        if(initUser !=null){
            Log.i("PROFILE", initUser.toString())
            username.text = initUser!!.uname
            descriptionProfile.text = initUser!!.bio

            //LOAD PROFILE PIC METHOD

            getPosts(initUser!!.uid)

            if(initUser!!.uid == FirebaseAuth.getInstance().currentUser!!.uid){
                editProfileButton.visibility = View.VISIBLE
                messageButton.visibility = View.GONE
            }else{
                editProfileButton.visibility = View.GONE
                messageButton.visibility = View.VISIBLE
            }
        }else{
            firestoreDb = FirebaseFirestore.getInstance()
            documentReference = firestoreDb.collection("users").document(id)
            documentReference.get()
                .addOnSuccessListener { documentSnapshot ->
                    if(documentSnapshot.exists()) {
                        username.text = documentSnapshot.getString("uname")
                        descriptionProfile.text = documentSnapshot.getString("bio")

                        //Set Profile Pic
                        if(documentSnapshot.getString("imageu") != "")
                            Glide.with(requireContext()).load(documentSnapshot.getString("imageu")).into(profilePhoto)

                        //Get Posts
                        getPosts(documentSnapshot.id)
                    }
                }
                .addOnFailureListener{ exception ->
                    Log.i("HomeFragment","Failure fetching signed in user" , exception)
                }


        }


        //EDIT PROFILE
            editProfile!!.setOnClickListener {
               /*fragmentManager?.beginTransaction()
                    ?.replace(R.id.fragment_edit, EditProfileFragment())
                    ?.commit()*/
                val fragmentManager = fragmentManager
                val fragmentTransaction = fragmentManager!!.beginTransaction()
                val editProfileFragment: EditProfileFragment = EditProfileFragment()
                fragmentTransaction.replace(R.id.mainActivity, editProfileFragment)
                    .setTransition(FragmentTransaction.TRANSIT_NONE)
                    .addToBackStack(null)
                    .commit()
            }

        return view
    }

    private fun getPosts(uid: String){
        firestoreDb = FirebaseFirestore.getInstance()
        var postReference = firestoreDb.collection("posts")
            .limit(20).whereEqualTo("userid",uid).orderBy("creation_time_ms", Query.Direction.DESCENDING)

        postReference.addSnapshotListener { snapshot, exception ->
            if (exception != null || snapshot == null) {
                Log.e(ContentValues.TAG, "Exception when querying posts", exception)
                return@addSnapshotListener
            }
            posts.clear()

            for (documentSnapshot in snapshot) {
                var documentid = documentSnapshot.id

                var post = documentSnapshot.toObject(AllPost::class.java)
                post.setDocumentId(documentid)

                posts.add(post)
            }

            postAdapter.notifyDataSetChanged()

        }
    }

    override fun onPostClick(position: Int) {

    }

}