package com.soundbliss.Adapters

import android.annotation.SuppressLint
import android.content.Context
import android.media.MediaPlayer
import android.media.MediaPlayer.create
import android.net.Uri
import android.os.Handler
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.soundbliss.Model.TrackPost
import com.soundbliss.R
import kotlinx.android.synthetic.main.item_post_track.view.*
import java.lang.Exception
import java.util.concurrent.TimeUnit

class TrackPostAdapter (val context:Context, val posts: List<TrackPost>) :
    RecyclerView.Adapter<TrackPostAdapter.ViewHolder>(){



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_post_track,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount() = posts.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindTrack(posts[position])
    }

    inner class ViewHolder (itemView: View): RecyclerView.ViewHolder(itemView){
        fun bindTrack (post: TrackPost){
            itemView.postTrackGender.text = post.gender
            itemView.postTrackName.text = post.title
            itemView.postTrackUserName.text = post.username
            itemView.postTrackRelativeTime.text = DateUtils.getRelativeTimeSpanString(post.creation_time_ms)
            itemView.postTrackDescription.text = post.description

            //Track player Assignment
            var playerDuration = itemView.player_duration
            var playerPosition = itemView.player_position
            var seekBar = itemView.seekBar
            var btPause = itemView.btn_pause
            var btPlay = itemView.btn_play


            //Media Player
            var mediaPlayer = MediaPlayer()

            //Play/Pause
            btPlay.setOnClickListener { v: View? ->
                btPlay.visibility = View.GONE
                btPause.visibility = View.VISIBLE
                mediaPlayer.start()
                seekBar.max = mediaPlayer.duration
            }
            btPause.setOnClickListener { v: View ->
                btPause.visibility = View.GONE
                btPlay.visibility = View.VISIBLE
                mediaPlayer.pause()
            }

            mediaPlayer = MediaPlayer.create(context, Uri.parse(post.posturl))

            var duration = mediaPlayer.duration
            var stDuration : String = convertFormat(duration.toLong())
            playerDuration.text = stDuration

            var handler = Handler()

            handler.postDelayed(object : Runnable{
                override fun run() {
                    try{
                        seekBar.progress = mediaPlayer.currentPosition
                        handler.postDelayed(this, 1000 )
                    }catch (e : Exception){
                        seekBar.progress = 0
                    }
                }
            },0)

            seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
                override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                    if(fromUser){
                        mediaPlayer.seekTo(progress)
                    }
                    playerPosition.text = convertFormat(mediaPlayer.currentPosition.toLong())
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {
                }

                override fun onStopTrackingTouch(seekBar: SeekBar?) {
                }

            })

            mediaPlayer.setOnCompletionListener ( object: MediaPlayer.OnCompletionListener {
                override fun onCompletion(mp: MediaPlayer?) {
                    btPause.visibility = View.GONE
                    btPlay.visibility = View.VISIBLE
                    mediaPlayer.seekTo(0)
                }
            })

        }
    }

    @SuppressLint("DefaultLocale")
    private fun convertFormat(duration:Long):String{
        return String.format("%02d:0%2d",
            TimeUnit.MILLISECONDS.toMinutes(duration)
            , TimeUnit.MILLISECONDS.toSeconds(duration) -
                    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(duration)))
    }

}




