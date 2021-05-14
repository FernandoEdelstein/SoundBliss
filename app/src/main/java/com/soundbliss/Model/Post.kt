package com.soundbliss.Model

import android.location.Location
import com.google.firebase.database.PropertyName

data class Post(
    var userid:String, var username:String,
    var description : String = "",
    @get:PropertyName("image_url") @set:PropertyName("file_url")  var imageUrl : String = "",
    @get:PropertyName("creation_time_ms") @set:PropertyName("creation_time_ms")  var creation_time_ms: Long = 0
    ){


}