package com.soundbliss

import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.SearchView
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var requestlatLng : LatLng
    private lateinit var close : ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        this.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        close = findViewById(R.id.mapClose)
        close.setOnClickListener {
            finish()
        }


        val mapFragment: SupportMapFragment = supportFragmentManager.findFragmentById(R.id.activityMap) as SupportMapFragment
        mapFragment.getMapAsync(this)

        val bundle = intent.getBundleExtra("latlng")
        requestlatLng = bundle!!.getParcelable<LatLng>("latlng") as LatLng

    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        mMap.clear()
        mMap.addMarker(MarkerOptions().position(requestlatLng).title("Request"))
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(requestlatLng, 10F))

        Log.e("MAPACTIVITY", requestlatLng.toString())
    }
}