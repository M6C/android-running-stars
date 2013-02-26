package com.runningstars.factory;

import com.runningstars.R;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface.OnClickListener;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class FactoryDialog {

	private static FactoryDialog instance;

	private FactoryDialog() {
	}

	public static FactoryDialog getInstance() {
		if (instance==null)
			instance = new FactoryDialog();
		return instance;
	}

	public Dialog buildListView(Context context, int titleId, String[] textList, final OnItemClickListener listener) {
		//http://stackoverflow.com/questions/2874191/is-it-possible-to-create-listview-inside-dialog
		//http://stackoverflow.com/questions/7302365/style-attributes-of-custom-styled-alertdialog
		AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(context, R.style.AlertDialogTheme));

		ListView modeList = new ListView(context);//new ContextThemeWrapper(context, R.style.AlertDialogList));
		builder.setView(modeList);

		View customTitle = View.inflate( context, R.layout.alert_dialog_title, null );
		TextView title = (TextView) customTitle.findViewById( R.id.alertTitle );
		builder.setCustomTitle(customTitle);
		title.setText(titleId);
		
		final Dialog dialog = builder.create();
//		dialog.setTitle(titleId);

		ArrayAdapter<String> modeAdapter = new ArrayAdapter<String>(new ContextThemeWrapper(context, R.style.AlertDialogItem), android.R.layout.simple_list_item_1, android.R.id.text1, textList);
		modeList.setAdapter(modeAdapter);

		modeList.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				listener.onItemClick(parent, view, position, id);
				dialog.dismiss();
			}
		});

		return dialog;
	}

	public void buildOkDialog(Context context, String title, String message) {
//		AlertDialog.Builder builder = new Builder(context);
		AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(context, R.style.AlertDialogTheme));
		builder.setTitle(title);
		builder.setMessage(message);
		builder.setPositiveButton("OK", null);
		builder.create().show();
	}

	public Dialog buildOkCancelDialog(Context context, OnClickListener onClickOkListener, int titleId, int messageId) {
//		AlertDialog.Builder builder = new Builder(context);
		AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(context, R.style.AlertDialogTheme));
		builder.setTitle(titleId);
		builder.setMessage(messageId);
		builder.setPositiveButton("OK", onClickOkListener);
		builder.setNeutralButton("Cancel", null);
		return builder.create();
	}
}
