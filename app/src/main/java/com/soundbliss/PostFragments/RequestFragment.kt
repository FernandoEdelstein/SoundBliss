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
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.soundbliss.MainActivity

import com.soundbliss.R
import kotlinx.android.synthetic.main.fragment_request.*
import kotlinx.android.synthetic.main.fragment_track.*
import java.io.IOException


class RequestFragment : Fragment(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var search : SearchView

    //Fragment Components
    private lateinit var requestTitle: EditText
    private lateinit var requestGender: EditText
    private lateinit var requestDescription: EditText

     override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

         var view = inflater.inflate(R.layout.fragment_request, container, false)

         search = view.findViewById(R.id.requestSearch)

        val mapFragment = childFragmentManager.findFragmentById(R.id.requestMap) as SupportMapFragment
        mapFragment.getMapAsync(this)


         search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query:String) : Boolean{
                var location : String = search.query.toString()
                var addressList : List<Address>? = null

                if(location != null || location != ""){
                    var geocoder = Geocoder(context)
                    try{
                        addressList = geocoder.getFromLocationName(location,1)
                    }catch (e:IOException){
                        e.printStackTrace()
                    }
                    var address = addressList?.get(0)
                    var latLng = LatLng(address!!.latitude!!,address!!.longitude)

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

        return view
    }

    override fun onMapReady(googleMap: GoogleMap){
        mMap = googleMap

        val milan = LatLng(45.4642,9.1900)
        mMap.addMarker(MarkerOptions().position(milan).title("Milan"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(milan))
    }


    fun uploadRequest(){

        //Back to Home Fragment
        startActivity(Intent(activity, MainActivity::class.java))
        activity?.finish()
    }




}