package com.runningstars.listener.onitemclick;

import android.view.View;

public interface IOnItemClickFieldData {

	public enum FieldData {
	    ADDRESS, SPEED, DISTANCE, ELAPSED_TIME, KMH, SPEED_AVERAGE, MAP
	};

	public void setItemClickFieldData(View view, FieldData fieldData);
	
	public FieldData getItemClickFieldData(View view);

	public boolean isFieldDataSelectable(FieldData fieldData);
}
