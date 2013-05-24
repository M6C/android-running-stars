package com.runningstars.listener.onchecked;

import android.app.Activity;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.runningstars.R;

public class OnCheckedStatTypeListener implements OnCheckedChangeListener {
	
	private View tabAvgMaxMin;
	private View tabHigherLower;
	private View tabMessage1;
	private View tabMessage2;
	private View tabMessage3;
	private View tabMessage4;
	private View tabMessage5;
	
	public OnCheckedStatTypeListener(Activity context) {
		super();
		
		tabAvgMaxMin = context.findViewById(R.id.tabAvgMaxMin);
		tabHigherLower = context.findViewById(R.id.tabHigherLower);
		tabMessage1 = context.findViewById(R.id.tabMessage1);
		tabMessage2 = context.findViewById(R.id.tabMessage2);
		tabMessage3 = context.findViewById(R.id.tabMessage3);
		tabMessage4 = context.findViewById(R.id.tabMessage4);
		tabMessage5 = context.findViewById(R.id.tabMessage5);
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
				tabAvgMaxMin.setVisibility(View.GONE);
				tabHigherLower.setVisibility(View.GONE);
				break;
			case R.id.radioMessage2:
				tabMessage1.setVisibility(View.GONE);
				tabMessage2.setVisibility(View.VISIBLE);
				tabMessage3.setVisibility(View.GONE);
				tabMessage4.setVisibility(View.GONE);
				tabMessage5.setVisibility(View.GONE);
				tabAvgMaxMin.setVisibility(View.GONE);
				tabHigherLower.setVisibility(View.GONE);
				break;
			case R.id.radioMessage3:
				tabMessage1.setVisibility(View.GONE);
				tabMessage2.setVisibility(View.GONE);
				tabMessage3.setVisibility(View.VISIBLE);
				tabMessage4.setVisibility(View.GONE);
				tabMessage5.setVisibility(View.GONE);
				tabAvgMaxMin.setVisibility(View.GONE);
				tabHigherLower.setVisibility(View.GONE);
				break;
			case R.id.radioMessage4:
				tabMessage1.setVisibility(View.GONE);
				tabMessage2.setVisibility(View.GONE);
				tabMessage3.setVisibility(View.GONE);
				tabMessage4.setVisibility(View.VISIBLE);
				tabMessage5.setVisibility(View.GONE);
				tabAvgMaxMin.setVisibility(View.GONE);
				tabHigherLower.setVisibility(View.GONE);
				break;
			case R.id.radioMessage5:
				tabMessage1.setVisibility(View.GONE);
				tabMessage2.setVisibility(View.GONE);
				tabMessage3.setVisibility(View.GONE);
				tabMessage4.setVisibility(View.GONE);
				tabMessage5.setVisibility(View.VISIBLE);
				tabAvgMaxMin.setVisibility(View.GONE);
				tabHigherLower.setVisibility(View.GONE);
				break;
			case R.id.radioStatAvgMaxMin:
				tabMessage1.setVisibility(View.GONE);
				tabMessage2.setVisibility(View.GONE);
				tabMessage3.setVisibility(View.GONE);
				tabMessage4.setVisibility(View.GONE);
				tabMessage5.setVisibility(View.GONE);
				tabAvgMaxMin.setVisibility(View.VISIBLE);
				tabHigherLower.setVisibility(View.GONE);
				break;
			case R.id.radioStatHigherLower:
				tabMessage1.setVisibility(View.GONE);
				tabMessage2.setVisibility(View.GONE);
				tabMessage3.setVisibility(View.GONE);
				tabMessage4.setVisibility(View.GONE);
				tabMessage5.setVisibility(View.GONE);
				tabAvgMaxMin.setVisibility(View.GONE);
				tabHigherLower.setVisibility(View.VISIBLE);
				break;
		}
	}
}