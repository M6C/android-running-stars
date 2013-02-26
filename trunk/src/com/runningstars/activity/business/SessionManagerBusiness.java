package com.runningstars.activity.business;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.gdocument.gtracergps.launcher.domain.Session;
import org.gdocument.gtracergps.launcher.log.Logger;

import android.widget.ArrayAdapter;

import com.runningstars.R;
import com.runningstars.activity.SessionManagerActivity;
import com.runningstars.activity.adapter.SessionManagerAdapter;
import com.runningstars.business.Business;
import com.runningstars.sort.comparator.SessionSortById;
import com.runningstars.sort.comparator.SessionSortById.TYPE;

public class SessionManagerBusiness {
	private static final String TAG = SessionManagerBusiness.class.getCanonicalName();

	private SessionManagerActivity activity;

	private Business business;

	private List<Session> listSession;

	public SessionManagerBusiness(SessionManagerActivity activity) {
		super();
		this.activity = activity;

        business = Business.getInstance(getContext(), activity);
	}

	public SessionManagerActivity getContext() {
		return activity;
	}

	public void initData() {
		logMe("Initializing data");
		try {
			listSession = business.getSessionAllWithLocation();
		}
		catch (Exception ex) {
			listSession = null;
			ex.printStackTrace();
		}
	
		ArrayAdapter<?> adapter = null;
		if (listSession!=null && listSession.size()>0) {
			listSession = sortSession(listSession);
			adapter = new SessionManagerAdapter(activity, android.R.layout.simple_list_item_1, android.R.id.text1, listSession);
		}
		else {
			adapter = new ArrayAdapter<String>(activity, android.R.layout.simple_list_item_1, android.R.id.text1, new String[] {activity.getString(R.string.text_no_session)});
		}
		activity.setListAdapter(adapter);
	}

	public Session getSessionAt(int position) {
		return listSession.get(position);
	}

	private List<Session> sortSession(List<Session> list) {
		Session[] array = new Session[list.size()];
		list.toArray(array);
		Arrays.sort(array, new SessionSortById(TYPE.DESC));
		return Arrays.asList(array);
	}

	/**
	 * Log methodes
	 * 
	 * @param msg
	 */
	private void logMe(Exception ex) {
		logMe(ex.getMessage());
	}

	/**
     * Log methodes
     * @param msg
     */
    private void logMe(String msg) {
		Logger.logMe(TAG, msg);
    }
}
