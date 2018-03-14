package com.runningstars.service.session.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.runningstars.service.session.receiver.inotifier.INotifierSessionProgress;

public class SessionProgressReceiver extends BroadcastReceiver {

	public static final String BROADCAST_ACTION = "BROADCAST_PROGRESS"; 
	public static final String INTENT_KEY_PROGRESS_TYPE = "INTENT_KEY_PROGRESS"; 
	public static final int INTENT_KEY_PROGRESS_TYPE_SESSION = 1; 
	public static final int INTENT_KEY_PROGRESS_TYPE_LOCATION = 2; 
	public static final int INTENT_KEY_PROGRESS_TYPE_END = 10; 
	public static final String INTENT_KEY_MESSAGE = "INTENT_KEY_MESSAGE"; 
	public static final String INTENT_KEY_COUNT = "INTENT_KEY_COUNT"; 
	public static final String INTENT_KEY_TOTAL = "INTENT_KEY_TOTAL"; 

	private INotifierSessionProgress iSessionProgress;

	/**
	 * @param iSessionProgress
	 */
	public SessionProgressReceiver(INotifierSessionProgress iSessionProgress) {
		super();
		this.iSessionProgress = iSessionProgress;
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		int messageId = intent.getIntExtra(INTENT_KEY_MESSAGE, -1);
		int count = intent.getIntExtra(INTENT_KEY_COUNT, -1);
		int total = intent.getIntExtra(INTENT_KEY_TOTAL, -1);
		int type = intent.getIntExtra(INTENT_KEY_PROGRESS_TYPE, -1);
		switch(type) {
			case INTENT_KEY_PROGRESS_TYPE_SESSION:
				iSessionProgress.notifySendSession(messageId);
				break;
			case INTENT_KEY_PROGRESS_TYPE_LOCATION:
				iSessionProgress.notifySendLocation(messageId, count, total);
				break;
			case INTENT_KEY_PROGRESS_TYPE_END:
				iSessionProgress.notifySendEnd(messageId);
				break;
		}
	}

}
