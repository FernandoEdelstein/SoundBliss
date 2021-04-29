package com.soundbliss.PostFragments

import android.content.Intent
import android.content.ContentResolver
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.SurfaceControl
import android.view.View
import android.view.ViewGroup
import android.webkit.MimeTypeMap
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageTask
import com.hendraanggrian.appcompat.widget.SocialAutoCompleteTextView
import com.soundbliss.MainActivity
import com.soundbliss.PostActivity
import com.soundbliss.R
import com.theartofdev.edmodo.cropper.CropImage
import kotlinx.android.synthetic.main.fragment_photovideo.*


class PhotoVideoFragment : Fragment() {

    private lateinit var imageUri : Uri
    private lateinit var imageUrl : String
    private lateinit var imageAdded : ImageView
    private lateinit var description : SocialAutoCompleteTextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        imageAdded = inflater.inflate(R.layout.fragment_photovideo,container,false).findViewById(R.id.image_added)
        description = inflater.inflate(R.layout.fragment_photovideo,container,false).findViewById(R.id.description)

        return inflater.inflate(R.layout.fragment_photovideo, container, false)
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

    fun upload(){
        if(imageUri != null){
            var filePath = FirebaseStorage.getInstance().getReference("Posts").child("${System.currentTimeMillis()}" + "." + getFileExtension(imageUri))

            var uploadTask = filePath.putFile(imageUri)
            uploadTask.continueWithTask {task ->
                if(!task.isSuccessful)
                    throw task.exception!!

                return@continueWithTask filePath.downloadUrl
            }.addOnCompleteListener{task ->

                var downloadurl : Uri? = task.result
                imageUrl = downloadurl.toString()

                var ref : DatabaseReference = FirebaseDatabase.getInstance().getReference("Posts")
                var postId : String = ref.push().key.toString()

                var map : HashMap<String, Any> = HashMap()
                map.put("PostId" , postId)
                map.put("ImageURL" , imageUrl)
                map.put("Description" , description.text)
                map.put("Publisher" , FirebaseAuth.getInstance().currentUser.uid)

                ref.child(postId).setValue(map)

                var mHashtagRef : DatabaseReference = FirebaseDatabase.getInstance().getReference().child("HashTags")
                var hashtags = listOf<String>()
                hashtags = description.hashtags

                if(!hashtags.isEmpty()){
                    hashtags.forEach(){
                        map.clear()
                        map.put("tag" , tag!!.toLowerCase())
                        map.put("postid" , postId)

                        mHashtagRef.child(tag!!.toLowerCase()).setValue(map)
                    }
                }

                startActivity(Intent(activity ,MainActivity::class.java))
                activity?.finish()
            }.addOnFailureListener{task ->
                Toast.makeText(activity, "Failure" , Toast.LENGTH_SHORT).show()
            }
        }else{
            Toast.makeText(activity, "No Image Added" , Toast.LENGTH_SHORT).show()
        }
    }


    fun getFileExtension(uri: Uri) : String? {
        return MimeTypeMap.getSingleton().getExtensionFromMimeType(this.context?.contentResolver?.getType(uri))
    }

}
