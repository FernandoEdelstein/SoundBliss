package com.soundbliss.Fragments

import android.content.Intent
import android.os.Bundle
import android.view.ContextMenu
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hendraanggrian.appcompat.widget.SocialAutoCompleteTextView
import com.soundbliss.Adapters.PostAdapter
import com.soundbliss.Adapters.UserSearchAdapter
import com.soundbliss.Model.AllPost
import com.soundbliss.Model.User
import com.soundbliss.PostActivity
import com.soundbliss.R
import com.soundbliss.R.menu.search_menu
import kotlinx.android.synthetic.main.fragment_search.*

class SearchFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var searchBar : SocialAutoCompleteTextView
    private lateinit var mUsers : MutableList<User>
    private lateinit var userAdapter : UserSearchAdapter

    private lateinit var posts : MutableList<AllPost>
    private lateinit var postAdapter: PostAdapter

    private lateinit var searchMenu: ContextMenu

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        var view : View = inflater.inflate(R.layout.fragment_search, container, false)


        recyclerView = view.findViewById(R.id.recyclerViewSearch)
        recyclerView.setHasFixedSize(true)

        mUsers = mutableListOf()

        userAdapter = UserSearchAdapter(context!!, mUsers,true)
        postAdapter = PostAdapter(context!!,posts)

        recyclerView.layoutManager = LinearLayoutManager(context)

        searchBar = view.findViewById(R.id.search_bar)

        search_navigation.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.ser_user -> recyclerView.adapter = userAdapter
                R.id.ser_location -> recyclerView.adapter = postAdapter
                R.id.ser_request -> recyclerView.adapter = postAdapter
                R.id.ser_gender -> recyclerView.adapter = postAdapter
            }
            true
        }




        return view
    }


}