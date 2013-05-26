package com.runningstars.activity;

import java.io.File;
import java.util.Date;
import java.util.List;

import org.gdocument.gtracergps.launcher.domain.Session;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.runningstars.R;
import com.runningstars.activity.adapter.ProfilSessionAdapter;
import com.runningstars.activity.business.ProfilBusiness;
import com.runningstars.activity.business.SessionDetailBusiness;
import com.runningstars.activity.inotifier.INotifierMessage;
import com.runningstars.factory.FactoryStyle;
import com.runningstars.listener.onchecked.OnCheckedStatTypeListener;
import com.runningstars.listener.ontouch.SessionManagerOnTouchListener;
import com.runningstars.listener.textwatcher.UserNameTextWatcherListener;
import com.runningstars.tool.ToolCalculate;

public class ProfilActivity extends Activity implements INotifierMessage, OnItemClickListener {

	private static final String PREFS_NAME = "PROFIL_RUNNING";
	protected static final int ACTIVITY_SELECT_IMAGE = 0;
	private ProfilBusiness business;
	private SharedPreferences preferences;

	private ImageView imageProfil;
	private ListView listSession;
	private ProgressBar progressSessionSend;
	private TextView nbSession;
	private TextView textDataDistanceAvg;
	private TextView textDataDistanceMax;
	private TextView textDataDistanceMin;
	private TextView textDataSpeedAvg;
	private TextView textDataSpeedMax;
	private TextView textDataSpeedMin;
	private TextView textDataTimeAvg;
	private TextView textDataTimeMax;
	private TextView textDataTimeMin;
	private TextView textDataCntSessionDistanceHi;
	private TextView textDataCntSessionDistanceLo;
	private TextView textDataCntSessionSpeedHi;
	private TextView textDataCntSessionSpeedLo;
	private TextView textDataCntSessionTimeHi;
	private TextView textDataCntSessionTimeLo;
	private TextView textDataDistanceSum;
	private TextView textDataTimeSum;
	private RadioGroup radioStatType;
	private TextView textStartEndSession;
	private TextView textGlobalDistanceTime;
	private TextView textSpeedRecord;
	private TextView textSlowestSpeed;
	private TextView textDistanceRecord;
	private TextView textShortestDistance;
	private EditText editUserName;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_profil);
		
		setTitle(R.string.title_activity_profil);
		
		preferences = getSharedPreferences(PREFS_NAME, 0);

		business = new ProfilBusiness(this);

		listSession = (ListView) findViewById(R.id.ListSession);
		listSession.setOnTouchListener(new SessionManagerOnTouchListener(this));
		listSession.setOnItemClickListener(this);

		imageProfil = (ImageView)findViewById(R.id.imageProfil);
		editUserName = (EditText)findViewById(R.id.editUserName);
		progressSessionSend = (ProgressBar) findViewById(R.id.progressSessionSend);
		nbSession = (TextView) findViewById(R.id.nbSession);
		textDataDistanceAvg = (TextView) findViewById(R.id.dataDistanceAvg);
		textDataDistanceMax = (TextView) findViewById(R.id.dataDistanceMax);
		textDataDistanceMin = (TextView) findViewById(R.id.dataDistanceMin);
		textDataSpeedAvg = (TextView) findViewById(R.id.dataSpeedAvg);
		textDataSpeedMax = (TextView) findViewById(R.id.dataSpeedMax);
		textDataSpeedMin = (TextView) findViewById(R.id.dataSpeedMin);
		textDataTimeAvg = (TextView) findViewById(R.id.dataTimeAvg);
		textDataTimeMax = (TextView) findViewById(R.id.dataTimeMax);
		textDataTimeMin = (TextView) findViewById(R.id.dataTimeMin);
		textDataCntSessionDistanceHi = (TextView) findViewById(R.id.dataCntSessionDistanceHi);
		textDataCntSessionDistanceLo = (TextView) findViewById(R.id.dataCntSessionDistanceLo);
		textDataCntSessionSpeedHi = (TextView) findViewById(R.id.dataCntSessionSpeedHi);
		textDataCntSessionSpeedLo = (TextView) findViewById(R.id.dataCntSessionSpeedLo);
		textDataCntSessionTimeHi = (TextView) findViewById(R.id.dataCntSessionTimeHi);
		textDataCntSessionTimeLo = (TextView) findViewById(R.id.dataCntSessionTimeLo);
		textDataDistanceSum = (TextView) findViewById(R.id.dataDistanceSum);
		textDataTimeSum = (TextView) findViewById(R.id.dataTimeSum);
		textStartEndSession = (TextView) findViewById(R.id.messageStartEndSession);
		textGlobalDistanceTime = (TextView) findViewById(R.id.messageGlobalDistanceTime);
		textSpeedRecord = (TextView) findViewById(R.id.messageSpeedRecord);
		textSlowestSpeed = (TextView) findViewById(R.id.messageSlowestSpeed);
		textDistanceRecord = (TextView) findViewById(R.id.messageDistanceRecord);
		textShortestDistance = (TextView) findViewById(R.id.messageShortestDistance);
		radioStatType = (RadioGroup)findViewById(R.id.radioStatType);

		editUserName.setText(preferences.getString(UserNameTextWatcherListener.USER_NAME_KEY, UserNameTextWatcherListener.USER_NAME_DEFAULT));
		editUserName.addTextChangedListener(new UserNameTextWatcherListener(preferences));
		
		radioStatType.setOnCheckedChangeListener(new OnCheckedStatTypeListener(this));
		radioStatType.check(R.id.radioMessage1);
		radioStatType.requestFocus();

		imageProfil.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
				startActivityForResult(i, ACTIVITY_SELECT_IMAGE);
			}
		});

		FactoryStyle.getInstance().centerTitle(this);
	}

		protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) { 
//			super.onActivityResult(requestCode, resultCode, imageReturnedIntent); 

			switch(requestCode) {
			case ACTIVITY_SELECT_IMAGE:
			    if(resultCode == RESULT_OK){  
			        Uri selectedImage = imageReturnedIntent.getData();
			        String[] filePathColumn = {MediaStore.Images.Media.DATA};

			        Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
			        cursor.moveToFirst();

			        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
			        String filePath = cursor.getString(columnIndex);
			        cursor.close();

					File imageProfilfile = new File(filePath);
					if (imageProfilfile.exists() && imageProfilfile.isFile()) {
						preferences.edit().putString("IMAGE_PROFIL", filePath).commit();
					}
			    }
			}
		}

	/* (non-Javadoc)
	 * @see android.app.Activity#onResume()
	 */
	@Override
	protected void onResume() {

		if (preferences.contains("IMAGE_PROFIL")) {
			String imageProfilPath = preferences.getString("IMAGE_PROFIL", "");
			File imageProfilfile = new File(imageProfilPath);
			if (imageProfilfile.exists() && imageProfilfile.isFile()) {
				Bitmap bm = BitmapFactory.decodeFile(imageProfilPath);
				int width = 211;
				int height = 211;//bm.getWidth()<bm.getHeight() ? width * bm.getHeight() / bm.getWidth() : width * bm.getWidth() / bm.getHeight();
				imageProfil.setImageBitmap(Bitmap.createScaledBitmap(bm, width, height, false));
			}
			else {
				preferences.edit().remove("IMAGE_PROFIL").commit();
			}
		}

		business.initData();
		super.onResume();
	}

	@Override
	public void onItemClick(AdapterView<?> a, View v, int position, long id) {
		Session session = business.getSessionAt(position);
		Intent intent = new Intent(this, SessionDetailActivity.class);
		intent.putExtra(SessionDetailBusiness.INTENT_KEY_SESSION, session.getId());
		startActivity(intent);
	}

	/* (non-Javadoc)
	 * @see android.app.ListActivity#setListAdapter(android.widget.ListAdapter)
	 */
	public void setListAdapter(ListAdapter adapter) {
		if (adapter instanceof ProfilSessionAdapter) {
			ProfilSessionAdapter sessionAdapter = (ProfilSessionAdapter)adapter;
			List<Session> listSession = sessionAdapter.getValue();
			int nbTotal = 0;
			long timeStart = -1l, timeStop = -1l;
			if (listSession!=null) {
				nbTotal = listSession.size();
				if (nbTotal>0) {
					double distanceSum = 0d, distanceAvg = 0d, distanceMax = 0d, distanceMin = -1d;
					long timeSum = 0l, timeAvg = 0l, timeMax = 0l, timeMin = -1l;
					float speedSum = 0f, speedAvg = 0f, speedMax = 0f, speedMin = -1f;
					Date distanceMaxDate = null, distanceMinDate = null, speedMaxDate = null, speedMinDate = null;
					for(Session session : listSession) {
						Date startDate = (session.getTimeStart()!=null) ? session.getTimeStart() : new Date();
						
						double distance = session.getCalculateDistance();
						distanceSum += distance;
						if (distanceMax<distance) {
							distanceMax = distance;
							distanceMaxDate = startDate;
						}
						if (distanceMin>distance || distanceMin==-1d) {
							distanceMin = distance;
							distanceMinDate = startDate;
						}

						float speed = ToolCalculate.getKmH(session.getCalculateElapsedTime(), session.getCalculateDistance());
						if (speedMax<speed) {
							speedMax = speed;
							speedMaxDate = startDate;
						}
						if (speedMin>speed || speedMin==-1f) {
							speedMin=speed;
							speedMinDate = startDate;
						}

						long time = session.getCalculateElapsedTime();
						timeSum += time;
						if (timeMax<time)
							timeMax = time;
						if (timeMin>time || timeMin==-1d)
							timeMin=time;

						long start = (session.getTimeStart()==null) ? 0 : session.getTimeStart().getTime();
						if (timeStart>start || timeStart==-1l)
							timeStart = start;

						long stop = (session.getTimeStop()==null) ? 0 : session.getTimeStop().getTime();
						if (timeStop<stop)
							timeStop = stop;
					}

					distanceAvg = distanceSum / nbTotal;
					speedAvg = ToolCalculate.getKmH(timeSum, distanceSum);
					timeAvg = timeSum/nbTotal;

					int cntSessionDistanceHi = 0, cntSessionDistanceLo = 0;
					int cntSessionSpeedHi = 0, cntSessionSpeedLo = 0;
					int cntSessionTimeHi = 0, cntSessionTimeLo = 0;
					int cntSessionSend = 0;
					for(Session session : listSession) {

						if (session.getCalculateDistance()>distanceAvg)
							cntSessionDistanceHi++;
						else if (session.getCalculateDistance()<distanceAvg)
							cntSessionDistanceLo++;

						float speed = ToolCalculate.getKmH(session.getCalculateElapsedTime(), session.getCalculateDistance());
						if (speed>speedAvg)
							cntSessionSpeedHi++;
						else if (speed<speedAvg)
							cntSessionSpeedLo++;

						if (session.getCalculateElapsedTime()>timeAvg)
							cntSessionTimeHi++;
						else if (session.getCalculateElapsedTime()<timeAvg)
							cntSessionTimeLo++;

						if (session.getTimeSend()!=null)
							cntSessionSend++;
					}

					String dataDistanceSum = ToolCalculate.formatDistanceKm(distanceSum);
					String dataDistanceAvg = ToolCalculate.formatDistanceKm(distanceAvg);
					String dataDistanceMax = ToolCalculate.formatDistanceKm(distanceMax);
					String dataDistanceMin = ToolCalculate.formatDistanceKm(distanceMin);

					String dataSpeedSum = ToolCalculate.formatSpeed(speedSum);
					String dataSpeedAvg = ToolCalculate.formatSpeed(speedAvg);
					String dataSpeedMax = ToolCalculate.formatSpeed(speedMax);
					String dataSpeedMin = ToolCalculate.formatSpeed(speedMin);

					String dataTimeSum = ToolCalculate.formatElapsedTime(timeSum);
					String dataTimeAvg = ToolCalculate.formatElapsedTime(timeAvg);
					String dataTimeMax = ToolCalculate.formatElapsedTime(timeMax);
					String dataTimeMin = ToolCalculate.formatElapsedTime(timeMin);

					String dataCntSessionDistanceHi = Integer.toString(cntSessionDistanceHi);
					String dataCntSessionDistanceLo = Integer.toString(cntSessionDistanceLo);
					String dataCntSessionSpeedHi = Integer.toString(cntSessionSpeedHi);
					String dataCntSessionSpeedLo = Integer.toString(cntSessionSpeedLo);
					String dataCntSessionTimeHi = Integer.toString(cntSessionTimeHi);
					String dataCntSessionTimeLo = Integer.toString(cntSessionTimeLo);
					String dataCntSessionSend = Integer.toString(cntSessionSend);
					String dataCntSession = Integer.toString(nbTotal);

					nbSession.setText(String.format(
						getResources().getString(R.string.progress_nb_session_send),
						dataCntSessionSend,
						dataCntSession
					));
					progressSessionSend.setIndeterminate(false);
					progressSessionSend.setMax(nbTotal);
					progressSessionSend.setProgress(cntSessionSend);
					textDataDistanceAvg.setText(dataDistanceAvg);
					textDataDistanceMax.setText(dataDistanceMax);
					textDataDistanceMin.setText(dataDistanceMin);
					textDataSpeedAvg.setText(dataSpeedAvg);
					textDataSpeedMax.setText(dataSpeedMax);
					textDataSpeedMin.setText(dataSpeedMin);
					textDataTimeAvg.setText(dataTimeAvg);
					textDataTimeMax.setText(dataTimeMax);
					textDataTimeMin.setText(dataTimeMin);
					textDataCntSessionDistanceHi.setText(dataCntSessionDistanceHi);
					textDataCntSessionDistanceLo.setText(dataCntSessionDistanceLo);
					textDataCntSessionSpeedHi.setText(dataCntSessionSpeedHi);
					textDataCntSessionSpeedLo.setText(dataCntSessionSpeedLo);
					textDataCntSessionTimeHi.setText(dataCntSessionTimeHi);
					textDataCntSessionTimeLo.setText(dataCntSessionTimeLo);
					textDataDistanceSum.setText(dataDistanceSum);
					textDataTimeSum.setText(dataTimeSum);

					textStartEndSession.setText(
						String.format(getResources().getString(R.string.message_start_stop_session),
							new Date(timeStart),
							new Date(timeStop)
					));
					
					textGlobalDistanceTime.setText(
						String.format(getResources().getString(R.string.message_global_distance_time),
							dataDistanceSum,
							dataTimeSum,
							dataSpeedAvg
					));
					textSpeedRecord.setText(
						String.format(getResources().getString(R.string.message_speed_record),
								dataSpeedMax,
								speedMaxDate
					));
					textSlowestSpeed.setText(
						String.format(getResources().getString(R.string.message_slowest_speed),
								dataSpeedMin,
								speedMinDate
					));
					textDistanceRecord.setText(
						String.format(getResources().getString(R.string.message_distance_record),
								dataDistanceMax,
								distanceMaxDate
					));
					textShortestDistance.setText(
						String.format(getResources().getString(R.string.message_shortest_distance),
								dataDistanceMin,
								distanceMinDate
					));
				}
			}
		}
		listSession.setAdapter(adapter);
	}

	public void notifyError(Exception ex) {
	}

	public void notifyMessage(String msg) {
	}
}