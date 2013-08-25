package com.runningstars.activity;

import java.util.List;

import org.gdocument.gtracergps.launcher.domain.Localisation;
import org.gdocument.gtracergps.launcher.domain.Session;
import org.gdocument.gtracergps.launcher.log.Logger;

import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.cameleon.common.inotifier.INotifierMessage;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;
import com.runningstars.ApplicationData;
import com.runningstars.R;
import com.runningstars.activity.business.GPSMapBusiness;
import com.runningstars.factory.FactoryStyle;
import com.runningstars.map.GPSMapOverlay;
import com.runningstars.map.GPSMapOverlay.OnZoomChangeListener;
import com.runningstars.tool.ToolCalculate;

public class GPSMapActivity extends MapActivity implements INotifierMessage {

	private static final String TAG = GPSMapActivity.class.getCanonicalName();

	private static final int SCREENSHOOT_MENU_ITEM = Menu.FIRST;

	private MapView mapView;
	private GPSMapBusiness business;
	private GPSMapOverlay startOverlay;
	private GPSMapOverlay finishOverlay;
	private GPSMapOverlay pathOverlay;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gpsmap);

		mapView = (MapView)findViewById(R.id.mapview);

		FactoryStyle.getInstance().centerTitle(this);

		startOverlay = new GPSMapOverlay(getMapView(), getResources().getDrawable(R.drawable.map_flag_start_48x48));
		finishOverlay = new GPSMapOverlay(getMapView(), getResources().getDrawable(R.drawable.map_flag_finish_48x48));
		pathOverlay = new GPSMapOverlay(getMapView(), getResources().getDrawable(R.drawable.map_star_2_24x24));

		startOverlay.setOnZoomChangeListener(new OnZoomChangeListener() {
			
			public void onZoomChange(MapView view, GPSMapOverlay overlay, int newZoom, int oldZoom) {
				logMe("onZoomChange newZoom:" + newZoom + " oldZoom:" + oldZoom);
			}
		});

		business = new GPSMapBusiness(this);
		business.onCreate(savedInstanceState);
	}

	@Override
	protected void onResume() {
		/**
		 * Clear Location
		 */
		clearOverlay();

		business.onResume();
		super.onResume();
	}

	/* (non-Javadoc)
	 * @see com.google.android.maps.MapActivity#onPause()
	 */
	@Override
	protected void onPause() {
		business.onPause();
		super.onPause();
	}

	/* (non-Javadoc)
	 * @see com.google.android.maps.MapActivity#onDestroy()
	 */
	@Override
	protected void onDestroy() {
		super.onDestroy();
		//TODO A Refaire : Retour à l'écran de détail de session
		business.onDestroy();
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onBackPressed()
	 */
	@Override
	public void onBackPressed() {
		finish();
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, SCREENSHOOT_MENU_ITEM, 0, "Screenshoot");
		return super.onCreateOptionsMenu(menu);
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onOptionsItemSelected(android.view.MenuItem)
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case SCREENSHOOT_MENU_ITEM: {
				business.updateScreenShoot();
				break;
			}
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}

	/**
	 * INotifierMessage methodes implementation
	 */

	public void notifyError(Exception ex) {
		Toast.makeText(this, ex.toString(), Toast.LENGTH_LONG).show();
	}

	public void notifyMessage(String msg) {
		Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
	}

	public void notifyLocation(Localisation localisation, Localisation previous) {
		addMapPath(localisation, previous);
		updateOverLay();
	}

	public void notifyLocation(Session session, List<Localisation> listLocalisation) {

		/**
		 * Add Location
		 */
		Localisation previous = session.getStart();
		int size = pathOverlay.getListOverlayItem().size();//0;
		if (listLocalisation!=null && size==0) {
//			/**
//			 * Clear Location
//			 */
//			clearOverlay();

			size = listLocalisation.size();

			if (size>0) {
				for(Localisation localisation : listLocalisation) {
					addMapPath(localisation, previous);
				}
			}
		}

		if (session.getStart()!=null)
			addMapStart(session.getStart(), null);

		if (session.getEnd()!=null)
			addMapFinish(session.getEnd(), previous);

		/**
		 * Move To Location
		 */
		if (session.getStart()!=null)
			moveToMapLocalisation(session.getStart());
		else if (session.getEnd()!=null)
			moveToMapLocalisation(session.getEnd());
		else if (listLocalisation!=null && size>0)
			moveToMapLocalisation(listLocalisation.get(size-1));
		
		showAllLocalisation();
	}

	private void addMapStart(Localisation localisation, Localisation previous) {
		addMapLocalisation(localisation, previous, startOverlay);
	}

	private void addMapFinish(Localisation localisation, Localisation previous) {
		addMapLocalisation(localisation, previous, finishOverlay);
	}

	private void addMapPath(Localisation localisation, Localisation previous) {
		addMapLocalisation(localisation, previous, pathOverlay);
	}

	private void addMapLocalisation(Localisation localisation, Localisation previous, GPSMapOverlay overlay) {
		String txt1 = "Locality:" + localisation.getSimpleAddress();
		String txt2 = 
				"Distance:" + ToolCalculate.formatCalculateDistanceCumulKm(localisation) +
				"\nSpeed:" + ToolCalculate.formatSpeedKmH(localisation);

		if (localisation.getCalculateSpeedAverageKmHRounded()>0)
			txt2 += "\nSpeed Average (c):" + ToolCalculate.formatCalculateSpeedAverageKmH(localisation);
		else if (previous!=null)
    		txt2 += "\nSpeed Average:" + ToolCalculate.formatKmH(
    			ApplicationData.getInstance(this).getDistanceMethodeCalcul(),
    			previous, localisation
    		);

		if (localisation.getCalculateElapsedTime()>0)
			txt2 += "\nElapsed Time (c):" + ToolCalculate.formatCalculateElapsedTime(localisation);
		else if (previous!=null)
    		txt2 += "\nElapsed Time:" + ToolCalculate.formatElapsedTime(previous, localisation);

		GeoPoint geoPoint2 = new GeoPoint((int) (localisation.getLatitude() * 1000000), (int) (localisation.getLongitude() * 1000000));
		OverlayItem overlayitem2 = new OverlayItem(geoPoint2, txt1, txt2);
		overlay.addOverlayItem(overlayitem2);
		
		List<Overlay> mapOverlays = getMapView().getOverlays();
		mapOverlays.add(overlay);
	}

	private void updateOverLay() {
		final Handler handler = new Handler();
		handler.post(new Runnable() {
			public void run() {
				handler.removeCallbacks(this);

				getMapView().postInvalidate();

				handler.postDelayed(this, 250);
			}
		});
	}

	private void clearOverlay() {
		/**
		 * Clear Location
		 */
		startOverlay.clearOverlayItem();
		finishOverlay.clearOverlayItem();
		pathOverlay.clearOverlayItem();
		getMapView().getOverlays().clear();
	}

	private void moveToMapLocalisation(Localisation localisation) {
		MapController myMapController = getMapView().getController();
		myMapController.setCenter(new GeoPoint((int)(localisation.getLatitude()*1000000), (int)(localisation.getLongitude()*1000000)));
	}

	private void showAllLocalisation() {
		int minLat = Integer.MAX_VALUE;
		int maxLat = Integer.MIN_VALUE;
		int minLon = Integer.MAX_VALUE;
		int maxLon = Integer.MIN_VALUE;

		for (Overlay overlay : getMapView().getOverlays()) {
			for (OverlayItem item : ((GPSMapOverlay)overlay).getListOverlayItem()) {
				int lat = item.getPoint().getLatitudeE6();
				int lon = item.getPoint().getLongitudeE6();
				
				maxLat = Math.max(lat, maxLat);
				minLat = Math.min(lat, minLat);
				maxLon = Math.max(lon, maxLon);
				minLon = Math.min(lon, minLon);
			}
		 }

		MapController mapController = getMapView().getController();
		mapController.zoomToSpan(Math.abs(maxLat - minLat), Math.abs(maxLon - minLon));
		mapController.animateTo(new GeoPoint( (maxLat + minLat)/2, (maxLon + minLon)/2 )); 
	}

	/**
	 * @return the mapView
	 */
	public MapView getMapView() {
		return mapView;
	}

	private void logMe(String msg) {
		Logger.logMe(TAG, msg);
    }
}
