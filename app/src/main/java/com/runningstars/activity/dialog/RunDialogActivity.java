package com.runningstars.activity.dialog;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.runningstars.R;
import com.runningstars.activity.dialog.business.RunDialogBusiness;

public class RunDialogActivity extends AppCompatActivity {

	private RunDialogBusiness business;

	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_dialog_run);
		
		business = new RunDialogBusiness(this);
	}

}
