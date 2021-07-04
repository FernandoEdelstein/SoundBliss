package com.soundbliss.Adapters

import android.content.Context
import com.bumptech.glide.Glide
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.internal.StorageReferenceUri
import com.soundbliss.Model.User
import com.soundbliss.R
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.user_layout.view.*


//class that contains the data and comunicate with the user_layout
//keep tracks of user
class UserAdapter(val user: User, val userId: String, val context:
Context): Item<ViewHolder>() {

    override fun bind(viewHolder: ViewHolder, position: Int) {
      viewHolder.itemView.username_item_message.text = user.uname
        if(user.imageu != ""){
            //to load a picture in the image view of the user
                //context -> provides Activities, Fragments, and Services access to resource files, images, themes/styles, and external directory locations.
            Glide.with(context)
                .load(user.imageu)
                .placeholder(R.drawable.default_profile_pic)
                .into(viewHolder.itemView.imageView)
        }
    }

    override fun getLayout(): Int {
       return R.layout.user_layout
    }
}