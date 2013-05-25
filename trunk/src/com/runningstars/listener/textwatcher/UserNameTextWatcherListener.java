package com.runningstars.listener.textwatcher;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.Editable;
import android.text.TextWatcher;

public class UserNameTextWatcherListener implements TextWatcher {
	
	public static final String USER_NAME_KEY = "USER_NAME";
	public static final String USER_NAME_DEFAULT = "";
	
	private SharedPreferences preference;

	public UserNameTextWatcherListener(SharedPreferences preference) {
		this.preference = preference;
	}
	
	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		Editor editor = preference.edit();
		editor.putString(USER_NAME_KEY, s.toString());
		editor.commit();
	}
	
	@Override
	public void beforeTextChanged(CharSequence s, int start, int count, int after) {
	}
	
	@Override
	public void afterTextChanged(Editable s) {
	}
}