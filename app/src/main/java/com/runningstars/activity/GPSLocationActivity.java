package com.runningstars.activity;

import java.text.NumberFormat;
import java.util.HashMap;
import java.util.List;

import org.gdocument.gtracergps.launcher.domain.Localisation;
import org.gdocument.gtracergps.launcher.domain.Session;
import org.gdocument.gtracergps.launcher.log.Logger;
import org.gdocument.gtracergps.launcher.util.NumberUtil;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.SlidingDrawer;
import android.widget.SlidingDrawer.OnDrawerCloseListener;
import android.widget.SlidingDrawer.OnDrawerOpenListener;
import android.widget.TableLayout;
import android.widget.TableLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.cameleon.common.factory.FactoryFont;
import com.cameleon.common.inotifier.INotifierMessage;
import com.cameleon.common.tool.ToolDatetime;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;
import com.runningstars.ApplicationData;
import com.runningstars.R;
import com.runningstars.activity.business.GPSLocationBusiness;
import com.runningstars.listener.onclick.OnClickButtonMenuListener;
import com.runningstars.listener.onclick.OnClickButtonRunListener;
import com.runningstars.listener.onclick.OnClickFieldDataListener;
import com.runningstars.listener.onitemclick.IOnItemClickFieldData;
import com.runningstars.map.GPSMapOverlay;
import com.runningstars.tool.ToolCalculate;

public class GPSLocationActivity extends MapActivity implements INotifierMessage, IOnItemClickFieldData {
	private static final String TAG = GPSLocationActivity.class.getCanonicalName();

	private static final String KEY_SAVE_STATE_ADDRESS = "SAVE_STATE_ADDRESS";
	private static final String KEY_SAVE_STATE_SPEED_AVERAGE = "SAVE_STATE_SPEED_AVERAGE";
	private static final String KEY_SAVE_STATE_SPEED = "SAVE_STATE_SPEED";
	private static final String KEY_SAVE_STATE_DISTANCE = "SAVE_STATE_DISTANCE";
	private static final String KEY_SAVE_STATE_ELAPSED_TIME = "SAVE_STATE_ELAPSED_TIME";
	private static final String KEY_SAVE_STATE_RUN = "SAVE_STATE_RUN";
	private static final String KEY_SAVE_MAP_LONGITUDE = "SAVE_MAP_LONGITUDE";
	private static final String KEY_SAVE_MAP_LATITUDE = "SAVE_MAP_LATITUDE";
	private static final String KEY_SAVE_STATE_SLIDINGBUTTON = null;

	private static final int PERMISSION_REQUEST_ACCESS_FINE_LOCATION = 1;

	private TextView addressField;
	private TextView speedAverageField;
	private TextView speedField;
	private TextView distanceField;
	private TextView elapsedTimeField;
	private MapView mapView;
	private SlidingDrawer slidingMap;
	private ImageView menuButton;
	private ImageView runButton;

	private GPSLocationBusiness business;

	private Handler handler;

	private HashMap<View, FieldData> fieldDataByView = new HashMap<View, FieldData>();

	private boolean run = false;
	private GPSMapOverlay itemizedoverlay;

	/**********************************************************************
	 * Activity overrides below
	 **********************************************************************/
	@Override
	public void onCreate(Bundle savedInstanceState) {
		logMe("onCreate START");
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);

		setContentView(R.layout.activity_gpslocation_digital);

		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title_red_nike);

		handler = new Handler();

		// get a handle to our TextView so we can write to it later
		addressField = (TextView) findViewById(R.id.TextAddress);
		speedAverageField = (TextView) findViewById(R.id.TextSpeedAverage);
		speedField = (TextView) findViewById(R.id.TextSpeed);
		distanceField = (TextView) findViewById(R.id.TextDistance);
		elapsedTimeField = (TextView) findViewById(R.id.TextElapsedTime);
		slidingMap = (SlidingDrawer) findViewById(R.id.slidingMap);
		mapView = (MapView) findViewById(R.id.mapview);
		menuButton = (ImageView)findViewById(R.id.button_menu);
		runButton = (ImageView)findViewById(R.id.button_run);

		addressField.setOnLongClickListener(new OnClickFieldDataListener(this, addressField, this));
		speedAverageField.setOnLongClickListener(new OnClickFieldDataListener(this, speedAverageField, this));
		speedField.setOnLongClickListener(new OnClickFieldDataListener(this, speedField, this));
		distanceField.setOnLongClickListener(new OnClickFieldDataListener(this, distanceField, this));
		elapsedTimeField.setOnLongClickListener(new OnClickFieldDataListener(this, elapsedTimeField, this));
