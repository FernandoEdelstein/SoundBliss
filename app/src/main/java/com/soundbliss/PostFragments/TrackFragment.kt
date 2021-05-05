package com.soundbliss.PostFragments

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.soundbliss.R
import kotlinx.android.synthetic.main.fragment_track.*

class TrackFragment : Fragment() {



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_track, container, false)
    }

    fun pickAudioFile(){
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.setType("audio/*")
        intent.setAction(Intent.ACTION_GET_CONTENT)
        startActivityForResult(Intent.createChooser(intent,"Select Track"),1)
    }
/*
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode == 1 && requestCode == RESULT_OK) val uri: Uri = data?.data!!
        super.onActivityResult(requestCode, resultCode, data)
    }*/
}