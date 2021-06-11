package com.veen.homechefrider.fragment

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.GroundOverlayOptions
import com.google.android.gms.maps.model.LatLng
import com.veen.homechefrider.R
import com.veen.homechefrider.activity.MainActivity
import com.veen.homechefrider.api.RetrofitInstance
import com.veen.homechefrider.databinding.FragmentHomeBinding
import com.veen.homechefrider.model.location.LocationReq
import com.veen.homechefrider.model.location.LocationRes
import com.veen.homechefrider.utils.AppUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class HomeFragment : Fragment(), OnMapReadyCallback {
    private lateinit var mMap: GoogleMap
    private var currentLocationLatitute: Double = 0.0
    private var currentLocationLongitute: Double = 0.0
    private val REQUEST_LOCATION_PERMISSION = 1
    lateinit var mFusedLocationClient: FusedLocationProviderClient
    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
//        val view = inflater.inflate(R.layout.fragment_home, container, false)
        val binding = DataBindingUtil.inflate<FragmentHomeBinding>(inflater, R.layout.fragment_home, container, false)
        val mapFragment = childFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())

        return binding.root
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        val zoomLevel = 15f
        val overlaySize = 150f
        var locationRequest = LocationRequest()
        locationRequest.interval = 10000
        locationRequest.fastestInterval = 5000
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY

        //now getting address from latitude and longitude

        val geocoder = Geocoder(requireContext(), Locale.getDefault())
        var addresses:List<Address>

        if (isPermissionGranted()) {
            if (ActivityCompat.checkSelfPermission(requireContext(),
                            Manifest.permission.ACCESS_FINE_LOCATION) !=
                    PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(requireContext(),
                            Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return
            }
            mMap.isMyLocationEnabled = true
        }
        else {
            ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf<String>(Manifest.permission.ACCESS_FINE_LOCATION),
                    REQUEST_LOCATION_PERMISSION
            )
            onMapReady(mMap)
        }
        LocationServices.getFusedLocationProviderClient(requireContext())
                .requestLocationUpdates(locationRequest, object : LocationCallback() {
                    override fun onLocationResult(locationResult: LocationResult?) {
                        super.onLocationResult(locationResult)
                        LocationServices.getFusedLocationProviderClient(requireContext())
                                .removeLocationUpdates(this)
                        if (locationResult != null && locationResult.locations.size > 0) {
                            var locIndex = locationResult.locations.size - 1

                            var latitude = locationResult.locations.get(locIndex).latitude
                            var longitude = locationResult.locations.get(locIndex).longitude

//                            addresses = geocoder.getFromLocation(latitude, longitude, 1)

                            currentLocationLatitute = latitude
                            currentLocationLongitute = longitude

                            // Add a marker in Sydney and move the camera
                            val location = LatLng(currentLocationLatitute, currentLocationLongitute)

                            /*mMap.addMarker(MarkerOptions()
                                    .position(location)
                                    .title("My Location"))*/

                            val androidOverlay = GroundOverlayOptions()
                                    .image(BitmapDescriptorFactory.fromResource(R.drawable.back))
                                    .position(location, overlaySize)
                            mMap.moveCamera(CameraUpdateFactory.newLatLng(location))
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, zoomLevel))
                            mMap.addGroundOverlay(androidOverlay)


                            locationUpdate()

                            /*mMap.setOnPoiClickListener { poi ->
                                val poiMarker = mMap.addMarker(
                                        MarkerOptions()
                                                .position(poi.latLng)
                                                .title(poi.name)
                                )
                                poiMarker.showInfoWindow()
                            }*/

//                                var address:String = addresses[0].getAddressLine(0)
//                                clocation!!.text = address
//                                if (clocation != null){
//                                    loader.visibility = View.GONE
//                                    checkout_done.visibility = View.VISIBLE
//                                    checkoutlayout.visibility = View.VISIBLE
//                                }
                        }
                    }
                }, Looper.getMainLooper())
    }

    private fun locationUpdate() {
        try {
            val getsaveToken = AppUtils.getsaveToken(requireContext())
            val getsaveLogin = AppUtils.getsaveLogin(requireContext())

            RetrofitInstance.instence?.location(getsaveToken, LocationReq(
                    currentLocationLatitute.toString(),
                    currentLocationLongitute.toString(),
                    getsaveLogin.toInt()
            ))!!.enqueue(object : Callback<LocationRes> {
                override fun onResponse(call: Call<LocationRes>, response: Response<LocationRes>) {
                    if (response.isSuccessful) {
                        currentLocationFetch()
                    }
                }

                override fun onFailure(call: Call<LocationRes>, t: Throwable) {
                    Log.d("TAG", "onFailure: Failed")
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun currentLocationFetch() {
        Handler().postDelayed(
                {
                    locationUpdate()
                }, 10000L
        )
    }

    private fun isPermissionGranted() : Boolean {
        return ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

    override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<out String>,
            grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_LOCATION_PERMISSION && grantResults.size > 0){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                onMapReady(mMap)
            }else{
                Toast.makeText(requireContext(),"Permission Denied!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
