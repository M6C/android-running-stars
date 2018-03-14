package com.runningstars.activity;

import android.app.Activity;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.runningstars.R;

public class GPSLocationActivitySimple extends Activity implements LocationListener{
	private TextView latituteField;
    private LocationManager myManager;


    /********************************************************************** 
     * Activity overrides below 
     **********************************************************************/
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gpslocation);

        // get a handle to our TextView so we can write to it later
        latituteField = (TextView) findViewById(R.id.TextAddress);

        // set up the LocationManager
        myManager = (LocationManager) getSystemService(LOCATION_SERVICE);   
    }

    @Override
    protected void onDestroy() {
        stopListening();
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        stopListening();
        super.onPause();
    }

    @Override
    protected void onResume() {
        startListening();
        super.onResume();
    }



    /**********************************************************************
     * helpers for starting/stopping monitoring of GPS changes below 
     **********************************************************************/
    private void startListening() {
    	logMe("startListening");
        myManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
    }

    private void stopListening() {
    	logMe("stopListening");
        if (myManager != null)
            myManager.removeUpdates(this);
    }




    /**********************************************************************
     * LocationListener overrides below 
     **********************************************************************/
    public void onLocationChanged(Location location) {
    	logMe("onLocationChanged");
        // we got new location info. lets display it in the textview
        int lat = (int) (location.getLatitude());
        int lng = (int) (location.getLongitude());
        latituteField.setText(String.valueOf(lat) + ":" + String.valueOf(lng));
    }    

    public void onProviderDisabled(String provider)  {
    	logMe("onProviderDisabled");
    }    

    public void onProviderEnabled(String provider)  {
    	logMe("onProviderEnabled");
    }    

    public void onStatusChanged(String provider, int status, Bundle extras) {
    	logMe("onStatusChanged");
    }
    
    private void logMe(String msg) {
  	  Log.i(this.getClass().getCanonicalName(), msg);
    }
}