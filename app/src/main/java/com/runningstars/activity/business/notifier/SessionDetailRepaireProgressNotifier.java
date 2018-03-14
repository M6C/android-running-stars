package com.runningstars.activity.business.notifier;

import com.runningstars.activity.business.SessionDetailBusiness;
import com.runningstars.service.session.receiver.inotifier.INotifierSessionProgress;

public class SessionDetailRepaireProgressNotifier implements INotifierSessionProgress {

	private SessionDetailBusiness business;

	public SessionDetailRepaireProgressNotifier(SessionDetailBusiness business) {
		this.business = business;
	}

	public void notifySendSession(int messageId) {
	}

	public void notifySendLocation(int messageId, int count, int total) {
	}

	public void notifySendEnd(int messageId) {
		
        /**
        * reinit data and update ihm
        */
		business.reinitializDataAndUpdateIhm();

        /**
        * unregister receiver
        */
		business.unregisterRepaireReceiverProgress();
	}
}
