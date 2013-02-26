package com.example.service.template;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import com.example.service.template.binder.TemplateServiceBinder;

public abstract class TemplateService<T extends TemplateServiceBinder<?>> extends Service {

	public TemplateService() {
	}

	@Override
	public IBinder onBind(Intent intent) {
		return createServiceBinder(this);
	}

	@Override
	public boolean onUnbind(Intent intent) {
		return super.onUnbind(intent);
	}

	protected abstract T createServiceBinder(TemplateService<T> templateService);

	protected Context getContext() {
		return getApplicationContext();
	}
}
