package com.soundbliss.Model

import android.location.Location
import com.google.firebase.database.PropertyName

data class Post (
    var user : User? = null,
    var description : String = "",
    var location: String?,
    @get:PropertyName("image_url") @set:PropertyName("file_url")  var imageUrl : String = "",
    @get:PropertyName("creation_time_ms") @set:PropertyName("creation_time_ms")  var creationTimeMs: Long = 0
    ){

}