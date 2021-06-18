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
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.soundbliss.Adapters.PostAdapter
import com.soundbliss.Model.AllPost
import com.soundbliss.R
import kotlinx.android.synthetic.main.fragment_home.*
import java.util.*
import kotlin.concurrent.thread

private const val TAG = "Home Fragment"

class HomeFragment : Fragment() {


    private lateinit var firestoreDb: FirebaseFirestore
    
    private lateinit var posts : MutableList<AllPost>
    private lateinit var postAdapter: PostAdapter

    private lateinit var recyclerView: RecyclerView

    private lateinit var refresh : SwipeRefreshLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view: View = inflater.inflate(R.layout.fragment_home, container, false)

        refresh = view.findViewById(R.id.homeRefresh)

        recyclerView = view.findViewById(R.id.rvPosts)

        posts = mutableListOf()

        postAdapter = PostAdapter(context!!, posts)

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
                var documentid = documentSnapshot.id
                var post = documentSnapshot.toObject(AllPost::class.java)
                post.setDocumentId(documentid)

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
                        var documentid = documentSnapshot.id

                        var post = documentSnapshot.toObject(AllPost::class.java)
                        post.setDocumentId(documentid)

                        posts.add(post)
                    }

                    postAdapter.notifyDataSetChanged()
            }

                refresh.isRefreshing = false
        }})

        return view
    }

}