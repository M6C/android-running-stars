package com.runningstars.db.task;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.gdocument.gtracergps.launcher.domain.Session;
import org.gdocument.gtracergps.launcher.log.Logger;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

//import com.addon.api.json.JsonWriter;
import com.cameleon.common.inotifier.INotifierMessage;
import com.runningstars.db.sqlite.datasource.DBSessionDataSource;
import com.runningstars.json.JSONParser;
import com.runningstars.tool.ToolMySQL;

/**
 * Background Async Task to Create new product
 * */
public class InsertSession extends AsyncTask<String, String, String> {
	private static final String TAG = InsertSession.class.getCanonicalName();

	private DBSessionDataSource dbSessionDatasource;
	private List<Session> listSession;

	private JSONParser jsonParser = new JSONParser();

	private String hostUrl;

//	private ProgressDialog pDialog;

	// url to create new product
	private static String url_insert_location = "/insert_session.php";

	// JSON Node names
	private static final String TAG_SUCCESS = "success";
	private static final String TAG_MESSAGE = "message";
	private static final String TAG_ID = "id";

	public InsertSession(Context context, INotifierMessage notificationMessage, List<Session> listSession, String hostUrl) {
		this.listSession = listSession;
		this.hostUrl = hostUrl;

        dbSessionDatasource = new DBSessionDataSource(context, notificationMessage);
	}

	/**
	 * Before starting background thread Show Progress Dialog
	 * */
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
//		pDialog = new ProgressDialog(context);
//		pDialog.setMessage("Creating Product..");
//		pDialog.setIndeterminate(false);
//		pDialog.setCancelable(true);
//		pDialog.show();
	}

	/**
	 * Creating product
	 * */
	protected String doInBackground(String... args) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
	        dbSessionDatasource.open();

//	        writeJsonStream(baos, listSession);
			String data = baos.toString();
			logMe(data);

			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("data", data));

			// getting JSON Object
			// Note that create product url accepts POST method
			JSONObject json = jsonParser.makeHttpRequest(this.hostUrl + url_insert_location, "POST", params);

			// check log cat for response
			Log.d("Create Response", (json==null ? "null" : json.toString()));

			if (json!=null) {
				// check for success tag
				int success = json.getInt(TAG_SUCCESS);
				String message = json.getString(TAG_MESSAGE);
				long id_send = json.getLong(TAG_ID);
	
				if (success==1) {
					Session session = listSession.get(0);
					session.setIdSend(id_send);
					session.setTimeSend(new Date());
//	            	business.dbSessionUpdateSend(session);
					// Update in database
					dbSessionDatasource.updateSend(session);
				}
				logMe(success + " " + message);
			}
		} catch (JSONException e) {
			logMe(e);
			e.printStackTrace();
//		} catch (IOException e) {
//			logMe(e);
			e.printStackTrace();
		} finally {
			try {
				baos.close();
			} catch (IOException e) {
				logMe(e);
				e.printStackTrace();
			}
	        dbSessionDatasource.close();
		}

		return null;
	}

	/**
	 * After completing background task Dismiss the progress dialog
	 * **/
	protected void onPostExecute(String file_url) {
		// dismiss the dialog once done
//		pDialog.dismiss();
	}
//
//	private void writeJsonStream(OutputStream out, List<Session> listSession) throws IOException {
//		JsonWriter writer = new JsonWriter(new OutputStreamWriter(out, "UTF-8"));
//		writer.setIndent("  ");
//		writeSessionArray(writer, listSession);
//		writer.close();
//	}
//
//	private void writeSessionArray(JsonWriter writer, List<Session> listSession) throws IOException {
//		writer.beginObject();
//		writer.name("session");
//		writer.beginArray();
//		for (Session session : listSession) {
//			writeSession(writer, session);
//		}
//		writer.endArray();
//		writer.endObject();
//	}
//
//	private void writeSession(JsonWriter writer, Session session) throws IOException {
//		if (session!=null) {
//			writer.beginObject();
//			writer.name("ID_SQLITE").value(session.getId());
//			writer.name("ID_MOBILE").value(session.getId());//KO
//			writer.name("ID_LOCATION_START").value(session.getStart().getId());
//			writer.name("ID_LOCATION_END").value(session.getEnd().getId());
//			writer.name("TIME_START").value(session.getTimeStart()==null ? null : ToolMySQL.toFormatedDate(session.getTimeStart()));
//			writer.name("TIME_STOP").value(session.getTimeStop()==null ? null : ToolMySQL.toFormatedDate(session.getTimeStop()));
//			writer.endObject();
//		}
//	}

    /**
     * Log methodes
     * @param ex
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
