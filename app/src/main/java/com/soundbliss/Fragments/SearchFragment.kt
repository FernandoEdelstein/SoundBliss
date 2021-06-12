package com.soundbliss.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hendraanggrian.appcompat.widget.SocialAutoCompleteTextView
import com.soundbliss.Adapters.UserSearchAdapter
import com.soundbliss.Model.User
import com.soundbliss.R

class SearchFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var searchBar : SocialAutoCompleteTextView
    private lateinit var mUsers : MutableList<User>
    private lateinit var userAdapter : UserSearchAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        var view : View = inflater.inflate(R.layout.fragment_search, container, false)

        recyclerView = view.findViewById(R.id.recyclerViewUsers)
        recyclerView.setHasFixedSize(true)

        mUsers = mutableListOf()

        userAdapter = UserSearchAdapter(context, mUsers, true)

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
