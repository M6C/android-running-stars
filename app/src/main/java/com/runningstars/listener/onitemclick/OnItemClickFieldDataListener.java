package com.runningstars.listener.onitemclick;

import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.runningstars.listener.onitemclick.IOnItemClickFieldData.FieldData;

public class OnItemClickFieldDataListener implements OnItemClickListener {

	private View field;
	private IOnItemClickFieldData iOnItemClickFieldData;
	private FieldData[] listField;

	public OnItemClickFieldDataListener(View field, IOnItemClickFieldData iOnItemClickFieldData, FieldData[] listField) {
		this.field = field;
		this.iOnItemClickFieldData = iOnItemClickFieldData;
		this.listField = listField;
	}

	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		iOnItemClickFieldData.setItemClickFieldData(field, listField[position]);
	}

}
