package com.thinkdevs.findfriend

import android.location.Location
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.mapbox.services.android.telemetry.location.LocationEngineListener
import com.mapbox.services.android.telemetry.permissions.PermissionsListener
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() , LocationEngineListener, PermissionsListener {

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)
		
		mapView.onCreate(savedInstanceState)
		mapView.getMapAsync { }
		
		
	}
	
	
	override fun onExplanationNeeded(permissionsToExplain: MutableList<String>?) {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}
	
	override fun onPermissionResult(granted: Boolean) {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}
	
	override fun onLocationChanged(location: Location?) {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}
	
	override fun onConnected() {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}
	
	
	override fun onStart() {
		super.onStart()
		mapView.onStart()
	}
}
