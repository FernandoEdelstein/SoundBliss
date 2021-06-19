package com.soundbliss

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.soundbliss.Adapters.MessageAdapter
import com.soundbliss.Model.ChatChannel
import com.soundbliss.Model.Message
import com.soundbliss.Model.MessageType
import com.soundbliss.Model.TextMessage
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Section
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_messenger.*
import java.util.*

class MessengerActivity : AppCompatActivity() {


    private lateinit var firestoreDb: FirebaseFirestore

    private lateinit var auth: FirebaseAuth
    private lateinit var currentUser: DocumentReference
    private lateinit var messageListener: ListenerRegistration

    private var initRecyclerView = true
    private lateinit var messagesSection : Section



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_messenger)
        setSupportActionBar(findViewById(R.id.toolbar))


        findViewById<ImageView>(R.id.send).setOnClickListener { view ->
            Snackbar.make(view, "Sending message", Snackbar.LENGTH_LONG)

            auth = FirebaseAuth.getInstance()
            firestoreDb = FirebaseFirestore.getInstance()
            currentUser = firestoreDb.collection("users").document(auth.currentUser!!.uid)

            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.title = intent.getStringExtra("username")
            val friendId = intent.getStringExtra("friendUid")

            if (friendId != null) {
                onCreateChatChannel(friendId) { channelId ->
                    messageListener = addMessageListener(channelId, this, this::updateRecyclerView)

                    send.setOnClickListener {
                        val sendMessage = TextMessage(editText_message.text.toString(),
                            Calendar.getInstance().time,
                            auth.currentUser!!.uid,
                            MessageType.TEXT)
                        //after send message, set it empty
                        editText_message.setText("")
                        sendMessage(sendMessage, channelId)
                    }
                }
            }
        }
    }

    //creation of the chat channel
            private fun onCreateChatChannel(
                friendId: String,
                onComplete: (channelId: String) -> Unit
            ) {
                val chatChannel = firestoreDb.collection("chatChannels")
                currentUser.collection("activeChat")
                    .document(friendId).get().addOnSuccessListener { documentSnapshot ->
                        if (documentSnapshot.exists()) {
                            onComplete(documentSnapshot["channelId"] as String)
                            return@addOnSuccessListener
                        }
                        val currentUserId = auth.currentUser!!.uid
                        //to create a new chat document if it doesn't exist
                        val newChatChannel = chatChannel.document()
                        newChatChannel.set(ChatChannel(mutableListOf(currentUserId, friendId)))

                        //to chat with a friend
                        currentUser.collection("activeChat")
                            .document(friendId)
                            .set(mapOf("channelId" to newChatChannel.id))

                        //a friend to chat with me
                        firestoreDb.collection("users").document(friendId)
                            .collection("activeChat")
                            .document("uname")
                            .set(mapOf("channelId" to newChatChannel.id))

                        onComplete(newChatChannel.id)

                    }
            }

            private fun addMessageListener(
                channelId: String, context: Context,
                onListen: (List<MessageAdapter>) -> Unit
            ): ListenerRegistration {
                val chatChannel = firestoreDb.collection("chatChannels")
                return chatChannel.document(channelId)
                    .collection("messages")
                    .orderBy("time")
                    .addSnapshotListener { snapshot, exception ->
                        if (exception != null) {
                            Log.e("FIRESTORE", "AddingMessageListener error", exception)
                            return@addSnapshotListener
                        }
                        //add the type of message, in this case a TEXT message
                        val items = mutableListOf<MessageAdapter>()
                        snapshot!!.documents.forEach { document ->
                            if (document["type"] == MessageType.TEXT)
                                items.add(MessageAdapter(document.toObject(TextMessage::class.java)!!,
                                    context))
                            else
                                TODO("adding image")

                        }
                        onListen(items)
                    }

            }

    //send a Message in the correct Channel thanks to the id of Chat (Channel)
            private fun sendMessage(message: Message, channelId: String) {
                val chatChannel = firestoreDb.collection("chatChannels")
                chatChannel.document(channelId)
                    .collection("messages")
                    .add(message)
            }


    //update the list of messages
            private fun updateRecyclerView(message: List<MessageAdapter>) {
                fun init() {
                    recycler_view_messages.apply {
                        layoutManager = LinearLayoutManager(this@MessengerActivity)
                        adapter = GroupAdapter<ViewHolder>().apply {
                            messagesSection = Section(message)
                            this.add(messagesSection)
                        }
                    }
                    initRecyclerView = false
                }

                fun updateItems() = messagesSection.update(message)
                if (initRecyclerView)
                    init()
                else
                    updateItems()

                recycler_view_messages.scrollToPosition(recycler_view_messages.adapter!!.itemCount - 1)
            }
        }
