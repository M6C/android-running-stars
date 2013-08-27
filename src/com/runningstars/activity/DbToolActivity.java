package com.runningstars.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.cameleon.common.factory.FactoryStyle;
import com.cameleon.common.inotifier.INotifierMessage;
import com.runningstars.ApplicationData;
import com.runningstars.R;
import com.runningstars.activity.business.DbToolBusiness;
import com.runningstars.listener.ontouch.DbToolOnTouchListener;
import com.runningstars.listener.view.ViewOnClickBackupDbListener;
import com.runningstars.listener.view.ViewOnClickDropTableListener;
import com.runningstars.listener.view.ViewOnClickRestoreDbListener;
import com.runningstars.listener.view.ViewOnClickSendDataListener;

public class DbToolActivity extends Activity implements INotifierMessage {

	private TextView edtUrl;

    private Handler handler;

	private DbToolBusiness business;

    /********************************************************************** 
     * Activity overrides below 
     **********************************************************************/
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dbtool);

        // Set the touch listener for the main view to be our custom gesture listener
		findViewById(R.id.mainLayout).setOnTouchListener(new DbToolOnTouchListener(this));

        handler = new Handler();

        business = new DbToolBusiness(this);

        // get a handle to our TextView so we can write to it later
        edtUrl = (TextView) findViewById(R.id.edtUrl);
    	edtUrl.setText(ApplicationData.getInstance(this).getMysqlServerUrl());

		// Send button
        Button btnCreateProduct = (Button) findViewById(R.id.btnSendLocalisation);
        // button click event
        btnCreateProduct.setOnClickListener(new ViewOnClickSendDataListener(business));

        // Db Backup button
        Button btnDbBackup = (Button) findViewById(R.id.btnDbBackup);
        // button click event
        btnDbBackup.setOnClickListener(new ViewOnClickBackupDbListener(business));

        // Db Restore button
        Button btnDbRestore = (Button) findViewById(R.id.btnDbRestore);
        // button click event
        btnDbRestore.setOnClickListener(new ViewOnClickRestoreDbListener(business));

        // Drop Table button
        Button btnDropTable = (Button) findViewById(R.id.btnRecreateTable);
        // button click event
        btnDropTable.setOnClickListener(new ViewOnClickDropTableListener(business));

        FactoryStyle.getInstance().centerTitle(this);
    }

    /* (non-Javadoc)
	 * @see android.app.Activity#onResume()
	 */
	@Override
	protected void onResume() {
		business.onResume();
		super.onResume();
	}

    /* (non-Javadoc)
	 * @see android.app.Activity#onPause()
	 */
	@Override
	protected void onPause() {
		business.onPause();
		super.onPause();
	}
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onDestroy()
	 */
	@Override
	protected void onDestroy() {
		super.onDestroy();
		ApplicationData.getInstance(this).setMysqlServerUrl(getUrl());
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onBackPressed()
	 */
	@Override
	public void onBackPressed() {
		finish();
	}

    public void runInHandler(Runnable runnable) {
    	handler.post(runnable);
    }

    public Handler getHandler() {
    	return handler;
    }

	/**
	 * Obtaine url field content
	 * @return Url
	 */
	public String getUrl() {
		return edtUrl.getText().toString();
	}

	public void notifyError(Exception ex) {
        Toast.makeText(this, ex.toString(), Toast.LENGTH_LONG).show();
    }

    public void notifyMessage(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
     }

	/**
	 * @return the business
	 */
	public DbToolBusiness getBusiness() {
		return business;
	}
}