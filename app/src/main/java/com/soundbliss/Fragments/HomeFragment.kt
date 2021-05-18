package com.soundbliss.Fragments

import android.content.ContentValues.TAG
import android.os.Bundle
import android.renderscript.Sampler
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.soundbliss.Adapters.PostAdapter
import com.soundbliss.Adapters.TrackPostAdapter
import com.soundbliss.Model.AllPost
import com.soundbliss.Model.Post
import com.soundbliss.Model.PostSuperClass
import com.soundbliss.Model.TrackPost
import com.soundbliss.R
import kotlinx.android.synthetic.main.fragment_home.*
import java.util.*

private const val TAG = "Home Fragment"

class HomeFragment : Fragment() {


    private lateinit var firestoreDb: FirebaseFirestore
    
    private lateinit var posts : MutableList<AllPost>
    private lateinit var postAdapter: PostAdapter

    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view : View = inflater.inflate(R.layout.fragment_home, container, false)

        recyclerView = view.findViewById(R.id.rvPosts)

        posts = mutableListOf()

        postAdapter = PostAdapter(context!!, posts)

        recyclerView.adapter = postAdapter

        recyclerView.layoutManager = LinearLayoutManager(context)


        firestoreDb = FirebaseFirestore.getInstance()
        val postReference = firestoreDb.collection("posts")
            .limit(20).orderBy("creation_time_ms", Query.Direction.DESCENDING)
        postReference.addSnapshotListener{snapshot,exception ->
            if(exception != null || snapshot == null){
                Log.e(TAG,"Exception when querying posts" , exception)
                return@addSnapshotListener
            }
            val postList = snapshot.toObjects(AllPost::class.java)
            posts.clear()
            posts.addAll(postList)
            postAdapter.notifyDataSetChanged()


            for(post in postList){
                Log.i(TAG,"Post ${post}")
            }

        }

        return view
    }

}