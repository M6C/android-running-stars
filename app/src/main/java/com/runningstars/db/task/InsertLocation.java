package com.runningstars.db.task;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.gdocument.gtracergps.launcher.domain.Localisation;
import org.gdocument.gtracergps.launcher.domain.Session;
import org.gdocument.gtracergps.launcher.log.Logger;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

//import com.addon.api.json.JsonWriter;
import com.cameleon.common.inotifier.INotifierMessage;
import com.runningstars.db.sqlite.datasource.DBLocationDataSource;
import com.runningstars.json.JSONParser;
import com.runningstars.tool.ToolMySQL;

/**
 * Background Async Task to Create new product
 * */
public class InsertLocation extends AsyncTask<String, String, String> {
	private static final String TAG = InsertLocation.class.getCanonicalName();

	private List<Localisation> listLocalisation;
	private Session session;

	private JSONParser jsonParser = new JSONParser();

	private String hostUrl;

	private DBLocationDataSource dbLocationDatasource;

	// private ProgressDialog pDialog;

	// url to create new product
	private static String url_insert_location = "/insert_location.php";

	// JSON Node names
	private static final String TAG_SUCCESS = "success";
	private static final String TAG_MESSAGE = "message";
	private static final String TAG_ID = "id";

	public InsertLocation(Context context, INotifierMessage notificationMessage, List<Localisation> listLocalisation, Session session, String hostUrl) {
		this.listLocalisation = listLocalisation;
		this.session = session;
		this.hostUrl = hostUrl;

        dbLocationDatasource = new DBLocationDataSource(context, notificationMessage);
	}

	/**
	 * Before starting background thread Show Progress Dialog
	 * */
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		// pDialog = new ProgressDialog(context);
		// pDialog.setMessage("Creating Product..");
		// pDialog.setIndeterminate(false);
		// pDialog.setCancelable(true);
		// pDialog.show();
	}

	/**
	 * Creating product
	 * */
	protected String doInBackground(String... args) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
	        dbLocationDatasource.open();

//	        writeJsonStream(baos, listLocalisation, session);
			String data = baos.toString("UTF-8");
			logMe(data);

			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("data", data));

			// getting JSON Object
			// Note that create product url accepts POST method
			JSONObject json = jsonParser.makeHttpRequest(this.hostUrl + url_insert_location, "POST", params);

			// check log cat for response
			Log.d("Create Response", (json == null ? "null" : json.toString()));

			if (json != null) {
				// check for success tag
				int success = json.getInt(TAG_SUCCESS);
				String message = json.getString(TAG_MESSAGE);
				long id_send = json.getLong(TAG_ID);

				if (success == 1) {
					Localisation localisation = listLocalisation.get(0);
					localisation.setIdSend(id_send);
			    	dbLocationDatasource.updateTimeSendBySession(session, listLocalisation);
				}
				logMe(success + " " + message);
			}
		} catch (JSONException e) {
			logMe(e);
			e.printStackTrace();
		} catch (IOException e) {
			logMe(e);
			e.printStackTrace();
		} finally {
			try {
				baos.close();
			} catch (IOException e) {
				logMe(e);
				e.printStackTrace();
			}
	        dbLocationDatasource.close();
		}

		return null;
	}

	private static String convertToUTF8(String s) {
		String out = null;
		try {
			if (s != null) {
				// TODO Replace Quot caracter to UTF-8 code
				s = s.replaceAll("'", " ");
				out = new String(s.getBytes("UTF-8"));
			}
		} catch (java.io.UnsupportedEncodingException e) {
			return null;
		}
		return out;
	}

	/**
	 * After completing background task Dismiss the progress dialog
	 * **/
	protected void onPostExecute(String file_url) {
		// dismiss the dialog once done
		// pDialog.dismiss();
	}

//	private void writeJsonStream(OutputStream out, List<Localisation> localisations, Session session) throws IOException {
//		JsonWriter writer = new JsonWriter(new OutputStreamWriter(out, "UTF-8"));
//		writer.setIndent("  ");
//		writeLocalisationArray(writer, localisations, session);
//		writer.close();
//	}
//
//	private void writeLocalisationArray(JsonWriter writer, List<Localisation> localisations, Session session) throws IOException {
//		writer.beginObject();
//		writer.name("location");
//		writer.beginArray();
//		for (Localisation localisation : localisations) {
//			writeLocalisation(writer, localisation, session);
//		}
//		writer.endArray();
//		writer.endObject();
//	}
//
//	private void writeLocalisation(JsonWriter writer, Localisation localisation, Session session) throws IOException {
//		if (localisation != null) {
//			writer.beginObject();
//			writer.name("ID_SQLITE").value(localisation.getId());
//			writer.name("ID_SESSION").value(session.getIdSend());
//			writer.name("ID_SESSION_SQLITE").value(session.getId());// KO
//			writer.name("SPEED").value(localisation.getSpeed());
//			writer.name("LONGITUDE").value(localisation.getLongitude());
//			writer.name("LATITUDE").value(localisation.getLatitude());
//			writer.name("ALTITUDE").value(localisation.getAltitude());
//			writer.name("ACCURANCY").value(localisation.getAccuracy());
//			writer.name("TIME").value(ToolMySQL.toFormatedDate(localisation.getTime()));
//			writer.name("ADDRESSLINE").value(convertToUTF8(localisation.getAddressLine()));
//			writer.name("POSTALCODE").value(convertToUTF8(localisation.getPostalCode()));
//			writer.name("LOCALITY").value(convertToUTF8(localisation.getLocality()));
//			writer.name("CAL_DISTANCE").value(localisation.getCalculateDistance());
//			writer.name("CAL_DISTANCE_CUMUL").value(localisation.getCalculateDistanceCumul());
//			writer.name("CAL_ELAPSED_TIME").value(localisation.getCalculateElapsedTime());
//			writer.name("CAL_ELAPSED_TIME_CUMUL").value(localisation.getCalculateElapsedTimeCumul());
//			writer.endObject();
//		}
//	}

	/**
	 * Log methodes
	 * 
	 * @param ex
	 */
	private void logMe(Exception ex) {
		logMe(ex.getMessage());
	}

	/**
	 * Log methodes
	 * 
	 * @param msg
	 */
	private void logMe(String msg) {
		Logger.logMe(TAG, msg);
	}
}
