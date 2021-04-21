package com.soundbliss.PostFragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.hendraanggrian.appcompat.widget.SocialAutoCompleteTextView
import com.soundbliss.MainActivity
import com.soundbliss.R
import com.theartofdev.edmodo.cropper.CropImage
import kotlinx.android.synthetic.main.fragment_photovideo.*


class PhotoVideoFragment : Fragment() {

    private lateinit var imageUri : Uri

    private lateinit var close : ImageView
    private lateinit var imageAdded : ImageView
    private lateinit var textView: TextView
    private lateinit var description : SocialAutoCompleteTextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        close = view!!.findViewById(R.id.close)
        imageAdded = view!!.findViewById(R.id.image_added)
        textView = view!!.findViewById(R.id.post)
        description = view!!.findViewById(R.id.description)

        close.setOnClickListener {
            startActivity(Intent(activity, MainActivity::class.java))
            activity?.finish()
        }

        post.setOnClickListener {
            upload()
        }

        activity?.let { CropImage.activity().start(it) }

        return inflater.inflate(R.layout.fragment_photovideo, container, false)
    }


    private fun upload(){

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && requestCode == AppCompatActivity.RESULT_OK){
            val result : CropImage.ActivityResult = CropImage.getActivityResult(data)
            imageUri = result.uri

            imageAdded.setImageURI(imageUri)
        }else{
            Toast.makeText(activity , "Try Again" , Toast.LENGTH_SHORT).show()
            startActivity(Intent(activity, MainActivity::class.java))
            activity?.finish()
        }
    }

}
