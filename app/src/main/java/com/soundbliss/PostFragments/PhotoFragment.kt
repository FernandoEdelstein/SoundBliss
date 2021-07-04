package com.soundbliss.PostFragments

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.MimeTypeMap
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import com.canhub.cropper.CropImage
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.hendraanggrian.appcompat.widget.SocialAutoCompleteTextView
import com.soundbliss.MainActivity
import com.soundbliss.Model.AllPost
import com.soundbliss.PostActivity
import com.soundbliss.R
import kotlinx.android.synthetic.main.fragment_photovideo.*


class PhotoFragment(userid: String, username : String, userpic: String) : Fragment() {
    private val TAG = "PhotoVideoFragment"
    private var imageUri : Uri? = null
    private lateinit var imageAdded : ImageView
    private lateinit var submit : Button

    private var userid = userid
    private var username = username
    private var userpic = userpic

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
                Toast.makeText(activity, R.string.TryAgain, Toast.LENGTH_SHORT).show()
                startActivity(Intent(activity, MainActivity::class.java))
                requireActivity().finish()
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
            .continueWithTask {photoUploadTask ->
                Log.i(TAG, "Uploaded bytes: ${photoUploadTask.result?.bytesTransferred}")
                photoReference.downloadUrl
            }.continueWithTask { downloadUrlTask ->
                val docref = firestoreDb.collection("posts/").document()
                    val post = AllPost(
                        System.currentTimeMillis(),
                        description.text.toString(),
                        downloadUrlTask.result.toString(),
                        userid,
                        username
                    )

                    //Set PostId to the post itself so that later it can be eliminated faster
                    post.setDocumentId(docref.id)


                docref.set(post)
            }.addOnCompleteListener { postCreationTask ->
                photoPostSubmit.isEnabled = true

                if(!postCreationTask.isSuccessful){
                    Log.e(TAG, "Exception during firestore operations" , postCreationTask.exception)
                    Toast.makeText(context,R.string.FailedToSavePost,Toast.LENGTH_SHORT).show()
                }else{
                    val profileIntent = Intent(activity,MainActivity::class.java)
                    startActivity(profileIntent)
                    requireActivity().finish()
                }
            }
    }


    private fun getFileExtension(uri: Uri) : String? {
        return MimeTypeMap.getSingleton().getExtensionFromMimeType(requireContext().contentResolver?.getType(uri))
    }

}
