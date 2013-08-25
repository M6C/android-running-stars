package com.runningstars.factory;

import android.content.Context;

import com.cameleon.common.inotifier.INotifierMessage;
import com.runningstars.factory.singleton.SingletonNotifierSendSessionProgress;
import com.runningstars.factory.singleton.SingletonNotifierToast;
import com.runningstars.service.session.receiver.inotifier.INotifierSessionProgress;


public class FactoryNotifier {

	private static FactoryNotifier instance;

	private FactoryNotifier() {
	}

	public static FactoryNotifier getInstance() {
		if (instance==null)
			instance = new FactoryNotifier();
		return instance;
	}

	public INotifierMessage getNotifierToast(Context context) {
		return SingletonNotifierToast.getInstance(context);
	}

	public INotifierSessionProgress getNotifierSendSessionProgress(Context context) {
		return SingletonNotifierSendSessionProgress.getInstance(context);
	}
}
