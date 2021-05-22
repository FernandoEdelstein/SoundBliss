package com.soundbliss.Model

data class TextMessage(val creation_time_ms : Long, val receiverid : String, val senderid : String, val text : String ) {
    constructor():this(0,"","","")
}