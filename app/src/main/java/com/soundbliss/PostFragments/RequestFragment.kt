package com.soundbliss.PostFragments

import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.FragmentManager
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint
import com.soundbliss.MainActivity
import com.soundbliss.Model.AllPost

import com.soundbliss.R
import kotlinx.android.synthetic.main.fragment_request.*
import kotlinx.android.synthetic.main.fragment_track.*
import kotlinx.android.synthetic.main.item_post_request.*
import java.io.IOException
import java.util.*


class RequestFragment(userid:String, username:String, userpic:String) : Fragment(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var search : SearchView

    //Fragment Components
    private lateinit var requestTitle: EditText
    private lateinit var requestGender: EditText
    private lateinit var requestDescription: EditText

    private lateinit var latLng : LatLng
    private lateinit var firestoreDb: FirebaseFirestore

    private lateinit var submitButton: Button
    private lateinit var geocoder: Geocoder
    private var addressList: MutableList<Address>? = null

    //Parsed Arguments
    private var userid = userid
    private var username = username
    private var userpic = userpic

     override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
         var view = inflater.inflate(R.layout.fragment_request, container, false)

         firestoreDb = FirebaseFirestore.getInstance()

         requestTitle = view.findViewById(R.id.requestTitle)
         requestDescription = view.findViewById(R.id.requestDescription)
         requestGender = view.findViewById(R.id.requestGender)
         submitButton = view.findViewById(R.id.requestSubmit)
         latLng = LatLng(45.4642,9.1900)


         search = view.findViewById(R.id.requestSearch)

        val mapFragment = childFragmentManager.findFragmentById(R.id.requestMap) as SupportMapFragment
        mapFragment.getMapAsync(this)


         search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query:String) : Boolean{
                var location : String = search.query.toString()
                addressList = null

                if(location != null || location != ""){
                    geocoder = Geocoder(context)
                    try{
                        addressList = geocoder.getFromLocationName(location,1)
                    }catch (e:IOException){
                        e.printStackTrace()
                    }
                    var address = addressList?.get(0)
                    latLng = LatLng(address!!.latitude!!,address!!.longitude)

                    mMap.clear()
                    mMap.addMarker(MarkerOptions().position(latLng).title(location))
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10F))
                }
                return false
            }

             override fun onQueryTextChange(newText: String?): Boolean {

                 return false
             }
         })

         submitButton.setOnClickListener {
             uploadRequest()
         }

        return view
    }

    override fun onMapReady(googleMap: GoogleMap){
        mMap = googleMap

        val milan = LatLng(45.4642,9.1900)
        mMap.addMarker(MarkerOptions().position(milan).title("Milan"))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(milan,10F))
    }


    fun uploadRequest(){
        if(requestTitle.text.isBlank() || requestTitle == null){
            Toast.makeText(context,R.string.FillTitle,Toast.LENGTH_SHORT).show()
            return
        }

        if(requestGender.text.isBlank() || requestGender == null){
            Toast.makeText(context,R.string.FillGender,Toast.LENGTH_SHORT).show()
        return}

        if(requestDescription.text.isBlank() || requestDescription == null) {
            Toast.makeText(context, R.string.FillDescription, Toast.LENGTH_SHORT).show()
            return
        }

        if(latLng == null){
            Toast.makeText(context,R.string.Error,Toast.LENGTH_SHORT).show()
            return
        }



        val geoLocation = GeoPoint(latLng.latitude,latLng.longitude)

        val locationText = ""
        if(addressList == null){
            Toast.makeText(context,R.string.FillLocation,Toast.LENGTH_SHORT).show()
        }else{
            val locationText = addressList!!.get(0).locality
        }

        if(locationText == ""){
            Toast.makeText(context,R.string.FillLocation,Toast.LENGTH_SHORT).show()
            return
        }

        val docref = firestoreDb.collection("posts/").document()

        val requestPost = AllPost(
            System.currentTimeMillis(),
            requestDescription.text.toString(),
            requestGender.text.toString(),
            geoLocation,
            locationText,
            requestTitle.text.toString(),
            userid,
            username
        )
        requestPost.setDocumentId(docref.id)
        requestPost.setPosterPic(userpic)

        docref.set(requestPost)

        //Back to Home Fragment
        val profileIntent = Intent(activity,MainActivity::class.java)
        startActivity(profileIntent)
        activity?.finish()
    }

}