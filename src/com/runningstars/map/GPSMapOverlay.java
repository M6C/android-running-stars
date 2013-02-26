package com.runningstars.map;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import android.graphics.drawable.Drawable;
import android.view.MotionEvent;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;
import com.runningstars.factory.FactoryDialog;

//http://www.tutos-android.com/ajouter-marqueurs-item-poi-google-map-android

public class GPSMapOverlay extends ItemizedOverlay<OverlayItem> {

	private ArrayList<OverlayItem> listOverlayItem = new ArrayList<OverlayItem>();
	private MapView mapView;

	public GPSMapOverlay(MapView mapView, Drawable defaultMarker) {
		super(boundCenterBottom(defaultMarker));
		this.mapView = mapView;
	}

	@Override
	protected OverlayItem createItem(int i) {
		return listOverlayItem.get(i);
	}

	@Override
	public int size() {
		return listOverlayItem.size();
	}

	public void clearOverlayItem() {
		listOverlayItem.clear();
	}

	public void addOverlayItem(OverlayItem overlay) {
		listOverlayItem.add(overlay);
		populate();
	}

	public ArrayList<OverlayItem> getListOverlayItem() {
		return listOverlayItem;
	}

	/* (non-Javadoc)
	 * @see com.google.android.maps.ItemizedOverlay#onTap(com.google.android.maps.GeoPoint, com.google.android.maps.MapView)
	 */
	@Override
	public boolean onTap(GeoPoint arg0, MapView arg1) {
		return super.onTap(arg0, arg1);
	}

	/* (non-Javadoc)
	 * @see com.google.android.maps.ItemizedOverlay#onTap(int)
	 */
	@Override
	protected boolean onTap(int arg0) {
		OverlayItem item = listOverlayItem.get(arg0);
		FactoryDialog.getInstance().buildOkDialog(mapView.getContext(), item.getTitle(), item.getSnippet());
		return true;
	}

	private long events_timeout = 500L;
	private Timer zoom_event_delay_timer = new Timer();
	private GPSMapOverlay.OnZoomChangeListener zoom_change_listener;
    int lastZoomLevel = -1;

    @Override
    public boolean onTouchEvent(MotionEvent event, MapView mapview) {
        if (event.getAction() == 1) {
            if (lastZoomLevel == -1)
                lastZoomLevel = mapView.getZoomLevel();

            if (zoom_change_listener != null &&
            	mapView.getZoomLevel() != lastZoomLevel) {
//                onZoom(mapView.getZoomLevel());
//                lastZoomLevel = mapView.getZoomLevel();
                zoom_event_delay_timer.cancel();
                zoom_event_delay_timer = new Timer();
                zoom_event_delay_timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        zoom_change_listener.onZoomChange(mapView, GPSMapOverlay.this, mapView.getZoomLevel(), lastZoomLevel);
                        lastZoomLevel =  mapView.getZoomLevel();
                    }
                }, events_timeout);
            }
        }
        return false;
    }

//	public void onZoom(int level) {
//	}

	public void setOnZoomChangeListener(GPSMapOverlay.OnZoomChangeListener l) {
	    zoom_change_listener = l;
	}

	public interface OnZoomChangeListener {
	    public void onZoomChange(MapView view, GPSMapOverlay overlay, int newZoom, int oldZoom);
	}
}