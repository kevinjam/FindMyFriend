package com.thinkdevs.findfriend

import android.annotation.SuppressLint
import android.location.Location
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.services.android.telemetry.location.LocationEngineListener
import com.mapbox.services.android.telemetry.permissions.PermissionsListener
import kotlinx.android.synthetic.main.activity_main.*
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.services.android.telemetry.location.LocationEngine
import com.mapbox.services.android.telemetry.location.LocationEnginePriority
import com.mapbox.services.android.telemetry.location.LocationEngineProvider
import com.mapbox.services.android.telemetry.permissions.PermissionsManager
import java.awt.font.TextAttribute.TRACKING

import com.mapbox.mapboxsdk.plugins.locationlayer.LocationLayerPlugin
import com.thinkdevs.findfriend.R.id.mapView
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory
import android.support.annotation.NonNull
import android.widget.Toast


class MainActivity : AppCompatActivity() , LocationEngineListener, PermissionsListener {
	
	 var permissionsManager: PermissionsManager? = null
	private var locationPlugin: Location? = null
	private var locationEngine: LocationEngine? = null
	
	private var mapboxMap: MapboxMap? = null
	
	
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		// Mapbox access token is configured here. This needs to be called either in your application
		// object or in the same activity which contains the mapview.
		Mapbox.getInstance(this, getString(R.string.access_token))
		
		setContentView(R.layout.activity_main)
		
		mapView.onCreate(savedInstanceState)
		mapView.getMapAsync {
			enableLocationPlugin()
		}
		
		
	}
	
	private fun enableLocationPlugin() {
		if (PermissionsManager.areLocationPermissionsGranted(this)){
			// Create a location engine instance
			initializeLocationEngine()
			locationPlugin = LocationLayerPlugin(mapView, mapboxMap, locationEngine)
			locationPlugin.setLocationLayerEnabled(LocationLayerMode.TRACKING)
			
		}else{
			permissionsManager = PermissionsManager(this)
			permissionsManager!!.requestLocationPermissions(this)
		}
		
	}
	
	
	@SuppressLint("MissingPermission")
	private fun initializeLocationEngine() {
		locationEngine =  LocationEngineProvider(this).obtainBestLocationEngineAvailable()
		locationEngine!!.priority = LocationEnginePriority.HIGH_ACCURACY
		locationEngine!!.activate()
		
		val lastLocation:Location =locationEngine!!.lastLocation
		if (lastLocation != null){
			setCameraPosition(lastLocation)
		}else{
			locationEngine!!.addLocationEngineListener(this)
		}
	
	}
	
	private fun setCameraPosition(lastLocation: Location) {
		mapboxMap!!.animateCamera(CameraUpdateFactory.newLatLngZoom(
				LatLng(lastLocation.latitude, lastLocation.longitude), 16.0))
	}
	
	override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
		permissionsManager!!.onRequestPermissionsResult(requestCode, permissions, grantResults)
	}
	
	
	override fun onExplanationNeeded(permissionsToExplain: MutableList<String>?) {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}
	
	override fun onPermissionResult(granted: Boolean) {
		if (granted) {
			enableLocationPlugin();
		} else {
			Toast.makeText(this, "user_location_permission_not_granted", Toast.LENGTH_LONG).show()
			finish();
		}
	}
	
	@SuppressLint("MissingPermission")
	override fun onConnected() {
		locationEngine!!.requestLocationUpdates()
		
	}
	
	override fun onLocationChanged(location: Location?) {
		if (location != null) {
			setCameraPosition(location)
			locationEngine!!.removeLocationEngineListener(this)
		}
	}
	
	override fun onStart() {
		super.onStart()
		if (locationPlugin != null) {
			locationPlugin!!.onStart()
		}
		mapView.onStart()
	}
	
	override fun onResume() {
		super.onResume()
		mapView.onResume()
	}
	
	override fun onPause() {
		super.onPause()
		mapView.onPause()
	}
	
	override fun onStop() {
		super.onStop()
		if (locationEngine != null) {
			locationEngine!!.removeLocationUpdates()
		}
		if (locationPlugin != null) {
			locationPlugin!!.onStop()
		}
		mapView.onStop()
	}
	
	override fun onSaveInstanceState(outState: Bundle?) {
		super.onSaveInstanceState(outState)
		mapView.onSaveInstanceState(outState!!)
	}
	
	override fun onDestroy() {
		super.onDestroy()
		mapView.onDestroy()
		if (locationEngine != null){
			locationEngine!!.deactivate()
		}
	}
	
	override fun onLowMemory() {
		super.onLowMemory()
		mapView.onLowMemory()
	}
}
