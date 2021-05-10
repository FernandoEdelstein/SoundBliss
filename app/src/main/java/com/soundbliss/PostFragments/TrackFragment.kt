package com.soundbliss.PostFragments

import android.app.Activity
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.content.ContextCompat
import com.soundbliss.R
import kotlinx.android.synthetic.main.fragment_track.view.*
import java.io.File
import android.content.Intent as Intent

class TrackFragment : Fragment() {

    private lateinit var uploadButton : Button
    private lateinit var filePath : Uri


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        uploadButton = inflater.inflate(R.layout.fragment_track,container,false).findViewById(R.id.uploadTrack)
        uploadButton.setOnClickListener {
            pickAudioFile()
        }

        val view:View = inflater!!.inflate(R.layout.fragment_track,container,false)

        view.uploadTrack.setOnClickListener { view ->
            Log.d("btnSetup", "Selected")
        }

        return view

        //return inflater.inflate(R.layout.fragment_track, container, false)



    }

    fun pickAudioFile(){
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "audio/*"
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        startActivityForResult(Intent.createChooser(intent,"Choose Audio File"), 111)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == 111 && resultCode == Activity.RESULT_OK && data != null){
            filePath = data.data!!
        }

    }

}