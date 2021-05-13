package com.soundbliss.Fragments

import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.hendraanggrian.appcompat.widget.SocialAutoCompleteTextView
import com.soundbliss.Adapters.UserAdapter
import com.soundbliss.Model.User
import com.soundbliss.R
import kotlinx.android.synthetic.main.fragment_search.*

class SearchFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var searchBar : SocialAutoCompleteTextView
    private lateinit var mUsers : MutableList<User>
    private lateinit var userAdapter : UserAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        var view : View = inflater.inflate(R.layout.fragment_search, container, false)

        recyclerView = view.findViewById(R.id.recyclerViewUsers)
        recyclerView.setHasFixedSize(true)

        mUsers = mutableListOf()

        userAdapter = UserAdapter(context, mUsers, true)

        searchBar = view.findViewById(R.id.search_bar)

        return view
    }

    /*
    fun readUsers(){
        var reference : DatabaseReference = FirebaseDatabase.getInstance().getReference().child("Users")
        reference.addValueEventListener(ValueEventListener(){
            override fun onDataChange(dataSnapshot: DataSnapshot){
                if(TextUtils.isEmpty(search_bar.text.toString())){
                    this.mUsers.clear()
                    dataSnapshot.children.forEach(){
                        snapshot: DataSnapshot? -> var user: User = snapshot

                    }
                }
            }
        })
    }
    */

}
