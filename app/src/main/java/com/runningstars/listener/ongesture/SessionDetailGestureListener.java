package com.runningstars.listener.ongesture;

import org.gdocument.gtracergps.launcher.domain.Session;

import android.content.Context;
import android.content.Intent;

import com.runningstars.activity.SessionDetailActivity;
import com.runningstars.activity.business.SessionDetailBusiness;
import com.runningstars.business.SessionBusiness;


public class SessionDetailGestureListener extends AbstractGestureListener {

	private SessionDetailBusiness business;
	private SessionBusiness sessionBusiness;

	public SessionDetailGestureListener(SessionDetailActivity context) {
		super(context);
		business = context.getBusiness();
		sessionBusiness = SessionBusiness.getInstance(context, context);
	}

	@Override
	protected Intent getFlingRight(Context context) {
		Intent intent = null;
		Session session = sessionBusiness.getNext(business.getSession());
		if (session!=null) {
			intent = new Intent(context, SessionDetailActivity.class);
			intent.putExtra(SessionDetailBusiness.INTENT_KEY_SESSION, session.getId());
		}
		return intent;
	}

	@Override
	protected Intent getFlingLeft(Context context) {
		Intent intent = null;
		Session session = sessionBusiness.getPrevious(business.getSession());
		if (session!=null) {
			intent = new Intent(context, SessionDetailActivity.class);
			intent.putExtra(SessionDetailBusiness.INTENT_KEY_SESSION, session.getId());
		}
		return intent;
	}
}
