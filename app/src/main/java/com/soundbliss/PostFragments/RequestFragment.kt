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

import com.soundbliss.R
import kotlinx.android.synthetic.main.fragment_request.*
import kotlinx.android.synthetic.main.fragment_track.*


class RequestFragment : Fragment(), OnMapReadyCallback{
    private lateinit var searchView:SearchView
    private lateinit var map: GoogleMap
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


        return view
    }

    fun uploadRequest(){

        //Back to Home Fragment
        startActivity(Intent(activity, MainActivity::class.java))
        activity?.finish()
    }

    override fun onMapReady(p0: GoogleMap?) {
        map = p0!!
    }

}