package com.soundbliss.Adapters

import android.annotation.SuppressLint
import android.content.Context
import android.media.MediaPlayer
import android.net.Uri
import android.os.Handler
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.soundbliss.Model.Post
import com.soundbliss.Model.PostSuperClass
import com.soundbliss.Model.TrackPost
import com.soundbliss.R
import kotlinx.android.synthetic.main.item_post_image.view.*
import kotlinx.android.synthetic.main.item_post_track.view.*
import org.w3c.dom.Text
import java.lang.Exception
import java.util.concurrent.TimeUnit


class PostAdapter(context: Context, list: List<PostSuperClass>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    private val TAG = "RecyclerAdapter"
    var list = list
    var context = context

    private inner class ViewHolderOnePhoto(itemView:View):RecyclerView.ViewHolder(itemView){
        var photoDescription = itemView.postPhotoDescription
        var imageUrl = itemView.postPhotoImageView
        var photoUsername = itemView.postPhotoUserName
        var photoRelativeTime = itemView.postPhotoRelativeTime

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

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        var layoutInflater = LayoutInflater.from(parent.context)
        var view : View

        if(viewType == 0){
            view = layoutInflater.inflate(R.layout.item_post_image, parent,false)
            return ViewHolderOnePhoto(view)
        }else if(viewType == 1){
            view = layoutInflater.inflate(R.layout.item_post_track,parent, false)
            return ViewHolderTwoTrack(view)
        }
        view = layoutInflater.inflate(R.layout.item_post_request,parent,false)
        return ViewHolderThreeRequest(view)

    }

    override fun getItemViewType(position: Int): Int {
        if(list.get(position) is Post){
            return 0
        }else if(list.get(position) is TrackPost){
            return 1
        }else
            return 2
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(list.get(position) is Post){
            var post = list.get(position) as Post
            var viewHolderOne = holder as ViewHolderOnePhoto

            viewHolderOne.photoDescription.text = post.description
            viewHolderOne.photoUsername.text = post.username
            viewHolderOne.photoRelativeTime.text = DateUtils.getRelativeTimeSpanString(post.creation_time_ms)
            Glide.with(context).load(post.posturl).into(viewHolderOne.imageUrl)

        }else if(list.get(position) is TrackPost){
            var post = list.get(position) as TrackPost

            var viewHolderTwo = holder as ViewHolderTwoTrack

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

            var duration = mediaPlayer.duration
            var stDuration : String = convertFormat(duration.toLong())
            viewHolderTwo.playerDuration.text = stDuration

            var handler = Handler()

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

        }else{
            var viewHolderThreeRequest = holder as ViewHolderThreeRequest
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