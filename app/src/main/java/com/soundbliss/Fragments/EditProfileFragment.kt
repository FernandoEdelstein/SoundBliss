package com.soundbliss.Fragments

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.MimeTypeMap
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.replace
import com.bumptech.glide.Glide
import com.canhub.cropper.CropImage
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuth.getInstance
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.soundbliss.Login.LogIn
import com.soundbliss.MainActivity
import com.soundbliss.Model.AllPost
import com.soundbliss.R
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.fragment_edit_profile.*
import kotlinx.android.synthetic.main.fragment_edit_profile.view.*
import kotlinx.android.synthetic.main.fragment_photovideo.*


class EditProfileFragment : Fragment() {
    private val TAG = "EditProfileFragment"
    //firebase

    private lateinit var auth: FirebaseAuth

    //Database
    private lateinit var firestoreDb: FirebaseFirestore
    private lateinit var document: DocumentReference
    private lateinit var storageReference: StorageReference

    //EditProfile Fragment
    private lateinit var mPassword: EditText
    private lateinit var mDescription: EditText  //EditProfile Fragment widgets
    private lateinit var mEmail: EditText//EditProfile Fragment widgets
    private lateinit var mChangeProfilePhoto: TextView
    private lateinit var mUsername: EditText

    private var mProfilePhoto: CircleImageView? = null
    private var back: ImageView? = null
    private var checkMark: ImageView? = null
    private var userId: String? = null
    private lateinit var logOutButton: Button

    private var imageUri : Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_edit_profile, container, false)
        auth = getInstance()
        userId = auth.currentUser!!.uid

        mUsername = view.findViewById(R.id.mUsername)
        mProfilePhoto = view.findViewById(R.id.profile_photo) as CircleImageView
        mDescription = view.findViewById(R.id.description) as EditText
        mEmail = view.findViewById(R.id.email) as EditText
        mPassword = view.findViewById(R.id.password) as EditText
        mChangeProfilePhoto = view.findViewById(R.id.changeProfilePhoto) as TextView
        back = view.findViewById(R.id.backArrow) as ImageView
        checkMark = view.findViewById(R.id.saveChanges) as ImageView

        firestoreDb = FirebaseFirestore.getInstance()
        document = firestoreDb.collection("users").document(userId!!)
        storageReference = FirebaseStorage.getInstance().reference

        document.get().addOnSuccessListener { documentSnapshot ->
            mDescription.hint = documentSnapshot.getString("bio")
            mEmail.hint = documentSnapshot.getString("mail")
            mUsername.hint = documentSnapshot.getString("uname")

            if(documentSnapshot.getString("imageu") != ""){
                Picasso.get().load(documentSnapshot.getString("imageu")).into(mProfilePhoto)
            }
        }
        //LOG OUT BUTTON
        logOutButton = view.logoutButton

        logOutButton.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            startActivity(Intent(requireContext(),LogIn::class.java))
            requireActivity().finish()
        }

        //setupFirebaseAuth()
        back!!.setOnClickListener {
            Log.d(TAG, "onClick: navigating back to FragmentProfile")
            requireActivity().supportFragmentManager.beginTransaction().apply{
                hide(this@EditProfileFragment)
                replace(R.id.fragment_container, ProfileFragment())
                commit()
            }
        }

        checkMark!!.setOnClickListener {
            saveChangesSetting()
        }

        mChangeProfilePhoto.setOnClickListener {
            CropImage.activity().start(requireContext(), this)
        }

        return view
    }

    private fun saveChangesSetting() {
        val username = mUsername.text.toString()
        val description = mDescription.text.toString()
        val email = mEmail.text.toString()
        val password = mPassword.text.toString()

        if(username.isNotEmpty()) {
            document.update(mapOf("uname" to username))
            Toast.makeText(this.context, R.string.Username, Toast.LENGTH_SHORT).show()
        }

        if(description.isNotEmpty()) {
            document.update(mapOf("bio" to description))
            Toast.makeText(this.context, R.string.Description, Toast.LENGTH_SHORT).show()
        }
        if(email.isNotEmpty()) {
            if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                document.update(mapOf("mail" to email))
                auth.currentUser!!.updateEmail(mEmail.toString())
                Toast.makeText(this.context, R.string.Email, Toast.LENGTH_SHORT).show()
            }
        }
        if(password.isNotEmpty()) {
           auth.currentUser!!.updatePassword(mPassword.toString())
            Toast.makeText(this.context, R.string.Pass, Toast.LENGTH_SHORT).show()
        }

        if(imageUri != null){
            val user = document.get().addOnSuccessListener { documentSnapshot ->
                if(documentSnapshot.getString("imageu") != ""){
                    // If there was already an Image, then delete it
                    val storage = FirebaseStorage.getInstance().getReferenceFromUrl(documentSnapshot.getString("imageu").toString())
                    storage.delete().addOnSuccessListener {
                        uploadImg()
                    }
                }else{
                    //If there wasn't then just upload it
                    uploadImg()
                }
            }
        }

        requireActivity().supportFragmentManager.beginTransaction().apply {
            hide(this@EditProfileFragment)
            replace(R.id.fragment_container, ProfileFragment())
            commit()
        }

    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            if (resultCode == Activity.RESULT_OK) {
                val resultUri: Uri? = result?.uriContent
                val resultFilePath: String? = result?.getUriFilePath(requireContext())

                imageUri = resultUri
                mProfilePhoto!!.setImageURI(imageUri)

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Toast.makeText(activity, R.string.TryAgain, Toast.LENGTH_SHORT).show()
                startActivity(Intent(activity, MainActivity::class.java))
                requireActivity().finish()
            }
        }
    }

    private fun getFileExtension(uri: Uri) : String? {
        return MimeTypeMap.getSingleton().getExtensionFromMimeType(requireContext().contentResolver?.getType(uri))
    }

    private fun uploadImg(){
        val photoUploadUri = imageUri as Uri

        val photoReference = storageReference.child("Images/${System.currentTimeMillis()}-photo." + getFileExtension(imageUri!!))
        photoReference.putFile(photoUploadUri)
            .continueWithTask {photoUploadTask ->
                Log.i(TAG, "Uploaded bytes: ${photoUploadTask.result?.bytesTransferred}")
                photoReference.downloadUrl
            }.continueWithTask { downloadUrlTask ->
                document.update(mapOf("imageu" to downloadUrlTask.result.toString()))
            }
    }

}




