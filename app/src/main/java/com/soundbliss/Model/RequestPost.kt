package com.soundbliss.Model

import com.google.firebase.firestore.GeoPoint

class RequestPost (creation_time_ms:String,
                   description:String,
                   gender:String,
                   location: GeoPoint,
                   title:String,
                   type:String,
                   userid:String,
                   username:String) : PostSuperClass() { }