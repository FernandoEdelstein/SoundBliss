package com.soundbliss.Model

import java.util.*

data class Message(val text : String, val time : Date, val senderId:String) {
    constructor():this("",Date(0),"")

}