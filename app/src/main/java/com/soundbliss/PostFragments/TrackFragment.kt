package com.soundbliss.PostFragments

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentValues.TAG
import android.content.pm.PackageManager
import android.media.Image
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import android.widget.SeekBar.OnSeekBarChangeListener
import androidx.core.content.ContextCompat
import androidx.core.os.postDelayed
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.soundbliss.MainActivity
import com.soundbliss.Model.Post
import com.soundbliss.Model.User
import com.soundbliss.PostActivity
import com.soundbliss.R
import kotlinx.android.synthetic.main.activity_post.*
import kotlinx.android.synthetic.main.fragment_track.*
import kotlinx.android.synthetic.main.fragment_track.view.*
import org.w3c.dom.Text
import java.io.File
import java.lang.Exception
import java.sql.Time
import java.util.*
import java.util.concurrent.TimeUnit
import android.content.Intent as Intent

private const val TAG = "TrackFragment"
private const val PICK_AUDIO_CODE = 1234

class TrackFragment : Fragment() {

    private var signedInUser : User? = null
    private lateinit var upTrack : Button
    private lateinit var trackUri : Uri
    private lateinit var postTxt : TextView


    //Track Player
    private lateinit var playerPosition : TextView
    private lateinit var playerDuration : TextView
    private lateinit var seekBar : SeekBar
    private lateinit var btPlay : ImageView
    private lateinit var btPause : ImageView

    private lateinit var mediaPlayer : MediaPlayer
    private lateinit var handler : Handler
    private lateinit var runnable: Runnable


    //Database
    private var mTrackPostReference: DatabaseReference? = FirebaseDatabase.getInstance().getReference("posts/trackpost")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater!!.inflate(R.layout.fragment_track, container, false)


        //Track player Assignment
        playerDuration = view.findViewById(R.id.player_duration)
        playerPosition = view.findViewById(R.id.player_position)
        seekBar = view.findViewById(R.id.seekBar)
        btPause = view.findViewById(R.id.btn_pause)
        btPlay = view.findViewById(R.id.btn_play)


        mediaPlayer = MediaPlayer()



        btPlay.setOnClickListener { v: View? ->
            btPlay.visibility = View.GONE
            btPause.visibility = View.VISIBLE
            mediaPlayer.start()
            seekBar.max = mediaPlayer.duration
            //handler.postDelayed(runnable,0)
        }
        btPause.setOnClickListener { v: View ->
            btPause.visibility = View.GONE
            btPlay.visibility = View.VISIBLE
            mediaPlayer.pause()
            //handler.removeCallbacks(runnable)
        }



        //
        //SIGNED USER METHOD
        //

        upTrack = view.findViewById(R.id.uploadTrack)

        upTrack.setOnClickListener { v: View? ->
            pickAudioFile()
        }

        return view
    }



@SuppressLint("DefaultLocale")
    private fun convertFormat(duration:Long):String{
        return String.format("%02d:0%2d",
                    TimeUnit.MILLISECONDS.toMinutes(duration)
                    ,TimeUnit.MILLISECONDS.toSeconds(duration) -
                    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(duration)))
    }

    private fun pickAudioFile(){
        val audioPickerIntent = Intent(Intent.ACTION_GET_CONTENT)
        audioPickerIntent.type = "audio/*"
        startActivityForResult(audioPickerIntent, PICK_AUDIO_CODE)
    }

    private fun checkParam() {
        if(trackUri == null){
            Toast.makeText(context, "No Track Selected", Toast.LENGTH_SHORT).show()
            return
        }
        if(trackGender.text == null){
            Toast.makeText(context, "Please Select Gender", Toast.LENGTH_SHORT).show()
            return
        }
        if(trackTitle.text == null){
            Toast.makeText(context, "Please Set title", Toast.LENGTH_SHORT).show()
            return
        }
        if(signedInUser == null){
            Toast.makeText(context,"No signed in user", Toast.LENGTH_SHORT).show()
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == PICK_AUDIO_CODE){
            if(resultCode == Activity.RESULT_OK){
                trackUri = data?.data!!
                Log.i(TAG, "Audio $trackUri")
                trackName.visibility = View.VISIBLE

                var file : File = File(trackUri.path)
                trackName.text = file.name

                trackPlayer.visibility = View.VISIBLE
                mediaPlayer = MediaPlayer.create(context,trackUri)

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


            }else{
                Toast.makeText(context,"Audio Pick Cancelled", Toast.LENGTH_SHORT).show()
            }

        }

    }

    private fun uploadTrack(){
        //mTrackPostReference!!.setValue()
    }

}