package com.example.service.template.binder;

import android.os.Binder;

import com.example.service.template.TemplateService;

public class TemplateServiceBinder<T extends TemplateService<?>> extends Binder {

	private T service;
	
	public TemplateServiceBinder(T service) {
		this.service = service;
	}

	public T getService() {
		return service;
	}
}
