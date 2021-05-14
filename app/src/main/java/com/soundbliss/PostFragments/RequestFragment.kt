package com.soundbliss.PostFragments

import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.FragmentManager
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.soundbliss.R


class RequestFragment : Fragment(), OnMapReadyCallback{

    private lateinit var map: GoogleMap
    private lateinit var searchView : SearchView
    private lateinit var mapFragment :SupportMapFragment

     override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
         var view = inflater.inflate(R.layout.fragment_request, container, false)

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
                     var latlng : LatLng = LatLng(address.latitude, address.longitude)
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

    }

    override fun onMapReady(p0: GoogleMap?) {
        map = p0!!
    }

}