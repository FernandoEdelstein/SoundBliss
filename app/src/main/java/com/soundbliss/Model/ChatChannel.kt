package com.soundbliss.Model

data class ChatChannel (val userIds : MutableList<String>){
    constructor():this(mutableListOf())
}