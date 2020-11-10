package com.runningstars

import android.Manifest
import android.content.*
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.os.IBinder
import android.provider.Settings
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.snackbar.Snackbar
import com.runningstars.extention.toText
import com.runningstars.service.ForegroundOnlyLocationService
import com.runningstars.tool.DateTimeTool
import com.runningstars.tool.SharedPreferenceUtil
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.logging.Level
import java.util.logging.Logger

class MapsActivity : BaseActivity(), OnMapReadyCallback, SharedPreferences.OnSharedPreferenceChangeListener {

    companion object {
        private var TAG = MapsActivity::class.java.toString()
        private const val REQUEST_FOREGROUND_ONLY_PERMISSIONS_REQUEST_CODE = 34
    }

    private var sdf: DateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")
    private lateinit var distanceTextView: TextView
    private var foregroundOnlyLocationService: ForegroundOnlyLocationService? = null
    private lateinit var foregroundOnlyBroadcastReceiver: ForegroundOnlyBroadcastReceiver
    private lateinit var foregroundOnlyLocationButton: Button
    private lateinit var sharedPreferences: SharedPreferences

    private lateinit var mMap: GoogleMap
    private var mapLocation by instanceState(LatLng(-34.0, 151.0))
    // FusedLocationProviderClient - Main class for receiving location updates.
    private var fusedLocationClient: FusedLocationProviderClient? = null

