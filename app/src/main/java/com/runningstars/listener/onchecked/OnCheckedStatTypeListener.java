package com.runningstars.listener.onchecked;

import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import androidx.appcompat.app.AppCompatActivity;

import com.runningstars.R;

public class OnCheckedStatTypeListener implements OnCheckedChangeListener, OnClickListener {
	
	private View tabAvgMaxMin;
	private View tabHigherLower;
	private View tabMessage1;
	private View tabMessage2;
	private View tabMessage3;
	private View tabMessage4;
	private View tabMessage5;
	private View tabMessage6;
	private AppCompatActivity context;
	private Handler handler;
	private Runnable runner;
	
	public OnCheckedStatTypeListener(AppCompatActivity context) {
		super();
		this.context = context;
		this.handler = new Handler();

		tabMessage1 = context.findViewById(R.id.tabMessage1);
		tabMessage2 = context.findViewById(R.id.tabMessage2);
		tabMessage3 = context.findViewById(R.id.tabMessage3);
		tabMessage4 = context.findViewById(R.id.tabMessage4);
		tabMessage5 = context.findViewById(R.id.tabMessage5);
		tabMessage6 = context.findViewById(R.id.tabMessage6);
		tabAvgMaxMin = context.findViewById(R.id.tabAvgMaxMin);
		tabHigherLower = context.findViewById(R.id.tabHigherLower);
		
		tabMessage1.setOnClickListener(this);
		tabMessage2.setOnClickListener(this);
		tabMessage3.setOnClickListener(this);
		tabMessage4.setOnClickListener(this);
		tabMessage5.setOnClickListener(this);
		tabMessage6.setOnClickListener(this);
		tabAvgMaxMin.setOnClickListener(this);
		tabHigherLower.setOnClickListener(this);
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		switch(checkedId) {
			case R.id.radioMessage1:
				tabMessage1.setVisibility(View.VISIBLE);
				tabMessage2.setVisibility(View.GONE);
				tabMessage3.setVisibility(View.GONE);
				tabMessage4.setVisibility(View.GONE);
				tabMessage5.setVisibility(View.GONE);
				tabMessage6.setVisibility(View.GONE);
				tabAvgMaxMin.setVisibility(View.GONE);
				tabHigherLower.setVisibility(View.GONE);
				cronCheckNextTab(R.id.radioMessage2);
				break;
			case R.id.radioMessage2:
				tabMessage1.setVisibility(View.GONE);
				tabMessage2.setVisibility(View.VISIBLE);
				tabMessage3.setVisibility(View.GONE);
				tabMessage4.setVisibility(View.GONE);
				tabMessage5.setVisibility(View.GONE);
				tabMessage6.setVisibility(View.GONE);
				tabAvgMaxMin.setVisibility(View.GONE);
				tabHigherLower.setVisibility(View.GONE);
				cronCheckNextTab(R.id.radioMessage3);
				break;
			case R.id.radioMessage3:
				tabMessage1.setVisibility(View.GONE);
				tabMessage2.setVisibility(View.GONE);
				tabMessage3.setVisibility(View.VISIBLE);
				tabMessage4.setVisibility(View.GONE);
				tabMessage5.setVisibility(View.GONE);
				tabMessage6.setVisibility(View.GONE);
				tabAvgMaxMin.setVisibility(View.GONE);
				tabHigherLower.setVisibility(View.GONE);
				cronCheckNextTab(R.id.radioMessage4);
				break;
			case R.id.radioMessage4:
				tabMessage1.setVisibility(View.GONE);
				tabMessage2.setVisibility(View.GONE);
				tabMessage3.setVisibility(View.GONE);
				tabMessage4.setVisibility(View.VISIBLE);
				tabMessage5.setVisibility(View.GONE);
				tabMessage6.setVisibility(View.GONE);
				tabAvgMaxMin.setVisibility(View.GONE);
				tabHigherLower.setVisibility(View.GONE);
				cronCheckNextTab(R.id.radioMessage5);
				break;
			case R.id.radioMessage5:
				tabMessage1.setVisibility(View.GONE);
				tabMessage2.setVisibility(View.GONE);
				tabMessage3.setVisibility(View.GONE);
				tabMessage4.setVisibility(View.GONE);
				tabMessage5.setVisibility(View.VISIBLE);
				tabMessage6.setVisibility(View.GONE);
				tabAvgMaxMin.setVisibility(View.GONE);
				tabHigherLower.setVisibility(View.GONE);
				cronCheckNextTab(R.id.radioMessage6);
				break;
			case R.id.radioMessage6:
				tabMessage1.setVisibility(View.GONE);
				tabMessage2.setVisibility(View.GONE);
				tabMessage3.setVisibility(View.GONE);
				tabMessage4.setVisibility(View.GONE);
				tabMessage5.setVisibility(View.GONE);
				tabMessage6.setVisibility(View.VISIBLE);
				tabAvgMaxMin.setVisibility(View.GONE);
				tabHigherLower.setVisibility(View.GONE);
				cronCheckNextTab(R.id.radioStatAvgMaxMin);
				break;
			case R.id.radioStatAvgMaxMin:
				tabMessage1.setVisibility(View.GONE);
				tabMessage2.setVisibility(View.GONE);
				tabMessage3.setVisibility(View.GONE);
				tabMessage4.setVisibility(View.GONE);
				tabMessage5.setVisibility(View.GONE);
				tabMessage6.setVisibility(View.GONE);
				tabAvgMaxMin.setVisibility(View.VISIBLE);
				tabHigherLower.setVisibility(View.GONE);
				cronCheckNextTab(R.id.radioStatHigherLower);
				break;
			case R.id.radioStatHigherLower:
				tabMessage1.setVisibility(View.GONE);
				tabMessage2.setVisibility(View.GONE);
				tabMessage3.setVisibility(View.GONE);
				tabMessage4.setVisibility(View.GONE);
				tabMessage5.setVisibility(View.GONE);
				tabMessage6.setVisibility(View.GONE);
				tabAvgMaxMin.setVisibility(View.GONE);
				tabHigherLower.setVisibility(View.VISIBLE);
				cronCheckNextTab(R.id.radioMessage1);
				break;
		}
	}
	
	private void cronCheckNextTab(final int id) {
		if (runner!=null)
			handler.removeCallbacks(runner);
		runner = new Runnable() {
			@Override
			public void run() {
				((RadioButton)context.findViewById(id)).setChecked(true);
			}
		};
		handler.postDelayed(runner, 1000*3);
	}

	@Override
	public void onClick(View arg0) {
		handler.removeCallbacks(runner);
	}
}