package com.soundbliss.Fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.soundbliss.Adapters.UserAdapter
import com.soundbliss.MessengerActivity
import com.soundbliss.Model.User
import com.soundbliss.R
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.OnItemClickListener
import com.xwray.groupie.Section
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.fragment_people.*


class PeopleChatFragment : Fragment() {

    private lateinit var userListenerRegistration: ListenerRegistration

    private var shouldInitRecyclerView = true

    private lateinit var peopleSection: Section

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        userListenerRegistration = addUsersListener(this.requireActivity(), this::updateRecyclerView)

        return inflater.inflate(R.layout.fragment_people, container, false)


    }

    override fun onDestroyView() {
        super.onDestroyView()
        removeListener(userListenerRegistration)
        shouldInitRecyclerView = true
    }

    //update the list of users present as Friends
    private fun updateRecyclerView(adapters: List<UserAdapter>) {
        fun init() {
            recyclerview_chatPeople.apply {
                layoutManager = LinearLayoutManager(this.context)
                adapter = GroupAdapter<ViewHolder>().apply {
                    peopleSection = Section(adapters)
                    add(peopleSection)
                    setOnItemClickListener(onItemClick)
                }
            }
            shouldInitRecyclerView = false
        }
        fun updateItems() = peopleSection.update(adapters)

        if (shouldInitRecyclerView)
            init()
        else
            updateItems()

    }

    //add some new user to the list of Friends
    private fun addUsersListener(
        context: Context,
        onListen: (List<UserAdapter>) -> Unit
    ): ListenerRegistration {
        var firestoreInstance = FirebaseFirestore.getInstance()
        return firestoreInstance.collection("users")
            .addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                if (firebaseFirestoreException != null) {
                    Log.e("FIRESTORE", "Users listener error.", firebaseFirestoreException)
                    return@addSnapshotListener
                }

                var items = mutableListOf<UserAdapter>()
                querySnapshot!!.documents.forEach {
                    if (it.id != FirebaseAuth.getInstance().currentUser?.uid)
                        items.add(UserAdapter(it.toObject(User::class.java)!!, it.id, context))
                }
                onListen(items)
            }

    }

    private val onItemClick = OnItemClickListener{ item, view ->
        if(item is UserAdapter){
          val intent = Intent(activity, MessengerActivity::class.java)
            intent.putExtra("username", item.user.uname)
            intent.putExtra("friendUid", item.user.uid)
            startActivity(intent)
        }
    }


    private fun removeListener(registration: ListenerRegistration) = registration.remove()

}


