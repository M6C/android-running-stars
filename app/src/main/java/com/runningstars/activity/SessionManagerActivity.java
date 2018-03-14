package com.runningstars.activity;

import java.util.List;

import org.gdocument.gtracergps.launcher.domain.Session;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.cameleon.common.factory.FactoryStyle;
import com.cameleon.common.inotifier.INotifierMessage;
import com.runningstars.R;
import com.runningstars.activity.adapter.SessionManagerAdapter;
import com.runningstars.activity.business.SessionDetailBusiness;
import com.runningstars.activity.business.SessionManagerBusiness;
import com.runningstars.listener.ontouch.SessionManagerOnTouchListener;
import com.runningstars.tool.ToolCalculate;

public class SessionManagerActivity extends ListActivity implements INotifierMessage {

	private SessionManagerBusiness business;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		business = new SessionManagerBusiness(this);

		getListView().setOnTouchListener(new SessionManagerOnTouchListener(this));
		
		FactoryStyle.getInstance().centerTitle(this);
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onResume()
	 */
	@Override
	protected void onResume() {
		business.initData();
		super.onResume();
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		Session session = business.getSessionAt(position);
		Intent intent = new Intent(this, SessionDetailActivity.class);
		intent.putExtra(SessionDetailBusiness.INTENT_KEY_SESSION, session.getId());
		startActivity(intent);
	}

	/* (non-Javadoc)
	 * @see android.app.ListActivity#setListAdapter(android.widget.ListAdapter)
	 */
	@Override
	public void setListAdapter(ListAdapter adapter) {
		if (adapter instanceof SessionManagerAdapter) {
			SessionManagerAdapter sessionAdapter = (SessionManagerAdapter)adapter;
			List<Session> listSession = sessionAdapter.getValue();
			int nbSend = 0, nbTotal = 0;
			double distance = 0d;
			long time = 0l; 
			if (listSession!=null) {
				nbTotal = listSession.size();
				for(Session session : listSession) {
					if (session.getTimeSend()!=null)
						nbSend++;
					distance += session.getCalculateDistance();
					time += session.getCalculateElapsedTime();
				}
			}
			setTitle(String.format(
				getResources().getString(R.string.title_activity_session_manager), 
				nbSend, 
				nbTotal,
				ToolCalculate.formatElapsedTime(time),
				ToolCalculate.formatDistanceKm(distance)
			));
		}
		super.setListAdapter(adapter);
	}

	public void notifyError(Exception ex) {
	}

	public void notifyMessage(String msg) {
	}
}