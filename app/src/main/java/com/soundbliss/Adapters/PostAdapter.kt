package com.soundbliss.Adapters

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.soundbliss.MapsActivity
import com.soundbliss.MessengerActivity
import com.soundbliss.Model.AllPost
import com.soundbliss.Player
import com.soundbliss.R
import kotlinx.android.synthetic.main.item_post_image.view.*
import kotlinx.android.synthetic.main.item_post_request.view.*
import kotlinx.android.synthetic.main.item_post_track.view.*

class PostAdapter(var context: Context, list: List<AllPost>,onListener: onUserListener, currentUserId : String) :
    RecyclerView.Adapter<ViewHolder>() {
    private val TAG = "RecyclerAdapter"
    private var list = list
    private var currentUserId = currentUserId
    private var firestoreDb = FirebaseFirestore.getInstance()

    private var storageReference = FirebaseStorage.getInstance().reference

    var mOnListener = onListener

    private inner class ViewHolderOnePhoto(itemView:View,onListener: onUserListener): ViewHolder(itemView),View.OnClickListener{
        var deletePhoto = itemView.deleteImagePost
        var postPhotoProfileImg = itemView.postPhotoProfileImg

        //Listener for accessing poster profile
        var onListener = onListener
        var postPhotoUserName = itemView.postPhotoUserName.setOnClickListener(this)
        override fun onClick(v: View?) {
            onListener.onPostClick(adapterPosition)
        }
    }

    private inner class ViewHolderTwoTrack(itemView:View,onListener: onUserListener): ViewHolder(itemView),View.OnClickListener{
        var postGender = itemView.postTrackGender
        var postTitle = itemView.postTrackName
        var postUsername = itemView.postTrackUserName
        var relativeTime = itemView.postTrackRelativeTime
        var postDescription = itemView.postTrackDescription

        var playBtn = itemView.playBtn

        var deleteTrack = itemView.deleteTrackPost

        var postTrackProfileImg = itemView.postTrackProfileImg

        //Listener for accessing poster profile
        var onListener = onListener
        var postTrackUserName = itemView.postTrackUserName.setOnClickListener(this)
        override fun onClick(v: View?) {
            onListener.onPostClick(adapterPosition)
        }
    }

    private inner class ViewHolderThreeRequest(itemView:View,onListener: onUserListener): ViewHolder(itemView),View.OnClickListener{
        var postLocation = itemView.postRequestLocation
        var deleteRequest = itemView.deleteRequestPost
        var contactButton = itemView.postRequestContact

        var postRequestProfileImg = itemView.postRequestProfileImg

        //Listener for accessing poster Profile
        var onListener = onListener
        var postRequestUserName = itemView.postRequestUserName.setOnClickListener(this)
        override fun onClick(v: View?) {
            onListener.onPostClick(adapterPosition)
        }
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view : View

        return if(viewType == 0){
            view = layoutInflater.inflate(R.layout.item_post_image, parent,false)
            ViewHolderOnePhoto(view, mOnListener)
        }else if(viewType == 1){
            view = layoutInflater.inflate(R.layout.item_post_track,parent, false)
            ViewHolderTwoTrack(view, mOnListener)
        }else {
            view = layoutInflater.inflate(R.layout.item_post_request, parent, false)
            ViewHolderThreeRequest(view,mOnListener)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (list[position].type) {
            "image" -> {
                0
            }
            "track" -> {
                1
            }
            else -> 2
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int){

        if(getItemViewType(position) == 0){
            val post = list[position]
            val viewHolderOne = holder as ViewHolderOnePhoto

            //Add username and profile pic
            var poster = firestoreDb.collection("users").document(post.userid)
            poster.get().addOnSuccessListener { documentSnapshot ->
                if(documentSnapshot.getString("imageu") != "") {
                    Glide.with(context!!).load(documentSnapshot.getString("imageu"))
                        .into(viewHolderOne.postPhotoProfileImg)
                }
                    viewHolderOne.itemView.postPhotoUserName.text = documentSnapshot.getString("uname")

            }

                viewHolderOne.itemView.postPhotoDescription.text = post.description
                viewHolderOne.itemView.postPhotoRelativeTime.text = DateUtils.getRelativeTimeSpanString(post.creation_time_ms)
                Glide.with(context).load(post.posturl).into(viewHolderOne.itemView.postPhotoImageView)

            if(currentUserId == post.userid){
                viewHolderOne.deletePhoto.visibility = View.VISIBLE
                viewHolderOne.deletePhoto.setOnClickListener {
                    val builder = AlertDialog.Builder(context)
                    builder.setMessage(R.string.DeletePost)
                        .setCancelable(false)
                        .setPositiveButton(R.string.Yes){ dialog, id ->
                            //DELETE FROM DATABASE METHOD
                            deletePost(position)
                        }
                        .setNegativeButton(R.string.No){ dialog, id ->
                            dialog.dismiss()
                        }
                    val alert = builder.create()
                    alert.show()
                }
            }else{
                viewHolderOne.deletePhoto.visibility = View.GONE
            }

        }else if(getItemViewType(position) == 1){
            val post = list.get(position)

            val viewHolderTwo = holder as ViewHolderTwoTrack

            //Add profile pic and username
            var poster = firestoreDb.collection("users").document(post.userid)
            poster.get().addOnSuccessListener { documentSnapshot ->
                if(documentSnapshot.getString("imageu") != "") {
                    Glide.with(context!!).load(documentSnapshot.getString("imageu"))
                        .into(viewHolderTwo.postTrackProfileImg)
                }
                    viewHolderTwo.postUsername.text = documentSnapshot.getString("uname")

            }


            //Set up all parameters
            viewHolderTwo.postGender.text = post.gender
            viewHolderTwo.postTitle.text = post.title

            viewHolderTwo.relativeTime.text = DateUtils.getRelativeTimeSpanString(post.creation_time_ms)
            viewHolderTwo.postDescription.text = post.description

            //Music Player
            viewHolderTwo.playBtn.setOnClickListener { v:View? ->
                val intent = Intent(context, Player::class.java)
                var trackBundle = Bundle()
                trackBundle.putSerializable("post",post)
                intent.putExtra("Post", trackBundle)
                context.startActivity(intent)
            }

            //Delete Button
            if(currentUserId == post.userid){
                viewHolderTwo.deleteTrack.visibility = View.VISIBLE
                viewHolderTwo.deleteTrack.setOnClickListener {
                    val builder = AlertDialog.Builder(context)
                    builder.setMessage(R.string.DeletePost)
                        .setCancelable(false)
                        .setPositiveButton(R.string.Yes){ dialog, id ->
                            //DELETE FROM DATABASE METHOD
                            deletePost(position) //Delete the post if user selects "yes"
                        }
                        .setNegativeButton(R.string.No){ dialog, id -> //Dialog Dismiss if user selects "No"
                            dialog.dismiss()
                        }
                    val alert = builder.create()
                    alert.show()
                }
            }else{
                viewHolderTwo.deleteTrack.visibility = View.GONE
            }


        }else if(getItemViewType(position) == 2) {
            var viewHolderThreeRequest = holder as ViewHolderThreeRequest

            val post = list[position]
            var posterUname = ""
            //UserImage and Username
            var poster = firestoreDb.collection("users").document(post.userid)
            poster.get().addOnSuccessListener { documentSnapshot ->
                if(documentSnapshot.getString("imageu") != "") {
                    Glide.with(context!!).load(documentSnapshot.getString("imageu"))
                        .into(viewHolderThreeRequest.postRequestProfileImg)
                }
                    viewHolderThreeRequest.itemView.postRequestUserName.text = documentSnapshot.getString("uname")
                    posterUname = documentSnapshot.getString("uname")!!
            }



            //Set ViewHolder Content
            viewHolderThreeRequest.itemView.postRequestTitle.text = post.title
            viewHolderThreeRequest.itemView.postRequestGender.text = post.gender
            viewHolderThreeRequest.itemView.postRequestDescription.text = post.description
            viewHolderThreeRequest.itemView.postRequestRelativeTime.text =
                DateUtils.getRelativeTimeSpanString(post.creation_time_ms)

            viewHolderThreeRequest.itemView.postRequestLocation.text = post.locationtext + " - Maps"

            viewHolderThreeRequest.postLocation.setOnClickListener { v:View? ->
                val mapIntent = Intent(context, MapsActivity::class.java)
                var bundle = Bundle()
                bundle.putParcelable("latlng", LatLng(list[position].location!!.latitude, list[position].location!!.longitude))
                mapIntent.putExtra("latlng", bundle)
                context.startActivity(mapIntent)
            }

            viewHolderThreeRequest.contactButton.setOnClickListener { v:View ->
/*
                var poster = firestoreDb.collection("users").document(post.userid)
                poster.get().addOnSuccessListener { documentSnapshot ->
                    if(documentSnapshot.getString("phoneNumber") != "") {
                        var phone = documentSnapshot.getString("phoneNumber")

                        var intent = Intent(Intent.ACTION_DIAL)
                        var temp = "tel:" + phone
                        intent.data = ((Uri.parse(temp)))

                        context.startActivity(intent)
                    }
                }*/
                val intent = Intent(context, MessengerActivity::class.java)
                intent.putExtra("username", posterUname)
                intent.putExtra("friendUid", list[position].userid)
                context.startActivity(intent)

            }

            //If current user = Poster then hide contact button
            if(currentUserId.equals(post.userid))
                viewHolderThreeRequest.contactButton.visibility = View.GONE

            //DELETE POST SECTION
            if(currentUserId == post.userid){
                viewHolderThreeRequest.deleteRequest.visibility = View.VISIBLE
                viewHolderThreeRequest.deleteRequest.setOnClickListener {
                    val builder = AlertDialog.Builder(context)
                    builder.setMessage(R.string.DeletePost)
                        .setCancelable(false)
                        .setPositiveButton(R.string.Yes){ dialog, id ->
                            //DELETE FROM DATABASE METHOD
                            deletePost(position)
                        }
                        .setNegativeButton(R.string.No){ dialog, id ->
                            dialog.dismiss()
                        }
                    val alert = builder.create()
                    alert.show()
                }
            }else{
                viewHolderThreeRequest.deleteRequest.visibility = View.GONE
            }

        }
    }

    override fun getItemCount(): Int = list.size


    private fun deletePost(position: Int){
        //Delete post from Storage
        if(list[position].posturl == null){
            firestoreDb.collection("posts").document(list[position].documentId)
                .delete()
                .addOnCompleteListener{task ->
                    if(task.isSuccessful){
                        list.drop(position)
                        notifyDataSetChanged()
                        Toast.makeText(context,R.string.PostDeleted, Toast.LENGTH_SHORT).show()
                    }else{
                        Toast.makeText(context,R.string.Error, Toast.LENGTH_SHORT).show()
                    }
                }
        }else if(list[position].posturl != null){
            val storage = FirebaseStorage.getInstance().getReferenceFromUrl(list[position].posturl!!)
            storage.delete().addOnSuccessListener {
                //Delete the post from Firestore
                firestoreDb.collection("posts").document(list[position].documentId)
                    .delete()
                    .addOnCompleteListener{task ->
                        if(task.isSuccessful){
                            list.drop(position)
                            notifyDataSetChanged()
                            Toast.makeText(context,R.string.PostDeleted, Toast.LENGTH_SHORT).show()
                        }else{
                            Toast.makeText(context,R.string.Error, Toast.LENGTH_SHORT).show()
                        }
                    }
            }
        }
    }

    interface onUserListener{
        fun onPostClick(position: Int)
    }

}