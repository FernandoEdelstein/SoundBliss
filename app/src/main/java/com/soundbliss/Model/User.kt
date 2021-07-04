package com.soundbliss.Model

import com.google.firebase.database.PropertyName

data class User (@get:PropertyName("lastname")@set:PropertyName("lastname") var lastname:String,
                 @get:PropertyName("name")@set:PropertyName("name") var  name:String,
                 @get:PropertyName("username")@set:PropertyName("username") var  uname:String,
                 @get:PropertyName("id")@set:PropertyName("id") var  uid: String,
                 @get:PropertyName("bio")@set:PropertyName("bio") var  bio:String,
                 @get:PropertyName("mail")@set:PropertyName("mail") var  mail:String,
                 @get:PropertyName("image")@set:PropertyName("image") var  imageu:String
){

constructor():this("","","","","","","")

    var phoneNumber : String = ""

    @JvmName("setDocumentId1")
    fun setPhoneNumber(s: String){
        phoneNumber = s
    }
}