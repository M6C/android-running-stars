package com.runningstars.listener.onclick;

import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.widget.AdapterView.OnItemClickListener;

import com.cameleon.common.factory.FactoryDialog;
import com.runningstars.R;
import com.runningstars.listener.onitemclick.IOnItemClickFieldData;
import com.runningstars.listener.onitemclick.IOnItemClickFieldData.FieldData;
import com.runningstars.listener.onitemclick.OnItemClickFieldDataListener;

public class OnClickFieldDataListener implements OnLongClickListener {

	private Context context;
	private View field;
	private IOnItemClickFieldData iOnItemClickFieldData;

	public OnClickFieldDataListener(Context context, View field, IOnItemClickFieldData iOnItemClickFieldData) {
		this.context = context;
		this.field = field;
		this.iOnItemClickFieldData = iOnItemClickFieldData;
	}

	public boolean onLongClick(View v) {

		ArrayList<String> arrayText = new ArrayList<String>();
		ArrayList<FieldData> arrayField = new ArrayList<FieldData>();

		if (iOnItemClickFieldData.isFieldDataSelectable(FieldData.ADDRESS)) {
			arrayText.add(context.getString(R.string.dialog_field_data_address));
			arrayField.add(FieldData.ADDRESS);
		}
		if (iOnItemClickFieldData.isFieldDataSelectable(FieldData.SPEED)) {
			arrayText.add(context.getString(R.string.dialog_field_data_speed));
			arrayField.add(FieldData.SPEED);
		}
		if (iOnItemClickFieldData.isFieldDataSelectable(FieldData.DISTANCE)) {
			arrayText.add(context.getString(R.string.dialog_field_data_distance));
			arrayField.add(FieldData.DISTANCE);
		}
		if (iOnItemClickFieldData.isFieldDataSelectable(FieldData.ELAPSED_TIME)) {
			arrayText.add(context.getString(R.string.dialog_field_data_elapsed_time));
			arrayField.add(FieldData.ELAPSED_TIME);
		}
		if (iOnItemClickFieldData.isFieldDataSelectable(FieldData.SPEED_AVERAGE)) {
			arrayText.add(context.getString(R.string.dialog_field_data_speed_average));
			arrayField.add(FieldData.SPEED_AVERAGE);
		}
		if (iOnItemClickFieldData.isFieldDataSelectable(FieldData.MAP)) {
			arrayText.add(context.getString(R.string.dialog_field_data_map));
			arrayField.add(FieldData.MAP);
		}

		if (arrayText.size()==0) {
			FieldData currentFieldData = iOnItemClickFieldData.getItemClickFieldData(field);
			if (currentFieldData==null || !currentFieldData.equals(FieldData.ADDRESS)) {
				arrayText.add(context.getString(R.string.dialog_field_data_address));
				arrayField.add(FieldData.ADDRESS);
			}
			if (currentFieldData==null || !currentFieldData.equals(FieldData.SPEED)) {
				arrayText.add(context.getString(R.string.dialog_field_data_speed));
				arrayField.add(FieldData.SPEED);
			}
			if (currentFieldData==null || !currentFieldData.equals(FieldData.DISTANCE)) {
				arrayText.add(context.getString(R.string.dialog_field_data_distance));
				arrayField.add(FieldData.DISTANCE);
			}
			if (currentFieldData==null || !currentFieldData.equals(FieldData.ELAPSED_TIME)) {
				arrayText.add(context.getString(R.string.dialog_field_data_elapsed_time));
				arrayField.add(FieldData.ELAPSED_TIME);
			}
			if (currentFieldData==null || !currentFieldData.equals(FieldData.SPEED_AVERAGE)) {
				arrayText.add(context.getString(R.string.dialog_field_data_speed_average));
				arrayField.add(FieldData.SPEED_AVERAGE);
			}
			if (currentFieldData==null || !currentFieldData.equals(FieldData.MAP)) {
				arrayText.add(context.getString(R.string.dialog_field_data_map));
				arrayField.add(FieldData.MAP);
			}
		}

		String[] listText = arrayText.toArray(new String[arrayText.size()]);
		FieldData[] listField = arrayField.toArray(new FieldData[arrayField.size()]);

		OnItemClickListener listener = new OnItemClickFieldDataListener(field, iOnItemClickFieldData, listField);
		FactoryDialog.getInstance().buildListView(context, R.string.dialog_field_data_title, listText, listener).show();
		
		return true;
	}
}