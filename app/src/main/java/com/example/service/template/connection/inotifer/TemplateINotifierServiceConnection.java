package com.example.service.template.connection.inotifer;

import com.example.service.template.TemplateService;


public interface TemplateINotifierServiceConnection<T extends TemplateService<?>> {

	public void setService(T service);
}
