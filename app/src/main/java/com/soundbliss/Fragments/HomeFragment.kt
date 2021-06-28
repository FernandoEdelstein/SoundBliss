package com.soundbliss.Fragments

import android.content.ContentValues.TAG
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.soundbliss.Adapters.PostAdapter
import com.soundbliss.Model.AllPost
import com.soundbliss.Model.User
import com.soundbliss.R
import kotlinx.android.synthetic.main.fragment_home.*
import java.util.*
import kotlin.concurrent.thread

private const val TAG = "Home Fragment"

class HomeFragment(userid: String) : Fragment(), PostAdapter.onUserListener {

    private lateinit var firestoreDb: FirebaseFirestore
    
    private lateinit var posts : MutableList<AllPost>
    private lateinit var postAdapter: PostAdapter

    private lateinit var recyclerView: RecyclerView

    private lateinit var refresh : SwipeRefreshLayout

    private val currentUserId = userid

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view: View = inflater.inflate(R.layout.fragment_home, container, false)

        refresh = view.findViewById(R.id.homeRefresh)

        recyclerView = view.findViewById(R.id.rvPosts)

        posts = mutableListOf()

        postAdapter = PostAdapter(requireContext(), posts,this, currentUserId)

        recyclerView.adapter = postAdapter

        recyclerView.layoutManager = LinearLayoutManager(context)


        firestoreDb = FirebaseFirestore.getInstance()
        val postReference = firestoreDb.collection("posts")
            .limit(20).orderBy("creation_time_ms", Query.Direction.DESCENDING)
        postReference.addSnapshotListener { snapshot, exception ->
            if (exception != null || snapshot == null) {
                Log.e(TAG, "Exception when querying posts", exception)
                return@addSnapshotListener
            }
            posts.clear()

            for (documentSnapshot in snapshot) {
                var post = documentSnapshot.toObject(AllPost::class.java)
                posts.add(post)
            }

            postAdapter.notifyDataSetChanged()

        }


        refresh.setOnRefreshListener(object : SwipeRefreshLayout.OnRefreshListener{
            override fun onRefresh() {
                firestoreDb = FirebaseFirestore.getInstance()
                val postReference = firestoreDb.collection("posts")
                    .limit(20).orderBy("creation_time_ms", Query.Direction.DESCENDING)
                postReference.addSnapshotListener{snapshot,exception ->
                    if(exception != null || snapshot == null){
                        Log.e(TAG,"Exception when querying posts" , exception)
                        return@addSnapshotListener
                    }
                    posts.clear()

                    for (documentSnapshot in snapshot){
                        var post = documentSnapshot.toObject(AllPost::class.java)
                        posts.add(post)
                    }
                    postAdapter.notifyDataSetChanged()
            }
                refresh.isRefreshing = false
        }})

        return view
    }

    override fun onPostClick(position: Int) {
        val fragmentTransaction = fragmentManager!!.beginTransaction()

        var postCreator : User
        var poster = firestoreDb.collection("users").document(posts[position].userid)
        poster.get().addOnSuccessListener { documentSnapshot ->
            postCreator = documentSnapshot.toObject(User::class.java)!!

            val profileFragment = ProfileFragment(postCreator)
            fragmentTransaction.replace(R.id.fragment_container, profileFragment)
                .addToBackStack(null)
                .commit()
        }

    }

}