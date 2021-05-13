package com.soundbliss.Adapters

import android.content.Context
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseUser
import com.soundbliss.Model.Post
import com.soundbliss.R
import kotlinx.android.synthetic.main.item_post_image.view.*


class PostAdapter(val context:Context, val posts: List<Post>) :
    RecyclerView.Adapter<PostAdapter.ViewHolder>() {

    lateinit var mContext : Context
    lateinit var mPost:List <Post>

    private lateinit var firebaseUser: FirebaseUser

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.item_post_image, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int = posts.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(posts[position])
    }

    inner class ViewHolder (itemView: View): RecyclerView.ViewHolder(itemView){
        fun bind (post:Post){
            itemView.tvUserName.text = post.user?.user
            itemView.tvDescription.text = post.description
            itemView.tvRelativeTime.text = DateUtils.getRelativeTimeSpanString(post.creationTimeMs)
        }
    }
}