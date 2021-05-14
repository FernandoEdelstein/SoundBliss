package com.soundbliss.PostFragments

import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.SearchView
import androidx.fragment.app.FragmentManager
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.soundbliss.MainActivity
import com.soundbliss.Model.RequestPost
import com.soundbliss.Model.TrackPost
import com.soundbliss.R
import kotlinx.android.synthetic.main.fragment_request.*
import kotlinx.android.synthetic.main.fragment_track.*


class RequestFragment : Fragment(), OnMapReadyCallback{

    private lateinit var map: GoogleMap
    private lateinit var searchView : SearchView
    private lateinit var mapFragment :SupportMapFragment

    //Fragment Components
    private lateinit var requestTitle: EditText
    private lateinit var requestGender: EditText
    private lateinit var requestDescription: EditText

    private lateinit var latlng: LatLng
     override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
         var view = inflater.inflate(R.layout.fragment_request, container, false)

         //Initialization
         requestTitle = view.findViewById(R.id.requestTitle)
         requestGender = view.findViewById(R.id.requestGender)
         requestDescription = view.findViewById(R.id.requestDescription)

         searchView = view.findViewById(R.id.location)


         mapFragment = childFragmentManager.findFragmentById((R.id.map)) as SupportMapFragment
         searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
             override fun onQueryTextSubmit(query: String?): Boolean {
                 var location = searchView.query.toString()
                 var addressList : List<Address>? = null

                 if(location != null || !location.equals("")){
                     var geocoder : Geocoder = Geocoder(context)
                     addressList = geocoder.getFromLocationName(location,1)
                     var address:Address = addressList.get(0)
                     latlng = LatLng(address.latitude, address.longitude)
                     map.addMarker(MarkerOptions().position(latlng).title(location))
                 }

                 return false
             }

             override fun onQueryTextChange(newText: String?): Boolean {
                 TODO("Not yet implemented")
             }
         })
        mapFragment.getMapAsync(this)
        return view
    }

    fun uploadRequest(){
        val ref = FirebaseDatabase.getInstance().getReference("posts")
        var id = ref.push().key

        val userRef = FirebaseDatabase.getInstance().getReference("users")

        var requestPost = RequestPost(
            FirebaseAuth.getInstance().currentUser.uid,
            userRef.child(FirebaseAuth.getInstance().currentUser.uid).child("username").get().toString(),
            requestTitle.text.toString(),
            requestGender.text.toString(),
            requestDescription.text.toString(),
            latlng,
            "request",
            "${System.currentTimeMillis()}"
        )

        if (id != null) {
            ref.child("request").child(id).setValue(requestPost)
        }

        //Back to Home Fragment
        startActivity(Intent(activity, MainActivity::class.java))
        activity?.finish()
    }

    override fun onMapReady(p0: GoogleMap?) {
        map = p0!!
    }

}