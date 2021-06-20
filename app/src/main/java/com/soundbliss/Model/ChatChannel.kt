package com.soundbliss.Model


//class to collect all the messages of firestore
data class ChatChannel (val userIds : MutableList<String>){
    constructor():this(mutableListOf())

}