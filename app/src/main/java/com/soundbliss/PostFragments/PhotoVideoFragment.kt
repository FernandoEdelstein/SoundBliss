package com.soundbliss.PostFragments

import android.app.Activity
import android.content.Intent
import android.content.ContentResolver
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.SurfaceControl
import android.view.View
import android.view.ViewGroup
import android.webkit.MimeTypeMap
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.canhub.cropper.CropImage
import com.canhub.cropper.CropImageView
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.StorageTask
import com.hendraanggrian.appcompat.widget.SocialAutoCompleteTextView
import com.soundbliss.MainActivity
import com.soundbliss.Model.AllPost
import com.soundbliss.Model.Post
import com.soundbliss.PostActivity
import com.soundbliss.R
import kotlinx.android.synthetic.main.fragment_photovideo.*


class PhotoVideoFragment : Fragment() {
    private val TAG = "PhotoVideoFragment"
    private var imageUri : Uri? = null
    private lateinit var imageAdded : ImageView
    private lateinit var descriptiontx : SocialAutoCompleteTextView
    private lateinit var submit : Button


    //Database
    private lateinit var firestoreDb : FirebaseFirestore
    private lateinit var storageReference: StorageReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.fragment_photovideo, container, false)

        storageReference = FirebaseStorage.getInstance().reference
        firestoreDb = FirebaseFirestore.getInstance()

        imageAdded = view.findViewById(R.id.image_added)
        imageAdded.setOnClickListener {
            CropImage.activity().start(requireContext(), this)
        }

        submit = view.findViewById(R.id.photoPostSubmit)
        submit.setOnClickListener {
            uploadImage()
        }

        return view
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            if (resultCode == Activity.RESULT_OK) {
                val resultUri: Uri? = result?.uriContent
                val resultFilePath: String? = result?.getUriFilePath(requireContext())

                imageUri = resultUri
                imageAdded.setImageURI(imageUri)

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Toast.makeText(activity, "Try Again", Toast.LENGTH_SHORT).show()
                startActivity(Intent(activity, MainActivity::class.java))
                activity!!.finish()
            }
        }
    }

    private fun uploadImage(){
        if(imageUri == null){
            Toast.makeText(context,"No Photo Selected", Toast.LENGTH_SHORT).show()
            return
        }

        photoPostSubmit.isEnabled = false

        val photoUploadUri = imageUri as Uri

        val photoReference = storageReference.child("Posts/Photo/${System.currentTimeMillis()}-photo." + getFileExtension(imageUri!!))
        photoReference.putFile(photoUploadUri)
            .continueWith {photoUploadTask ->
                Log.i(TAG, "Uploaded bytes: ${photoUploadTask.result?.bytesTransferred}")
                photoReference.downloadUrl
            }.continueWithTask { downloadUrlTask ->
                    val post = AllPost(
                        System.currentTimeMillis(),
                        description.text.toString(),
                        downloadUrlTask.result.toString(),
                        "123",
                        "Ferna3138"
                    )
                firestoreDb.collection("posts/").add(post)
            }.addOnCompleteListener { postCreationTask ->
                photoPostSubmit.isEnabled = true

                if(!postCreationTask.isSuccessful){
                    Log.e(TAG, "Exception during firestore operations" , postCreationTask.exception)
                    Toast.makeText(context,"Failed to save post",Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(context,"Success" , Toast.LENGTH_SHORT).show()
                    val profileIntent = Intent(activity,MainActivity::class.java)
                    startActivity(profileIntent)
                    activity!!.finish()
                }
            }
    }


    private fun getFileExtension(uri: Uri) : String? {
        return MimeTypeMap.getSingleton().getExtensionFromMimeType(context!!.contentResolver?.getType(uri))
    }

}