//		mapView.setBuiltInZoomControls(true);

		/**
		 * Map OverLay Initialisation
		 */
		Drawable drawable = this.getResources().getDrawable(R.drawable.ic_launcher);
		itemizedoverlay = new GPSMapOverlay(mapView, drawable);

		/**
		 * Sliding Map Initialisation
		 */
		final GPSLocationActivity context = this;
		slidingMap.setOnDrawerOpenListener(new OnDrawerOpenListener() {
			
			public void onDrawerOpened() {
				speedField.setLayoutParams(new TableLayout.LayoutParams(LayoutParams.MATCH_PARENT, 0, 0.6f));
				slidingMap.setLayoutParams(new TableLayout.LayoutParams(LayoutParams.MATCH_PARENT, 0, 0.4f));
//				if (business.getPreviousLocalisation()!=null) {
					Localisation localisation = new Localisation();//business.getPreviousLocalisation();//
					localisation.setLatitude(48.8438568888);
					localisation.setLongitude(2.56948095151);
					GeoPoint point = context.locationToGeoPoint(localisation);
					context.addMapLocalisation(localisation);
					zoomOnLocation(point);
//				}
//				else {
//					// TODO Affichage de la position courante
//				}
			}
		});

		/**
		 * Sliding Map Initialisation
		 */
		slidingMap.setOnDrawerCloseListener(new OnDrawerCloseListener() {
			public void onDrawerClosed() {
				speedField.setLayoutParams(new TableLayout.LayoutParams(LayoutParams.MATCH_PARENT, 0, 0.88f));
				slidingMap.setLayoutParams(new TableLayout.LayoutParams(LayoutParams.MATCH_PARENT, 0, 0.12f));
			}
		});

		/**
		 * Menu Button Initialisation
		 */
		menuButton.setOnClickListener(new OnClickButtonMenuListener(this));

		/**
		 * Run Button Initialisation
		 */
		runButton.setOnClickListener(new OnClickButtonRunListener(this));

		if (savedInstanceState != null) {
			addressField.setText(savedInstanceState.getString(KEY_SAVE_STATE_ADDRESS));
			speedAverageField.setText(savedInstanceState.getString(KEY_SAVE_STATE_SPEED_AVERAGE));
			speedField.setText(savedInstanceState.getString(KEY_SAVE_STATE_SPEED));
			distanceField.setText(savedInstanceState.getString(KEY_SAVE_STATE_DISTANCE));
			elapsedTimeField.setText(savedInstanceState.getString(KEY_SAVE_STATE_ELAPSED_TIME));
//			run = savedInstanceState.getBoolean(KEY_SAVE_STATE_RUN);

			int mapLongitude = savedInstanceState.getInt(KEY_SAVE_MAP_LONGITUDE);
			int mapLatitude = savedInstanceState.getInt(KEY_SAVE_MAP_LATITUDE);

			Localisation localisation = new Localisation();
			localisation.setLocality("1st Location");//"Tahiti");
			localisation.setSpeed(10.0f);
			localisation.setLatitude(mapLatitude);//-149487411 * 1000000);
			localisation.setLatitude(mapLongitude);//-17595983 * 1000000);
			addMapLocalisation(localisation);

//			mapView.getController().setCenter(new GeoPoint(mapLatitude, mapLongitude));

			boolean slidingButtonState = savedInstanceState.getBoolean(KEY_SAVE_STATE_SLIDINGBUTTON);
			if (slidingButtonState) {
				slidingMap.open();
			}
			else {
				slidingMap.close();
			}
		} else {
			setItemClickFieldData(addressField, FieldData.ADDRESS);
			setItemClickFieldData(speedAverageField, FieldData.SPEED_AVERAGE);
			setItemClickFieldData(speedField, FieldData.SPEED);
			setItemClickFieldData(distanceField, FieldData.DISTANCE);
			setItemClickFieldData(elapsedTimeField, FieldData.ELAPSED_TIME);
			setItemClickFieldData(mapView, FieldData.MAP);
		}

		business = new GPSLocationBusiness(this);
		business.onCreate(savedInstanceState);

		logMe("onCreate END");
	}
	@Override
	public void onResume() {
		super.onResume();

		if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
				!= PackageManager.PERMISSION_GRANTED) {

			// Should we show an explanation?
			if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {

				// Show an expanation to the user *asynchronously* -- don't block
				// this thread waiting for the user's response! After the user
				// sees the explanation, try again to request the permission.

			} else {

				// No explanation needed, we can request the permission.
				// PERMISSION_REQUEST_ACCESS_FINE_LOCATION can be any unique int
				ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_ACCESS_FINE_LOCATION);
			}
		}
	}

	@Override
	public void onRequestPermissionsResult(int requestCode,
										   String permissions[], int[] grantResults) {
		switch (requestCode) {
			case PERMISSION_REQUEST_ACCESS_FINE_LOCATION: {
				// If request is cancelled, the result arrays are empty.
				if (grantResults.length > 0
						&& grantResults[0] == PackageManager.PERMISSION_GRANTED) {

					// permission was granted, yay! Do the
					// contacts-related task you need to do.

				} else {

					// permission denied, boo! Disable the
					// functionality that depends on this permission.
				}
				return;
			}

			// other 'case' lines to check for other
			// permissions this app might request
		}
	}
	/**
	 * Initialize IHM After Service is setted in business
	 */
	public void afterServiceSetted() {
		logMe("afterServiceSetted");
		run = business.isSystemGpsServiceRunning();
		if (run)
			((ImageView)runButton).setImageResource(R.drawable.title_red_button_run);
	}

	@Override
	protected void onDestroy() {
		logMe("onDestroy START");
		business.onDestroy();
		super.onDestroy();
		logMe("onDestroy END");
	}

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}

	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {
		savedInstanceState.putString(KEY_SAVE_STATE_ADDRESS, addressField.getText().toString());
		savedInstanceState.putString(KEY_SAVE_STATE_SPEED_AVERAGE, speedAverageField.getText().toString());
		savedInstanceState.putString(KEY_SAVE_STATE_SPEED, speedField.getText().toString());
		savedInstanceState.putString(KEY_SAVE_STATE_DISTANCE, distanceField.getText().toString());
		savedInstanceState.putString(KEY_SAVE_STATE_ELAPSED_TIME, elapsedTimeField.getText().toString());
		savedInstanceState.putInt(KEY_SAVE_MAP_LONGITUDE, mapView.getMapCenter().getLongitudeE6());
		savedInstanceState.putInt(KEY_SAVE_MAP_LATITUDE, mapView.getMapCenter().getLatitudeE6());
		savedInstanceState.putString(KEY_SAVE_STATE_ELAPSED_TIME, elapsedTimeField.getText().toString());
		savedInstanceState.putBoolean(KEY_SAVE_STATE_RUN, isRun());
		savedInstanceState.putBoolean(KEY_SAVE_STATE_SLIDINGBUTTON, slidingMap.isOpened());
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onBackPressed()
	 */
	@Override
	public void onBackPressed() {
		finish();
	}

	public void runInHandler(Runnable runnable) {
		handler.post(runnable);
	}

	public void notifyLocation(Localisation localisation, Session session) {
    	logMe("notifyLocation");

    	if (localisation==null) {
    		logMe("location is null");
    		return;
    	}
    	logMe("notifyLocation localisation:"+localisation.toString());

		setViewData(addressField, localisation);
		setViewData(speedField, localisation);
		setViewData(mapView, localisation);
	}

	public void notifySession(Session session) {
    	logMe("notifySession");

    	if (run) {
			runButton.setImageResource(R.drawable.title_red_button_run);
		}
		else {
			runButton.setImageResource(R.drawable.title_red_button_no_run);
		}

		if (session==null) {
    		logMe("session is null");
    		return;
    	}
    	logMe("notifySession session:"+session.toString());

		setViewData(speedAverageField, session);
		setViewData(distanceField, session);
		setViewData(elapsedTimeField, session);
	}

	private void setViewStyle(View view, FieldData fieldData) {
		if (FieldData.MAP.equals(fieldData)) {
//			if (view == addressField) {
//				addressField.setVisibility(View.INVISIBLE);
//				mapView.setVisibility(View.VISIBLE);
//			} else {
//				addressField.setVisibility(View.VISIBLE);
//				mapView.setVisibility(View.INVISIBLE);
//			}
		} else {
			int hintId = 0, maxlines = 1;
			Typeface typeface = null;
			switch (fieldData) {
			case ADDRESS:
				// maxlines = 3;
				hintId = R.string.dialog_field_data_address;
				typeface = FactoryFont.getInstance().buildDigital(this);
				break;
			case SPEED:
				hintId = R.string.dialog_field_data_speed;
				typeface = FactoryFont.getInstance().buildDigital(this);
				break;
			case DISTANCE:
				hintId = R.string.dialog_field_data_distance;
				typeface = FactoryFont.getInstance().buildDigital(this);
				break;
			case ELAPSED_TIME:
				hintId = R.string.dialog_field_data_elapsed_time;
				typeface = FactoryFont.getInstance().buildDigital(this);
				break;
			case SPEED_AVERAGE:
				hintId = R.string.dialog_field_data_speed_average;
				typeface = FactoryFont.getInstance().buildDigital(this);
				break;
			default:
				break;
			}

			if (view instanceof TextView) {
				TextView tv = ((TextView) view);
				tv.setMaxLines(maxlines);
				tv.setHint(hintId);
				tv.setTypeface(typeface);
			}

			/**
			 * Setup Address & Distance Layout Weidth
			 */
			FieldData addressFieldData = fieldDataByView.get(addressField);
			float addressLayoutWeidth = 0.5f, speedAverageLayoutWeidth = 0.5f;
			if (FieldData.ADDRESS.equals(addressFieldData)) {
				addressLayoutWeidth = 0.2f;
				speedAverageLayoutWeidth = 0.8f;
			}
			addressField.setLayoutParams(new TableLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, addressLayoutWeidth));
			speedAverageField.setLayoutParams(new TableLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, speedAverageLayoutWeidth));
		}
	}

	private void setViewData(View view, Localisation localisation) {
		logMe("setViewData localisation");
		NumberFormat nf = NumberFormat.getNumberInstance();

		String data = null;
		FieldData fieldData = fieldDataByView.get(view);
		switch (fieldData) {
			case ADDRESS: {
				data = localisation.getSimpleAddress();
				break;
			}
			case SPEED: {
				float speed = (localisation.getSpeedKmHRounded()>0.0f) ? localisation.getSpeedKmHRounded() : localisation.getCalculateSpeedKmHRounded();
				data = (speed < 1f) ? nf.format((int) (speed * 1000)) + " M/H" : nf.format(speed) + " KM/H";

				if (localisation.getSpeedKmHRounded()<=0 && localisation.getCalculateSpeedKmHRounded()>0)
					data += " c";
				break;
			}
			case MAP: {
				if (slidingMap.isOpened())
					addMapLocalisation(localisation);
				break;
			}
		default:
			break;
		}

		if (view instanceof TextView) {
			logMe("setViewData localisation fieldData:"+fieldData.name()+" data:"+data);
			TextView tv = ((TextView) view);
			tv.setText(data);
		}
	}

	private void setViewData(View view, Session session) {
		logMe("setViewData session");
		NumberFormat nf = NumberFormat.getNumberInstance();

		String data = null;
		FieldData fieldData = fieldDataByView.get(view);
		if (fieldData!=null) {
			switch (fieldData) {
				case DISTANCE: {
					double distance = NumberUtil.formatDouble(session.getCalculateDistance());
					data = /*
							 * (distance < 1d) ? nf.format((int) (distance * 1000)) +
							 * " M" :
							 */nf.format(distance) + " KM";
					break;
				}
				case ELAPSED_TIME: {
					data = ToolDatetime.toTimeDefault(session.getCalculateElapsedTime());
					break;
				}
				case SPEED_AVERAGE: {
					logMe("setViewData SPEED_AVERAGE session start:"+session.getStart()+" end:"+session.getEnd()+" calculate distance:"+session.getCalculateDistance());
					data = ToolCalculate.formatSpeedKmH(session);
					break;
				}
				default:
					break;
			}
			
			if (view instanceof TextView) {
				logMe("setViewData session fieldData:"+fieldData.name()+" data:"+data);
				TextView tv = ((TextView) view);
				tv.setText(data);
			}
		}
	}

	private void addMapLocalisation(Localisation localisation) {
		NumberFormat nf = NumberFormat.getNumberInstance();

		String txt1 = localisation.getLocality();
		float speed = localisation.getSpeedKmHRounded();
		String txt2 = (speed < 1f) ? nf.format((int) (speed * 1000)) + " M/H" : nf.format(speed) + " KM/H";

		GeoPoint geoPoint = locationToGeoPoint(localisation);
		OverlayItem overlayitem = new OverlayItem(geoPoint, txt1, txt2);
		addMapLocalisation(overlayitem);
	}

	private void addMapLocalisation(OverlayItem overlayitem) {
		itemizedoverlay.clearOverlayItem();
		itemizedoverlay.addOverlayItem(overlayitem);
		
		List<Overlay> mapOverlays = mapView.getOverlays();
		mapOverlays.clear();
		mapOverlays.add(itemizedoverlay);
		
		moveToMapLocalisation(overlayitem.getPoint());
	}
	
	private void moveToMapLocalisation(GeoPoint point) {
		MapController myMapController = mapView.getController();
		myMapController.setCenter(point);
	}

	private void zoomOnLocation(GeoPoint point) {
		MapController mapController = mapView.getController();
		mapController.animateTo(point);
		mapController.setZoom(ApplicationData.getInstance(this).getGpsLocationMapZoom());
		mapView.invalidate();
	}

	private GeoPoint locationToGeoPoint(Localisation localisation) {
		return new GeoPoint((int)(localisation.getLatitude()*1000000), (int)(localisation.getLongitude()*1000000));
	}

	/**
	 * @return the run
	 */
	public boolean isRun() {
		return run;
	}

	/**
	 * @return the run
	 */
	public void setRun(boolean run) {
		this.run = run;
	}

	public Handler getHandler() {
		return handler;
	}

	/**
	 * 
	 */
	public GPSLocationBusiness getBusiness() {
		return business;
	}

	/**
	 * INotifierMessage methodes implementation
	 */

	public void setItemClickFieldData(View view, FieldData fieldData) {
		fieldDataByView.put(view, fieldData);
		setViewStyle(view, fieldData);
	}

	public boolean isFieldDataSelectable(FieldData fieldData) {
		return !fieldDataByView.containsValue(fieldData);
	}

	public FieldData getItemClickFieldData(View view) {
		return fieldDataByView.get(view);
	}

	/**
	 * INotifierMessage methodes implementation
	 */

	public void notifyError(Exception ex) {
		Toast.makeText(this, ex.toString(), Toast.LENGTH_LONG).show();
	}

	public void notifyMessage(String msg) {
		Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
	}

	/**
     * Log methodes
     * @param msg
     */
    private void logMe(String msg) {
		Logger.logMe(TAG, msg);
    }
}