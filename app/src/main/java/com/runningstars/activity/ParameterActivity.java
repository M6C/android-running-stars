package com.runningstars.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;

import com.cameleon.common.factory.FactoryStyle;
import com.runningstars.ApplicationData;
import com.runningstars.R;
import com.runningstars.filter.NumericInputFilter;
import com.runningstars.listener.ontouch.ParameterOnTouchListener;

import org.gdocument.gtracergps.GpsConstant;
import org.gdocument.gtracergps.launcher.log.Logger;

public class ParameterActivity extends AppCompatActivity {

	private EditText gpsLocationMapZoomTxt;
	private SeekBar gpsLocationMapZoomBar;
	private EditText mapScreenShootQualityTxt;
	private SeekBar mapScreenShootQualityBar;
	private AppCompatActivity context;
	private OnSeekBarChangeListener zoomSeekBarChangeListener;
	private OnSeekBarChangeListener qualitySeekBarChangeListener;
	private ToggleButton toggleButtonLog;
	private ToggleButton toggleButtonSd;
	private ToggleButton toggleButtonSysOut;
	private ParameterOnTouchListener onTouchListener;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		context = this;
		
		setContentView(R.layout.activity_parameter);

		gpsLocationMapZoomTxt = (EditText) findViewById(R.id.gpsLocationMapZoomTxt);
		gpsLocationMapZoomBar = (SeekBar) findViewById(R.id.gpsLocationMapZoomBar);
		mapScreenShootQualityTxt = (EditText) findViewById(R.id.mapScreenShootQualityTxt);
		mapScreenShootQualityBar = (SeekBar) findViewById(R.id.mapScreenShootQuality);
		toggleButtonLog = (ToggleButton) findViewById(R.id.toggleButtonLog);
		toggleButtonSd = (ToggleButton) findViewById(R.id.toggleButtonSd);
		toggleButtonSysOut = (ToggleButton) findViewById(R.id.toggleButtonSysOut);

		FactoryStyle.getInstance().centerTitle(this);

		initializeMapZoomTxt();
		initializeMapZoomBar();
		initializeMapScreenShootQualityTxt();
		initializeMapScreenShootQualityBar();
		initializeToggleButtonLog();
		initializeToggleButtonSd();
		initializeToggleButtonSysOut();

		enableOnTouchListenerMain();
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onBackPressed()
	 */
	@Override
	public void onBackPressed() {
		finish();
	}

	public void enableOnTouchListenerMain() {

		if (onTouchListener == null)
			onTouchListener = new ParameterOnTouchListener(this);

		// Set the touch listener for the main view to be our custom gesture
		// listener
		findViewById(R.id.mainLayout).setOnTouchListener(onTouchListener);
	}

	public void disableOnTouchListenerMain() {
		// Unset the touch listener for the main view
		findViewById(R.id.mainLayout).setOnTouchListener(null);
	}

	private void initializeMapZoomBar() {
		gpsLocationMapZoomBar.setProgress(ApplicationData.getInstance(this).getGpsLocationMapZoom());

		zoomSeekBarChangeListener = new OnSeekBarChangeListener() {

			public void onStopTrackingTouch(SeekBar seekBar) {
			}
			
			public void onStartTrackingTouch(SeekBar seekBar) {
			}
			
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				ApplicationData.getInstance(context).setGpsLocationMapZoom(progress);//seekBar.getProgress());
				gpsLocationMapZoomTxt.setText(Integer.toString(ApplicationData.getInstance(context).getGpsLocationMapZoom()));
			}
		};
		gpsLocationMapZoomBar.setOnSeekBarChangeListener(zoomSeekBarChangeListener);
	}
	
	private void initializeMapZoomTxt() {
		gpsLocationMapZoomTxt.setText(Integer.toString(ApplicationData.getInstance(this).getGpsLocationMapZoom()));

		gpsLocationMapZoomTxt.setFilters(new InputFilter[]{new NumericInputFilter()}); 
		TextWatcher textWatcher = new TextWatcher() {
			
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}
			
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}
			
			public void afterTextChanged(Editable s) {
				String str = s.toString();
				int value = str==null || "".equals(str) ? 0 : Integer.parseInt(str);
				ApplicationData.getInstance(context).setGpsLocationMapZoom(value);
				gpsLocationMapZoomBar.setOnSeekBarChangeListener(null);
				gpsLocationMapZoomBar.setProgress(value);
				gpsLocationMapZoomBar.setOnSeekBarChangeListener(zoomSeekBarChangeListener);
			}
		};
		gpsLocationMapZoomTxt.addTextChangedListener(textWatcher);
	}

	private void initializeMapScreenShootQualityBar() {
		mapScreenShootQualityBar.setProgress(ApplicationData.getInstance(this).getMapScreenShootQuality());

		qualitySeekBarChangeListener = new OnSeekBarChangeListener() {

			public void onStopTrackingTouch(SeekBar seekBar) {
			}
			
			public void onStartTrackingTouch(SeekBar seekBar) {
			}
			
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				ApplicationData.getInstance(context).setMapScreenShootQuality(progress);//seekBar.getProgress());
				mapScreenShootQualityTxt.setText(Integer.toString(ApplicationData.getInstance(context).getMapScreenShootQuality()));
			}
		};
		mapScreenShootQualityBar.setOnSeekBarChangeListener(qualitySeekBarChangeListener);
	}
	
	private void initializeMapScreenShootQualityTxt() {
		mapScreenShootQualityTxt.setText(Integer.toString(ApplicationData.getInstance(this).getMapScreenShootQuality()));

		mapScreenShootQualityTxt.setFilters(new InputFilter[]{new NumericInputFilter()}); 
		TextWatcher textWatcher = new TextWatcher() {
			
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}
			
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}
			
			public void afterTextChanged(Editable s) {
				String str = s.toString();
				int value = str==null || "".equals(str) ? 0 : Integer.parseInt(str);
				ApplicationData.getInstance(context).setMapScreenShootQuality(value);
				mapScreenShootQualityBar.setOnSeekBarChangeListener(null);
				mapScreenShootQualityBar.setProgress(value);
				mapScreenShootQualityBar.setOnSeekBarChangeListener(qualitySeekBarChangeListener);
			}
		};
		mapScreenShootQualityTxt.addTextChangedListener(textWatcher);
	}

	private void initializeToggleButtonLog() {
		toggleButtonLog.setChecked(GpsConstant.LOG_WRITE);

		toggleButtonLog.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				GpsConstant.LOG_WRITE = isChecked;
			}
		});
	}

	private void initializeToggleButtonSd() {
		toggleButtonSd.setChecked(GpsConstant.LOG_WRITE_SD);

		toggleButtonSd.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				GpsConstant.LOG_WRITE_SD = isChecked;
				if (GpsConstant.LOG_WRITE_SD)
					Logger.initLogSD();
			}
		});
	}
	
	private void initializeToggleButtonSysOut() {
		toggleButtonSysOut.setChecked(GpsConstant.LOG_WRITE_SYSOUT);

		toggleButtonSysOut.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				GpsConstant.LOG_WRITE_SYSOUT = isChecked;
			}
		});
	}
}