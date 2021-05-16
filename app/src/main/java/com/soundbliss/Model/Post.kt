package com.soundbliss.Model

import android.location.Location
import com.google.firebase.database.PropertyName

class Post(
    @get:PropertyName("creation_time_ms")@set:PropertyName("creation_time_ms") var creation_time_ms: Long = 0,
    @get:PropertyName("description")@set:PropertyName("description") var description: String = "",
    @get:PropertyName("posturl")@set:PropertyName("posturl") var posturl: String = "",
    @get:PropertyName("type")@set:PropertyName("type") var type: String = "",
    @get:PropertyName("userid")@set:PropertyName("userid") var userid: String = "",
    @get:PropertyName("username")@set:PropertyName("username") var username: String = ""
    ): PostSuperClass() {


}