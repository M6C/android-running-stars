package com.runningstars.activity.inotifier;

public interface INotifierMessage {
	   
    public void notifyError(Exception ex);

    public void notifyMessage(String msg);
}
