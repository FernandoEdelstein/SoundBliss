package com.soundbliss

import android.content.ContentValues
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*
import com.soundbliss.Adapters.PostAdapter
import com.soundbliss.Fragments.ChatListFragment
import com.soundbliss.Model.AllPost
import com.soundbliss.Model.ChatChannel
import com.soundbliss.Model.TextMessage
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.fragment_track.*

class ChatActivity : AppCompatActivity() {

    private lateinit var firestoreDb: FirebaseFirestore

    private lateinit var messages: MutableList<TextMessage>
    private lateinit var msgAdapter: PostAdapter

    private lateinit var recyclerView: RecyclerView

    private lateinit var receiverId : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        receiverId = intent.getStringExtra("receiverid").toString()

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
    }

    private fun sendMessage() {
        val newMessage = TextMessage(
            System.currentTimeMillis(),
            receiverId,
            "f4KCsDS2X4WWholLIjpBwsy9XoO2", //HARD CODED -> TO BE REPLACED WITH CURRENT USER
            "Hola Eli!"     //HARD CODED -> TO BE REPLACED WITH TEXT MESSAGE
            )

        firestoreDb.collection("message/").add(newMessage)
    }

}