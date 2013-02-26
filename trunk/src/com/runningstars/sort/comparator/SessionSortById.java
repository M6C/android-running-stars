package com.runningstars.sort.comparator;

import java.util.Comparator;

import org.gdocument.gtracergps.launcher.domain.Session;

public class SessionSortById implements Comparator<Session> {

	public enum TYPE {
		DESC, ASC
	}
	
	private TYPE type = TYPE.ASC;
	
	/**
	 * 
	 */
	public SessionSortById() {
		super();
	}
	
	/**
	 * @param type
	 */
	public SessionSortById(TYPE type) {
		super();
		this.type = type;
	}

	public int compare(Session ses1, Session ses2) {
		if (ses1==null || ses2==null)
			return 0;

		if (ses1.getId()==ses2.getId())
			return 0;

		return (
				type==TYPE.ASC ? 
						(ses1.getId()>ses2.getId() ? 1 : -1)
						:
						(ses1.getId()>ses2.getId() ? -1 : 1)
				);
	}

}
