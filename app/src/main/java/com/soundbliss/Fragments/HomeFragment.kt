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
import com.soundbliss.Adapters.TrackPostAdapter
import com.soundbliss.Model.Post
import com.soundbliss.Model.PostSuperClass
import com.soundbliss.Model.TrackPost
import com.soundbliss.R
import kotlinx.android.synthetic.main.fragment_home.*
import java.util.*

private const val TAG = "Home Fragment"

class HomeFragment : Fragment() {


    private lateinit var firestoreDb: FirebaseFirestore
    private lateinit var posts: MutableList<PostSuperClass>
    private lateinit var imagePosts : MutableList<Post>
    private lateinit var trackPosts : MutableList<TrackPost>
    private lateinit var trackPostAdapter: TrackPostAdapter

    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view : View = inflater.inflate(R.layout.fragment_home, container, false)

        recyclerView = view.findViewById(R.id.rvPosts)

        posts = mutableListOf()

        trackPosts = mutableListOf()

        trackPostAdapter = TrackPostAdapter(context!!,trackPosts)

        recyclerView.adapter = trackPostAdapter

        recyclerView.layoutManager = LinearLayoutManager(context)


        firestoreDb = FirebaseFirestore.getInstance()
        val postReference = firestoreDb.collection("tracks")
            .limit(10).orderBy("creation_time_ms", Query.Direction.DESCENDING)
        postReference.addSnapshotListener{snapshot,exception ->
            if(exception != null || snapshot == null){
                Log.e(TAG,"Exception when querying image posts" , exception)
                return@addSnapshotListener
            }
            val trackPostList = snapshot.toObjects(TrackPost::class.java)
            trackPosts.clear()
            trackPosts.addAll(trackPostList)
            trackPostAdapter.notifyDataSetChanged()



            for(post in trackPostList){
                Log.i(TAG,"Post ${post}")
            }

        }

        return view
    }

}