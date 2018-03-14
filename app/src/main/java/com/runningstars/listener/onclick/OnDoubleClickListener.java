package com.runningstars.listener.onclick;

import android.view.View;
import android.view.View.OnClickListener;

public abstract class OnDoubleClickListener implements OnClickListener {

	private static final int DOUBLE_CLICK_TIME = 250;

	private long lastTouchTime = -1;

	public OnDoubleClickListener() {
	}

	public void onClick(View v) {
		long thisTime = System.currentTimeMillis();
		if (thisTime - lastTouchTime < DOUBLE_CLICK_TIME) {
			// Double click detected
			onDoubleClick(v);
			lastTouchTime = -1;

		} else {
			// too slow
			lastTouchTime = thisTime;
		}
	}

	/**
	 * Double click call back methode
	 * @param v view
	 */
	protected abstract void onDoubleClick(View v);
}