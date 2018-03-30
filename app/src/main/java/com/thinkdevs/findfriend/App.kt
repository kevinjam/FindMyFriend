package com.thinkdevs.findfriend

import android.app.Application
import com.mapbox.mapboxsdk.Mapbox

/**
 * Created by kevinjanvier on 31/03/2018.
 */
class App:Application(){
	override fun onCreate() {
		super.onCreate()
		//mapbox
		Mapbox.getInstance(applicationContext, "pk.eyJ1Ijoia2V2aW5qYW4iLCJhIjoiY2l5emxsdjh5MDRqdjMycDhwY3oyZWZwZyJ9.JLzUBcJAuTHgwMkWpPwWFQ")
	}
}