package com.soundbliss.Model

import android.os.Parcelable
import com.google.firebase.database.PropertyName
import com.google.firebase.firestore.GeoPoint
import java.io.Serializable

data class AllPost(@get:PropertyName("creation_time_ms")@set:PropertyName("creation_time_ms") var creation_time_ms: Long = 0,
                   @get:PropertyName("description")@set:PropertyName("description") var description: String = "",
                   @get:PropertyName("gender")@set:PropertyName("gender") var gender: String? = "",
                   @get:PropertyName("gender")@set:PropertyName("gender") var location: GeoPoint? = null,
                   @get:PropertyName("gender")@set:PropertyName("gender") var locationtext: String? = null,
                   @get:PropertyName("posturl")@set:PropertyName("posturl") var posturl: String? = "",
                   @get:PropertyName("title")@set:PropertyName("title") var title: String? = "",
                   @get:PropertyName("type")@set:PropertyName("type") var type: String = "",
                   @get:PropertyName("userid")@set:PropertyName("userid") var userid: String = "",
                   @get:PropertyName("username")@set:PropertyName("username") var username: String = "") :
    Serializable {


    var documentId : String = ""
    var posterPic : String = ""

    @JvmName("setDocumentId1")
    fun setDocumentId(s: String){
        documentId = s
    }
    @JvmName("setPosterPic1")
    fun setPosterPic(s: String){
        posterPic = s
    }
    //Track Constructor
    constructor(creation_time_ms: Long = 0,
                description: String = "",
                gender: String = "",
                posturl: String = "",
                title: String = "",
                userid:String = "",
                username:String = ""):this(creation_time_ms,
                                            description,
                                            gender,
                                            null,
                                            null,
                                            posturl,
                                            title,
                                            "track",
                                            userid,
                                            username){
                }

    //Photo Constructor
    constructor(creation_time_ms: Long = 0,
                description: String = "",
                posturl: String = "",
                userid:String = "",
                username:String = ""):this(creation_time_ms,
                                            description,
                                            null,
                                            null,
                                            null,
                                            posturl,
                                            null,
                                            "image",
                                            userid,
                                            username){

                }

    //Request Constructor
    constructor(creation_time_ms: Long = 0,
                description: String = "",
                gender: String = "",
                location: GeoPoint,
                locationtext: String,
                title: String = "",
                userid:String = "",
                username:String = ""):this(creation_time_ms,
                                            description,
                                            gender,
                                            location,
                                            locationtext,
                                            null,
                                            title,
                                            "request",
                                            userid,
                                            username){

                                        }


}