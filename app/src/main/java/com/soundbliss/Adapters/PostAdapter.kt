package com.soundbliss.Adapters

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.soundbliss.MapsActivity
import com.soundbliss.Model.AllPost
import com.soundbliss.Player
import com.soundbliss.R
import kotlinx.android.synthetic.main.item_post_image.view.*
import kotlinx.android.synthetic.main.item_post_request.view.*
import kotlinx.android.synthetic.main.item_post_track.view.*
import java.lang.Exception
import java.util.concurrent.TimeUnit

class PostAdapter(var context: Context, list: List<AllPost>) :
    RecyclerView.Adapter<ViewHolder>() {
    private val TAG = "RecyclerAdapter"
    private var list = list

    private val currentUser = FirebaseAuth.getInstance().currentUser
    private var firestoreDb = FirebaseFirestore.getInstance()

    private inner class ViewHolderOnePhoto(itemView:View): ViewHolder(itemView){
        var deletePhoto = itemView.deleteImagePost
    }

    private inner class ViewHolderTwoTrack(itemView:View): ViewHolder(itemView){
        var postGender = itemView.postTrackGender
        var postTitle = itemView.postTrackName
        var postUsername = itemView.postTrackUserName
        var relativeTime = itemView.postTrackRelativeTime
        var postDescription = itemView.postTrackDescription

        var playBtn = itemView.playBtn

        var deleteTrack = itemView.deleteTrackPost




    }

    private inner class ViewHolderThreeRequest(itemView:View): ViewHolder(itemView){
        var postLocation = itemView.postRequestLocation
        var deleteRequest = itemView.deleteRequestPost
        var contactButton = itemView.postRequestContact

    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view : View

        return if(viewType == 0){
            view = layoutInflater.inflate(R.layout.item_post_image, parent,false)
            ViewHolderOnePhoto(view)
        }else if(viewType == 1){
            view = layoutInflater.inflate(R.layout.item_post_track,parent, false)
            ViewHolderTwoTrack(view)
        }else {
            view = layoutInflater.inflate(R.layout.item_post_request, parent, false)
            ViewHolderThreeRequest(view)
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

                viewHolderOne.itemView.postPhotoDescription.text = post.description
                viewHolderOne.itemView.postPhotoUserName.text = post.username
                viewHolderOne.itemView.postPhotoRelativeTime.text = DateUtils.getRelativeTimeSpanString(post.creation_time_ms)
                Glide.with(context).load(post.posturl).into(viewHolderOne.itemView.postPhotoImageView)

            if(currentUser!!.uid == post.userid){
                viewHolderOne.deletePhoto.visibility = View.VISIBLE
                viewHolderOne.deletePhoto.setOnClickListener {
                    val builder = AlertDialog.Builder(context)
                    builder.setMessage("Delete post?")
                        .setCancelable(false)
                        .setPositiveButton("Yes"){ dialog, id ->
                            //DELETE FROM DATABASE METHOD
                            deletePost(position)
                        }
                        .setNegativeButton("No"){ dialog, id ->
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

            viewHolderTwo.postGender.text = post.gender
            viewHolderTwo.postTitle.text = post.title
            viewHolderTwo.postUsername.text = post.username
            viewHolderTwo.relativeTime.text = DateUtils.getRelativeTimeSpanString(post.creation_time_ms)
            viewHolderTwo.postDescription.text = post.description

            viewHolderTwo.playBtn.setOnClickListener { v:View? ->
                val intent = Intent(context, Player::class.java)
                var trackBundle = Bundle()
                trackBundle.putSerializable("post",post)
                intent.putExtra("Post", trackBundle)
                context.startActivity(intent)
            }


            //Delete Button
            if(currentUser!!.uid == post.userid){
                viewHolderTwo.deleteTrack.visibility = View.VISIBLE
                viewHolderTwo.deleteTrack.setOnClickListener {
                    val builder = AlertDialog.Builder(context)
                    builder.setMessage("Delete post?")
                        .setCancelable(false)
                        .setPositiveButton("Yes"){ dialog, id ->
                            //DELETE FROM DATABASE METHOD
                            deletePost(position)
                        }
                        .setNegativeButton("No"){ dialog, id ->
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

            viewHolderThreeRequest.itemView.postRequestTitle.text = post.title
            viewHolderThreeRequest.itemView.postRequestGender.text = post.gender
            viewHolderThreeRequest.itemView.postRequestDescription.text = post.description
            viewHolderThreeRequest.itemView.postRequestUserName.text = post.username
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

            //If current user = Poster then hide contact button
            if(currentUser!!.uid.equals(post.userid))
                viewHolderThreeRequest.contactButton.visibility = View.GONE

            //DELETE POST SECTION
            if(currentUser!!.uid == post.userid){
                viewHolderThreeRequest.deleteRequest.visibility = View.VISIBLE
                viewHolderThreeRequest.deleteRequest.setOnClickListener {
                    val builder = AlertDialog.Builder(context)
                    builder.setMessage("Delete post?")
                        .setCancelable(false)
                        .setPositiveButton("Yes"){ dialog, id ->
                            //DELETE FROM DATABASE METHOD
                            deletePost(position)
                        }
                        .setNegativeButton("No"){ dialog, id ->
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

    @SuppressLint("DefaultLocale")
    private fun convertFormat(duration:Long):String{
        return String.format("%02d:0%2d",
            TimeUnit.MILLISECONDS.toMinutes(duration)
            , TimeUnit.MILLISECONDS.toSeconds(duration) -
                    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(duration)))
    }


    private fun deletePost(position: Int){
        firestoreDb.collection("posts").document(list.get(position).documentId)
            .delete()
            .addOnCompleteListener{task ->
                if(task.isSuccessful){
                    list.drop(position)
                    notifyDataSetChanged()
                    Toast.makeText(context,"Item Deleted", Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(context,"Error" + task.exception!!.message, Toast.LENGTH_SHORT).show()
                }
            }
    }



}