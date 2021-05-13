package com.soundbliss.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import com.soundbliss.Model.User
import com.soundbliss.R
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

class UserAdapter(mContext: Context?, mUsers: List<User>, isFragment: Boolean) : RecyclerView.Adapter<UserAdapter.ViewHolder>() {

    lateinit var mContext : Context
    var mUser = listOf<User>()
    var isFragment : Boolean = false
    private lateinit var fireBaseUser : FirebaseUser

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var profileImage: CircleImageView = itemView.findViewById(R.id.imageProfile)
        var username : TextView = itemView.findViewById(R.id.username_item)
        var fullName : TextView = itemView.findViewById(R.id.fullname)
        var btnFollow : Button = itemView.findViewById(R.id.btn_follow)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        var view : View = LayoutInflater.from(mContext).inflate(R.layout.user_item, parent ,false)
        return UserAdapter.ViewHolder(view)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        fireBaseUser = Firebase.auth.currentUser

        var user: User = mUser.get(position)
        holder.btnFollow.setText("Follow")
        holder.username.setText(user.user)
        holder.fullName.setText(user.name)

        Picasso.get().load(user.imageurl).placeholder(R.mipmap.ic_launcher).into(holder.profileImage)

        isFollowed(user.uid, holder.btnFollow)

        if(user.uid.equals(fireBaseUser.uid)){
            holder.btnFollow.setText("Following")
        }
    }



    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }

    fun isFollowed(id: String, btnFollow: Button) {
        var reference: DatabaseReference =
            FirebaseDatabase.getInstance().getReference().child("Follow")
                .child(fireBaseUser.uid).child("Following")
        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.child(id).exists())
                    btnFollow.setText("Following")
                else
                    btnFollow.setText("Follow")
            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        })

    }

}