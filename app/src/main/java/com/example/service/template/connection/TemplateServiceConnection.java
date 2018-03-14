package com.example.service.template.connection;

import org.gdocument.gtracergps.launcher.log.Logger;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;

import com.example.service.template.TemplateService;
import com.example.service.template.binder.TemplateServiceBinder;
import com.example.service.template.connection.inotifer.TemplateINotifierServiceConnection;

public class TemplateServiceConnection <T extends TemplateINotifierServiceConnection<?>> implements ServiceConnection {
	private static final String TAG = TemplateServiceConnection.class.getCanonicalName();

	private T notifierServiceConnection; 

	public TemplateServiceConnection(T notifierServiceConnection) {
		super();
		this.notifierServiceConnection = notifierServiceConnection;
	}

	public void onServiceConnected(ComponentName name, IBinder service) {
		logMe("onServiceConnected");
		TemplateService<?> srv = ((TemplateServiceBinder<?>)service).getService();
//		notifierServiceConnection.setService(srv);
	}

	public void onServiceDisconnected(ComponentName name) {
		logMe("onServiceDisconnected");
		notifierServiceConnection.setService(null);
	}

	/**
     * Log methodes
     * @param msg
     */
    private void logMe(String msg) {
		Logger.logMe(TAG, msg);
    }
}
