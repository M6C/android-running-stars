package com.cameleon.common.inotifier;

public interface INotifierMessage {
	   
    public void notifyError(Exception ex);

    public void notifyMessage(String msg);
}
