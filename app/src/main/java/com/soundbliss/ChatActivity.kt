package com.soundbliss


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu

import android.view.MenuInflater
import android.view.MenuItem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*
import com.soundbliss.Adapters.PostAdapter
import com.soundbliss.Fragments.PeopleChatFragment
import com.soundbliss.Fragments.ProfileFragment
import com.soundbliss.Model.TextMessage
import com.soundbliss.Model.User


import kotlinx.android.synthetic.main.activity_chat.*
import kotlinx.android.synthetic.main.fragment_search.*
import kotlinx.android.synthetic.main.fragment_track.*

class ChatActivity : AppCompatActivity() {

    private lateinit var firestoreDb: FirebaseFirestore
    private lateinit var documentReference: DocumentReference
    private lateinit var auth: FirebaseAuth
    private lateinit var id: String
    //private lateinit var username: TextView

    private lateinit var listUser: MutableList<User>
    private lateinit var messages: MutableList<TextMessage>
    private lateinit var msgAdapter: PostAdapter

    //private lateinit var recyclerView: RecyclerView


    private lateinit var receiverId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        navigation.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.navigation_people -> {
                    makeCurrentFragment(PeopleChatFragment())
                    true
                }
                R.id.navigation_my_account -> {
                    makeCurrentFragment(ProfileFragment())
                    true
                }
                else -> false
            }
        }
    }


    private fun makeCurrentFragment(fragment: androidx.fragment.app.Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragment_layout, fragment)
            commit()
        }
}

        /*receiverId = intent.getStringExtra("receiverid").toString()

        //RECYCLER VIEW INITIALIZATION

        messages = mutableListOf()

        //msgAdapter = PostAdapter(this, messages)
        recyclerView.adapter = msgAdapter

        recyclerView.layoutManager = LinearLayoutManager(this)


        firestoreDb = FirebaseFirestore.getInstance()
        val msgReference = firestoreDb.collection("messages")
            .limit(20).orderBy("creation_time_ms", Query.Direction.ASCENDING)

        //HARD CODED -> TO BE REPLACED WITH CURRENT USER
        msgReference.whereEqualTo("senderid","f4KCsDS2X4WWholLIjpBwsy9XoO2")
        msgReference.whereEqualTo("receiverid",receiverId)

        msgReference.addSnapshotListener { snapshot, exception ->
            if (exception != null || snapshot == null) {
                Log.e(ContentValues.TAG, "Exception when querying messages", exception)
                return@addSnapshotListener
            }

            val msgList = snapshot.toObjects(TextMessage::class.java)
            messages.clear()
            messages.addAll(msgList)
            msgAdapter.notifyDataSetChanged()


            for (msg in messages) {
                Log.i(ContentValues.TAG, "Message ${msg}")
            }
        }
    }*/

  /*  private fun sendMessage() {
        val newMessage = TextMessage(
            System.currentTimeMillis(),
            receiverId,
            "f4KCsDS2X4WWholLIjpBwsy9XoO2", //HARD CODED -> TO BE REPLACED WITH CURRENT USER
            "Hola Eli!"     //HARD CODED -> TO BE REPLACED WITH TEXT MESSAGE
            )

        firestoreDb.collection("message/").add(newMessage)
    }*/





