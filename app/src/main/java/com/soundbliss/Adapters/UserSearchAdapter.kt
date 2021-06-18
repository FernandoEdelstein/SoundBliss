package com.soundbliss.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.View.inflate
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseUser
import com.soundbliss.Model.User
import com.soundbliss.R
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.item_post_image.view.*

class UserSearchAdapter(context: Context, mUsers: List<User>, isFragment: Boolean, onListener: onUserListener) : RecyclerView.Adapter<UserSearchAdapter.ViewHolder>() {

    var mUser = mUsers
    var mOnListener = onListener

    class ViewHolder(itemView: View, onListener: onUserListener) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        var profileImage: CircleImageView = itemView.findViewById(R.id.imageProfile)
        var username : TextView = itemView.findViewById(R.id.username_item)
        var fullName : TextView = itemView.findViewById(R.id.fullname)
        var onLister = onListener

        var itemView = itemView.setOnClickListener (this)

        override fun onClick(v: View?) {
            onLister.onClick(adapterPosition)
        }


    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        var view = layoutInflater.inflate(R.layout.user_item, parent,false)
        return ViewHolder(view,mOnListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var user: User = mUser[position]

        holder.username.text = user.uname
        holder.fullName.text = user.name

        if(user.imageu != ""){
            Picasso.get().load(user.imageu).placeholder(R.mipmap.ic_launcher).into(holder.profileImage)
        }
    }

    override fun getItemCount(): Int = mUser.size

    interface onUserListener{
        fun onClick(position: Int)
    }
}