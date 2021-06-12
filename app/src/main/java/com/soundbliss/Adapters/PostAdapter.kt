package com.soundbliss.Adapters


import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.location.Geocoder
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.text.format.DateUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.gms.common.internal.Constants
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.stats.CodePackage.LOCATION
import com.google.common.net.HttpHeaders.LOCATION
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.GeoPoint
import com.mypopsy.maps.StaticMap
import com.soundbliss.MapsActivity
import com.soundbliss.Model.AllPost
import com.soundbliss.R
import kotlinx.android.synthetic.main.item_post_image.view.*
import kotlinx.android.synthetic.main.item_post_request.view.*
import kotlinx.android.synthetic.main.item_post_track.view.*
import java.lang.Exception
import java.util.concurrent.TimeUnit
import androidx.fragment.app.Fragment as fragment


class PostAdapter(var context: Context, list: List<AllPost>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val TAG = "RecyclerAdapter"
    private var list = list

    private val currentUser = FirebaseAuth.getInstance().currentUser

    private inner class ViewHolderOnePhoto(itemView:View):RecyclerView.ViewHolder(itemView){

    }

    private inner class ViewHolderTwoTrack(itemView:View):RecyclerView.ViewHolder(itemView){
        var postGender = itemView.postTrackGender
        var postTitle = itemView.postTrackName
        var postUsername = itemView.postTrackUserName
        var relativeTime = itemView.postTrackRelativeTime
        var postDescription = itemView.postTrackDescription


        var playerDuration = itemView.player_duration
        var playerPosition = itemView.player_position
        var seekBar = itemView.seekBar
        var btPause = itemView.btn_pause
        var btPlay = itemView.btn_play


    }

    private inner class ViewHolderThreeRequest(itemView:View):RecyclerView.ViewHolder(itemView){
        var postLocation = itemView.postRequestLocation

        var deleteRequest = itemView.deleteRequestPost
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
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
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int){

        if(getItemViewType(position) == 0){
            val post = list[position]
            val viewHolderOne = holder as ViewHolderOnePhoto

                viewHolderOne.itemView.postPhotoDescription.text = post.description
                viewHolderOne.itemView.postPhotoUserName.text = post.username
                viewHolderOne.itemView.postPhotoRelativeTime.text = DateUtils.getRelativeTimeSpanString(post.creation_time_ms)
                Glide.with(context).load(post.posturl).into(viewHolderOne.itemView.postPhotoImageView)

        }else if(getItemViewType(position) == 1){
            val post = list.get(position)

            val viewHolderTwo = holder as ViewHolderTwoTrack

            viewHolderTwo.postGender.text = post.gender
            viewHolderTwo.postTitle.text = post.title
            viewHolderTwo.postUsername.text = post.username
            viewHolderTwo.relativeTime.text = DateUtils.getRelativeTimeSpanString(post.creation_time_ms)
            viewHolderTwo.postDescription.text = post.description

            //Media Player
            var mediaPlayer = MediaPlayer()

            //Play/Pause
            viewHolderTwo.btPlay.setOnClickListener { v: View? ->
                viewHolderTwo.btPlay.visibility = View.GONE
                viewHolderTwo.btPause.visibility = View.VISIBLE
                mediaPlayer.start()
                viewHolderTwo.seekBar.max = mediaPlayer.duration
            }
            viewHolderTwo.btPause.setOnClickListener { v: View ->
                viewHolderTwo.btPause.visibility = View.GONE
                viewHolderTwo.btPlay.visibility = View.VISIBLE
                mediaPlayer.pause()
            }

            mediaPlayer = MediaPlayer.create(context, Uri.parse(post.posturl))

            val duration = mediaPlayer.duration
            val stDuration : String = convertFormat(duration.toLong())
            viewHolderTwo.playerDuration.text = stDuration

            val handler = Handler()

            handler.postDelayed(object : Runnable{
                override fun run() {
                    try{
                        viewHolderTwo.seekBar.progress = mediaPlayer.currentPosition
                        handler.postDelayed(this, 1000 )
                    }catch (e : Exception){
                        viewHolderTwo.seekBar.progress = 0
                    }
                }
            },0)

            viewHolderTwo.seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
                override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                    if(fromUser){
                        mediaPlayer.seekTo(progress)
                    }
                    viewHolderTwo.playerPosition.text = convertFormat(mediaPlayer.currentPosition.toLong())
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {
                }

                override fun onStopTrackingTouch(seekBar: SeekBar?) {
                }

            })

            mediaPlayer.setOnCompletionListener ( object: MediaPlayer.OnCompletionListener {
                override fun onCompletion(mp: MediaPlayer?) {
                    viewHolderTwo.btPause.visibility = View.GONE
                    viewHolderTwo.btPlay.visibility = View.VISIBLE
                    mediaPlayer.seekTo(0)
                }
            })

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

            if(currentUser!!.uid == post.userid){
                viewHolderThreeRequest.deleteRequest.visibility = View.VISIBLE
                viewHolderThreeRequest.deleteRequest.setOnClickListener {
                    val builder = AlertDialog.Builder(context)
                    builder.setMessage("Delete post?")
                        .setCancelable(false)
                        .setPositiveButton("Yes"){ dialog, id ->
                            //DELETE FROM DATABASE METHOD

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

    
}