package com.soundbliss.Fragments

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.provider.ContactsContract
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.ContextMenu
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.EditText
import android.widget.SearchView
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.firebase.ui.database.SnapshotParser
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.hendraanggrian.appcompat.widget.SocialAutoCompleteTextView
import com.soundbliss.Adapters.PostAdapter
import com.soundbliss.Adapters.UserSearchAdapter
import com.soundbliss.Model.AllPost
import com.soundbliss.Model.User
import com.soundbliss.PostActivity
import com.soundbliss.R
import com.soundbliss.R.menu.search_menu
import kotlinx.android.synthetic.main.fragment_search.*
import kotlinx.android.synthetic.main.user_item.view.*

class SearchFragment : Fragment(), UserSearchAdapter.onUserListener, PostAdapter.onUserListener {

    private lateinit var userRecyclerView: RecyclerView
    private lateinit var searchBar : SearchView
    private lateinit var mUsers : MutableList<User>
    private lateinit var userAdapter : UserSearchAdapter

    private lateinit var postAdapter : PostAdapter

    private lateinit var posts: MutableList<AllPost>


    private lateinit var firestoreDb: FirebaseFirestore
    private lateinit var currentUser: FirebaseUser

    private lateinit var menu : BottomNavigationView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        var view : View = inflater.inflate(R.layout.fragment_search, container, false)

        firestoreDb = FirebaseFirestore.getInstance()
        currentUser = FirebaseAuth.getInstance().currentUser!!
        userRecyclerView = view.findViewById(R.id.recyclerViewUser)
        userRecyclerView.setHasFixedSize(true)

        mUsers = mutableListOf()
        posts = mutableListOf()

        userRecyclerView.layoutManager = LinearLayoutManager(context)
        userAdapter = UserSearchAdapter(requireContext(), mUsers, true, this)
        userAdapter.notifyDataSetChanged()

        postAdapter = PostAdapter(requireContext(),posts,this)

        userRecyclerView.adapter = userAdapter

        searchBar = view.findViewById(R.id.search_field)


        searchUser("")

        menu = view.findViewById(R.id.search_navigation)

        menu.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.ser_user ->{searchBar.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
                    override fun onQueryTextSubmit(query: String?): Boolean {
                        return false
                    }

                    override fun onQueryTextChange(newText: String?): Boolean {
                        if (newText != null) {
                            searchUser(newText)
                        }
                        return false
                    }

                })}

                R.id.ser_request -> {searchBar.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
                    override fun onQueryTextSubmit(query: String?): Boolean {
                        return false
                    }

                    override fun onQueryTextChange(newText: String?): Boolean {
                        if (newText != null) {
                            searchRequestTitle(newText)
                        }
                        return false
                    }

                })}
                R.id.ser_gender -> {searchBar.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
                    override fun onQueryTextSubmit(query: String?): Boolean {
                        return false
                    }

                    override fun onQueryTextChange(newText: String?): Boolean {
                        if (newText != null) {
                            searchRequestGender(newText)
                        }
                        return false
                    }

                })}
                R.id.ser_location -> {searchBar.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
                    override fun onQueryTextSubmit(query: String?): Boolean {
                        return false
                    }

                    override fun onQueryTextChange(newText: String?): Boolean {
                        if (newText != null) {
                            searchRequestLocation(newText)
                        }
                        return false
                    }

                })}
            }
            true
        }

        return view
    }



    private fun searchUser(str : String){
        val userReference = firestoreDb.collection("users")
            .limit(20).orderBy("uname",Query.Direction.DESCENDING)

        userReference.addSnapshotListener { snapshot, exception ->
            if (exception != null || snapshot == null) {
                Log.e("SEARCH_FRAGMENT", "Exception when querying users", exception)
                return@addSnapshotListener
            }
            mUsers.clear()

            for (documentSnapshot in snapshot) {
                var user = documentSnapshot.toObject(User::class.java)
                if(user.uid != currentUser.uid){
                    if(user.uname.contains(str,true))
                        mUsers.add(user)
                }
            }
            userAdapter = UserSearchAdapter(requireContext(), mUsers, true, this)
            userAdapter.notifyDataSetChanged()

            userRecyclerView.adapter = userAdapter
        }
    }

    private fun searchRequestTitle(str : String){
        val postReference = firestoreDb.collection("posts")
            .limit(5)
            .whereEqualTo("type", "request").orderBy("creation_time_ms",Query.Direction.DESCENDING)

        postReference.addSnapshotListener { snapshot, exception ->
            if (exception != null || snapshot == null) {
                Log.e("SEARCH_FRAGMENT", "Exception when querying users", exception)
                return@addSnapshotListener
            }
            posts.clear()

            for (documentSnapshot in snapshot) {
                var post = documentSnapshot.toObject(AllPost::class.java)
                    if(post.title!!.contains(str,true))
                        posts.add(post)
            }

            postAdapter = PostAdapter(requireContext(), posts,this)
            postAdapter.notifyDataSetChanged()

            userRecyclerView.adapter = postAdapter
        }
    }

    private fun searchRequestGender(str : String){
        val postReference = firestoreDb.collection("posts")
            .limit(5)
            .whereEqualTo("type", "request").orderBy("creation_time_ms",Query.Direction.DESCENDING)

        postReference.addSnapshotListener { snapshot, exception ->
            if (exception != null || snapshot == null) {
                Log.e("SEARCH_FRAGMENT", "Exception when querying users", exception)
                return@addSnapshotListener
            }
            posts.clear()

            for (documentSnapshot in snapshot) {
                var post = documentSnapshot.toObject(AllPost::class.java)
                if(post.gender!!.contains(str,true))
                    posts.add(post)
            }

            postAdapter = PostAdapter(requireContext(), posts,this)
            postAdapter.notifyDataSetChanged()

            userRecyclerView.adapter = postAdapter
        }
    }

    private fun searchRequestLocation(str : String){
        val postReference = firestoreDb.collection("posts")
            .limit(5)
            .whereEqualTo("type", "request").orderBy("creation_time_ms",Query.Direction.DESCENDING)

        postReference.addSnapshotListener { snapshot, exception ->
            if (exception != null || snapshot == null) {
                Log.e("SEARCH_FRAGMENT", "Exception when querying users", exception)
                return@addSnapshotListener
            }
            posts.clear()

            for (documentSnapshot in snapshot) {
                var post = documentSnapshot.toObject(AllPost::class.java)
                if(post.locationtext!!.contains(str,true))
                    posts.add(post)
            }

            postAdapter = PostAdapter(requireContext(), posts,this)
            postAdapter.notifyDataSetChanged()

            userRecyclerView.adapter = postAdapter
        }
    }

    override fun onClick(position: Int) {
        val fragmentTransaction = fragmentManager!!.beginTransaction()
        val profileFragment = ProfileFragment(mUsers[position])
        fragmentTransaction.replace(R.id.fragment_container, profileFragment)
            .addToBackStack(null)
            .commit()
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