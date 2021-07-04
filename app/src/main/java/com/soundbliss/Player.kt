package com.soundbliss

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.text.format.DateUtils
import android.view.View
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import androidx.core.net.toUri
import com.soundbliss.Model.AllPost
import org.w3c.dom.Text
import java.lang.Exception
import java.util.concurrent.TimeUnit

class Player : AppCompatActivity() {
    private lateinit var post : AllPost

    //Track Player
    private lateinit var playerPosition : TextView
    private lateinit var playerDuration : TextView
    private lateinit var seekBar : SeekBar
    private lateinit var btPlay : ImageView
    private lateinit var btPause : ImageView

    private lateinit var mediaPlayer : MediaPlayer
    private lateinit var handler : Handler
    private lateinit var runnable: Runnable

    private lateinit var username : TextView

    private lateinit var postTime : TextView
    private lateinit var trackName : TextView
    private lateinit var trackGender : TextView

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.stop()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_media_player)

        this.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        val bundle = intent.getBundleExtra("Post")
        post = bundle!!.getSerializable("post") as AllPost

        username = findViewById(R.id.username_item_player)

        postTime = findViewById(R.id.postTime_player)
        trackName = findViewById(R.id.trackName_player)
        trackGender = findViewById(R.id.trackGender_player)

        username.text = post.username
        trackName.text = post.title
        trackGender.text = post.gender
        postTime.text = DateUtils.getRelativeTimeSpanString(post.creation_time_ms)

        //Track player Assignment
        playerDuration = findViewById(R.id.player_duration)
        playerPosition = findViewById(R.id.player_position)
        seekBar = findViewById(R.id.seekBar)
        btPause = findViewById(R.id.btn_pause)
        btPlay = findViewById(R.id.btn_play)

        val trackUri = post.posturl!!.toUri()
        //Media Player
        mediaPlayer = MediaPlayer()

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

        mediaPlayer = MediaPlayer.create(this,trackUri)

        var duration = mediaPlayer.duration
        var stDuration : String = convertFormat(duration.toLong())
        playerDuration.text = stDuration

        handler = Handler()

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

    @SuppressLint("DefaultLocale")
    private fun convertFormat(duration:Long):String{
        return String.format("%02d:0%2d",
            TimeUnit.MILLISECONDS.toMinutes(duration)
            , TimeUnit.MILLISECONDS.toSeconds(duration) -
                    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(duration)))
    }
}