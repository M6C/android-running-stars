package com.runningstars.activity;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.gdocument.gtracergps.launcher.domain.Session;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.cameleon.common.factory.FactoryDialog;
import com.cameleon.common.factory.FactoryLayoutInFlater;
import com.cameleon.common.factory.FactoryStyle;
import com.cameleon.common.inotifier.INotifierMessage;
import com.runningstars.R;
import com.runningstars.activity.business.SessionDetailBusiness;
import com.runningstars.listener.onclick.OnDoubleClickListener;
import com.runningstars.listener.onitemclick.OnItemClickSessionDetailManageListener;
import com.runningstars.listener.onitemclick.OnItemClickSessionDetailTrackListener;
import com.runningstars.listener.ontouch.SessionDetailOnTouchListener;
import com.runningstars.tool.ToolCalculate;

public class SessionDetailActivity extends Activity implements INotifierMessage {

	private static final int MANAGE_MENU_ITEM = Menu.FIRST;
	private static final int TRACK_MENU_ITEM = MANAGE_MENU_ITEM + 1;

	private SessionDetailBusiness business;

	private TextView textStart;
	private TextView textEnd;
	private TextView textElapsedTime;
	private TextView textDistance;
	private TextView textKmH;
	private ImageView imageMap;

	private SessionDetailOnTouchListener onTouchListener;
	private ProgressDialog mProgressDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_session_detail);

        business = new SessionDetailBusiness(this);

		textStart = (TextView)findViewById(R.id.TextStart);
		textEnd = (TextView)findViewById(R.id.TextEnd);
		textElapsedTime = (TextView)findViewById(R.id.TextElapsedTime);
		textDistance = (TextView)findViewById(R.id.TextDistance);
		textKmH = (TextView)findViewById(R.id.TextKmH);
		imageMap = (ImageView)findViewById(R.id.imageMap);
		
		imageMap.setOnClickListener(new OnDoubleClickListener() {
			@Override
			public void onDoubleClick(View v) {
				business.map();
			}
		});

		FactoryStyle.getInstance().centerTitle(this);

		iniIhm();
	}

	@Override
	protected void onPause() {
		business.onPause();
		super.onPause();
	}

	@Override
	protected void onResume() {
		business.onResume();
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		business.onDestroy();
		super.onDestroy();
	}

	@Override
	public void onBackPressed() {
		finish();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, MANAGE_MENU_ITEM, 0, "Manage");
		menu.add(0, TRACK_MENU_ITEM, 1, "Track");

		// Change Option Menu Color
		getLayoutInflater().setFactory(FactoryLayoutInFlater.getInstance().createOptionsMenuBackground());

		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case MANAGE_MENU_ITEM: {
				onOptionsManage();
				break;
			}
			case TRACK_MENU_ITEM: {
				onOptionsTrack();
				break;
			}
		}
		return super.onOptionsItemSelected(item);
	}

	private void onOptionsManage() {
		String[] textList = {
			this.getString(R.string.dialog_session_detail_delete),
			this.getString(R.string.dialog_session_detail_repair),
			this.getString(R.string.dialog_session_detail_send)
		};
		OnItemClickListener listener = new OnItemClickSessionDetailManageListener(business);
		FactoryDialog.getInstance().buildListView(this, R.string.dialog_session_title_manage, textList, listener).show();
	}

	private void onOptionsTrack() {
		String[] textList = {
			this.getString(R.string.dialog_session_detail_kml),
			this.getString(R.string.dialog_session_detail_map)
		};
		OnItemClickListener listener = new OnItemClickSessionDetailTrackListener(business);
		FactoryDialog.getInstance().buildListView(this, R.string.dialog_session_title_track, textList, listener).show();
	}

	public void iniIhm() {
		setTitle(getResources().getString(R.string.title_activity_session_detail, business.getSession().getTimeStart(), business.getSession().getTimeStop()));

		Session session = business.getSession();
		textStart.setText(business.getStart()==null ? "" : business.getStart().getSimpleAddress());
		textEnd.setText(business.getEnd()==null ? "" : business.getEnd().getSimpleAddress());
		textElapsedTime.setText(ToolCalculate.formatElapsedTime(session));
		textDistance.setText(ToolCalculate.formatDistance(session));
		textKmH.setText(ToolCalculate.formatSpeedKmH(session));

		byte[] buf = session.getPngMapScreenShoot();
    	if (buf!=null) {
			ByteArrayInputStream bais = null;
			try {
	    		bais = new ByteArrayInputStream(buf);
	    		imageMap.setImageBitmap(BitmapFactory.decodeStream(bais));
			}
			finally {
				if (bais!=null) {
					try {
						bais.close();
					} catch (IOException e) {
				    	imageMap.setImageResource(R.drawable.no_image);
					}
				}
			}
		}
		else {
	    	imageMap.setImageResource(R.drawable.no_image);
		}

		enableOnTouchListenerMain();
	}

	public void enableOnTouchListenerMain() {

		if (onTouchListener == null)
			onTouchListener = new SessionDetailOnTouchListener(this);

		// Set the touch listener for the main view to be our custom gesture
		// listener
		findViewById(R.id.mainLayout).setOnTouchListener(onTouchListener);
	}

	public void disableOnTouchListenerMain() {
		// Unset the touch listener for the main view
		findViewById(R.id.mainLayout).setOnTouchListener(null);
	}

	public SessionDetailBusiness getBusiness() {
		return business;
	}

	public void notifyError(Exception ex) {
	}

	public void notifyMessage(String msg) {
	}
}