    private var marker: Marker? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        sharedPreferences = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE)

        foregroundOnlyBroadcastReceiver = ForegroundOnlyBroadcastReceiver()
        distanceTextView = findViewById(R.id.distance_textview)
        initializeLocationButton()
        initializeLocationProvider()
    }

    override fun onStart() {
        super.onStart()

        updateButtonState(sharedPreferences.getBoolean(SharedPreferenceUtil.KEY_FOREGROUND_ENABLED, false))
        sharedPreferences.registerOnSharedPreferenceChangeListener(this)

        val serviceIntent = Intent(this, ForegroundOnlyLocationService::class.java)
        bindService(serviceIntent, foregroundOnlyServiceConnection, Context.BIND_AUTO_CREATE)
    }


    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, key: String) {
        // Updates button states if new while in use location is added to SharedPreferences.
        if (key == SharedPreferenceUtil.KEY_FOREGROUND_ENABLED) {
            updateButtonState(sharedPreferences.getBoolean(SharedPreferenceUtil.KEY_FOREGROUND_ENABLED, false))
        }
    }

    override fun onResume() {
        super.onResume()
        LocalBroadcastManager.getInstance(this).registerReceiver(
                foregroundOnlyBroadcastReceiver,
                IntentFilter(ForegroundOnlyLocationService.ACTION_FOREGROUND_ONLY_LOCATION_BROADCAST)
        )
    }

    override fun onStop() {
        foregroundOnlyLocationService?.let { unbindService(foregroundOnlyServiceConnection) }
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)

        super.onStop()
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        updateUI()
    }

    private fun initializeLocationButton() {
        foregroundOnlyLocationButton = findViewById(R.id.foreground_only_location_button)

        foregroundOnlyLocationButton.setOnClickListener {
            val enabled = sharedPreferences.getBoolean(SharedPreferenceUtil.KEY_FOREGROUND_ENABLED, false)
            if (enabled) {
                foregroundOnlyLocationService?.unsubscribeToLocationUpdates()
            } else {
                foregroundOnlyLocationService?.cleanBeforeStart()
                subscribeToLocationService()
            }
        }
    }

    private fun subscribeToLocationService() {
        if (foregroundPermissionApproved()) {
            foregroundOnlyLocationService?.subscribeToLocationUpdates() ?: Log.d(TAG, "Service Not Bound")
        } else {
            requestForegroundPermissions()
        }
    }

    private fun updateUI() = updateUI(mapLocation, zoom = 1.0F)

    private fun updateUI(location: Location) {
        val distance = foregroundOnlyLocationService?.distance ?: 0.0F
        val now = LocalDateTime.now()
        distanceTextView.text =
                foregroundOnlyLocationService?.start?.let { String.format("%.0f m in %s", distance, DateTimeTool.counterTime(it, now)) }
                ?: String.format("%.0f m", distance)
        updateUI(LatLng(location.latitude, location.longitude))
    }

    private fun updateUI(latLng: LatLng, zoom: Float = 15.0F) {
        mapLocation = latLng
        marker?.remove()
        marker = mMap.addMarker(MarkerOptions().position(latLng).title("I'm Here !"))
        Logger.getGlobal().log(Level.SEVERE, "Adding Marker $latLng")
         mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng))
        mMap.animateCamera(CameraUpdateFactory.zoomTo(zoom))
    }


    private fun updateButtonState(trackingLocation: Boolean) {
        if (trackingLocation) {
            foregroundOnlyLocationButton.text = getString(R.string.stop_location_updates_button_text)
        } else {
            foregroundOnlyLocationButton.text = getString(R.string.start_location_updates_button_text)
        }
    }

    private fun foregroundPermissionApproved(): Boolean {
        return PackageManager.PERMISSION_GRANTED == ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
    }

    // Monitors connection to the while-in-use service.
    private val foregroundOnlyServiceConnection = object : ServiceConnection {

        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            val binder = service as ForegroundOnlyLocationService.LocalBinder
            foregroundOnlyLocationService = binder.service

            if (sharedPreferences.getBoolean(SharedPreferenceUtil.KEY_FOREGROUND_ENABLED, false)) {
                subscribeToLocationService()
            }
        }

        override fun onServiceDisconnected(name: ComponentName) {
            foregroundOnlyLocationService = null
        }
    }

    private fun requestForegroundPermissions() {
        val provideRationale = foregroundPermissionApproved()

        // If the user denied a previous request, but didn't check "Don't ask again", provide
        // additional rationale.
        if (provideRationale) {
            Snackbar.make(findViewById(R.id.activity_main), R.string.permission_rationale, Snackbar.LENGTH_LONG)
                .setAction(R.string.ok) {
                    // Request permission
                    ActivityCompat.requestPermissions(this@MapsActivity, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_FOREGROUND_ONLY_PERMISSIONS_REQUEST_CODE)
                }
                .show()
        } else {
            Log.d(TAG, "Request foreground only permission")
            ActivityCompat.requestPermissions(this@MapsActivity, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_FOREGROUND_ONLY_PERMISSIONS_REQUEST_CODE)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        Log.d(TAG, "onRequestPermissionResult")

        when (requestCode) {
            REQUEST_FOREGROUND_ONLY_PERMISSIONS_REQUEST_CODE -> when {
                grantResults.isEmpty() ->
                    // If user interaction was interrupted, the permission request
                    // is cancelled and you receive empty arrays.
                    Log.d(TAG, "User interaction was cancelled.")

                grantResults[0] == PackageManager.PERMISSION_GRANTED ->
                    // Permission was granted.
                    foregroundOnlyLocationService?.subscribeToLocationUpdates()

                else -> {
                    // Permission denied.
                    updateButtonState(false)

                    Snackbar.make(findViewById(R.id.activity_main), R.string.permission_denied_explanation, Snackbar.LENGTH_LONG)
                            .setAction(R.string.settings) {
                                // Build intent that displays the App settings screen.
                                val intent = Intent()
                                intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                                val uri = Uri.fromParts("package", BuildConfig.APPLICATION_ID, null)
                                intent.data = uri
                                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                                startActivity(intent)
                            }
                            .show()
                }
            }
        }
    }

    private fun initializeLocationProvider() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
    }

    /**
     * Receiver for location broadcasts from [ForegroundOnlyLocationService].
     */
    private inner class ForegroundOnlyBroadcastReceiver : BroadcastReceiver() {

        override fun onReceive(context: Context, intent: Intent) {
            val location = intent.getParcelableExtra<Location>(
                    ForegroundOnlyLocationService.EXTRA_LOCATION
            )

            if (location != null) {
                updateUI(location)
                Logger.getGlobal().log(Level.INFO, "Foreground location: ${location.toText()}")
            }
        }
    }
}