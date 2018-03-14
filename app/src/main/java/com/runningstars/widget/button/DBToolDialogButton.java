package com.runningstars.widget.button;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;

import com.cameleon.common.factory.FactoryDialog;
import com.runningstars.R;

public class DBToolDialogButton extends Button {

	public DBToolDialogButton(Context context) {
		super(context);

      setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				String[] textList = {
						getContext().getString(R.string.btn_backup_db_to_sd),
						getContext().getString(R.string.btn_restore_db_from_sd),
						getContext().getString(R.string.btn_recreate_table),
						getContext().getString(R.string.btn_send_localisation)
				};
				OnItemClickListener listener = new OnItemClickListener() {

					public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
						switch (position) {
						case 0:
//							business.dbBackup();
							break;

						default:
							break;
						}
					}					
				};
				Dialog dialog = FactoryDialog.getInstance().buildListView(getContext(), R.string.dialog_title_menu, textList, listener);
				dialog.show();
			}
		});
	}

}
