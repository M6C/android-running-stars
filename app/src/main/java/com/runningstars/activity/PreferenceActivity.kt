package com.runningstars.activity

import android.content.SharedPreferences.OnSharedPreferenceChangeListener
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.runningstars.ApplicationData
import com.runningstars.R
import com.runningstars.listener.ontouch.ParameterOnTouchListener
import com.runningstars.listener.preference.ApplicationDataPreferenceListener
import org.gdocument.gtracergps.GpsConstant

class PreferenceActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)
        if (savedInstanceState == null) {
            supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.settings, SettingsFragment())
                    .commit()
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    class SettingsFragment : PreferenceFragmentCompat(), IPreference {

        private var onTouchListener: ParameterOnTouchListener? = null
        private var spChanged: OnSharedPreferenceChangeListener? = null
        private lateinit var performLog: Preference
        private lateinit var performLogSysOut: Preference
        private lateinit var performLogSd: Preference
        private lateinit var screenshootQuality: ListPreference
        private lateinit var mapZoom: ListPreference
        private lateinit var mapScale: ListPreference
        private lateinit var mapBearing: ListPreference
        private lateinit var mapServerUrl: ListPreference
        private lateinit var methodeCalculDistance: ListPreference

        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.application_data_preference, rootKey)

//		FactoryStyle.getInstance().centerTitle(this);

            //http://stackoverflow.com/questions/9505901/android-using-switch-preference-pre-api-level-14

//		FactoryStyle.getInstance().centerTitle(this);

            //http://stackoverflow.com/questions/9505901/android-using-switch-preference-pre-api-level-14
            performLog = preferenceScreen.findPreference("perform_log")!!
            performLogSysOut = preferenceScreen.findPreference("perform_log_sysout")!!
            performLogSd = preferenceScreen.findPreference("perform_log_sd")!!
            screenshootQuality = preferenceScreen.findPreference("screenshoot_quality")!!
            mapZoom = preferenceScreen.findPreference("map_zoom")!!
            mapScale = preferenceScreen.findPreference("map_scale")!!
            mapBearing = preferenceScreen.findPreference("map_bearing")!!
            mapServerUrl = preferenceScreen.findPreference("mysql_server_url")!!
            methodeCalculDistance = preferenceScreen.findPreference("methode_calcul_distance")!!

            initializeSummary(null)

            enableOnTouchListenerMain()

            spChanged = ApplicationDataPreferenceListener(context, this)
        }

        /**
         * Setup the initial values
         */
        override fun initializeSummary(key: String?) {
//		SharedPreferences sharedPreferences = getPreferenceScreen().getSharedPreferences();
//            if (key == null || "perform_log" == key) //			performLog.setSummary(sharedPreferences.getBoolean("perform_log", false) ? "Disabled Log" : "Enabled Log");
                performLog.setSummary(if (GpsConstant.LOG_WRITE) "Enabled Log" else "Disabled Log")
            if (key == null || "perform_log_sysout" == key) //			performLogSysOut.setSummary(sharedPreferences.getBoolean("perform_log_sysout", false) ? "Disabled Log SysOut" : "Enabled Log SysOut");
                performLogSysOut.setSummary(if (GpsConstant.LOG_WRITE_SYSOUT) "Enabled Log SysOut" else "Disabled Log SysOut")
            if (key == null || "perform_log_sd" == key) //			performLogSd.setSummary(sharedPreferences.getBoolean("perform_log_sd", false) ? "Disabled Log Sd" : "Enabled Log Sd");
                performLogSd.setSummary(if (GpsConstant.LOG_WRITE_SD) "Enabled Log Sd" else "Disabled Log Sd")
            if (key == null || "screenshoot_quality" == key) //			screenshootQuality.setSummary("Define quality of screenshoot map " + sharedPreferences.getString("screenshoot_quality", ""));
                screenshootQuality.setSummary("Define quality of screenshoot map : " + ApplicationData.getInstance(context).mapScreenShootQuality)
            if (key == null || "map_zoom" == key) //			mapZoom.setSummary("Define the map zoom " + sharedPreferences.getString("map_zoom", ""));
                mapZoom.setSummary("Define the map zoom : " + ApplicationData.getInstance(context).gpsLocationMapZoom)
            if (key == null || "map_scale" == key) //			mapScale.setSummary("Define the map scale " + sharedPreferences.getString("map_scale", ""));
                mapScale.setSummary("Define the map scale : " + ApplicationData.getInstance(context).mapScaleInMeter)
            if (key == null || "map_bearing" == key) //			mapBearing.setSummary("Define the map bearing " + sharedPreferences.getString("map_bearing", ""));
                mapBearing.setSummary("Define the map bearing : " + ApplicationData.getInstance(context).mapBearingScale)
            if (key == null || "mysql_server_url" == key) //			mapServerUrl.setSummary("Define MySql Server Url " + sharedPreferences.getString("mysql_server_url", ""));
                mapServerUrl.setSummary("Define MySql Server Url : " + ApplicationData.getInstance(context).mysqlServerUrl)
            if (key == null || "methode_calcul_distance" == key) //			mapServerUrl.setSummary("Define MySql Server Url " + sharedPreferences.getString("mysql_server_url", ""));
                methodeCalculDistance.setSummary("Define distance methode calculate : " + ApplicationData.getInstance(context).distanceMethodeCalcul)
        }

        private fun enableOnTouchListenerMain() {
            if (onTouchListener == null) onTouchListener = ParameterOnTouchListener(activity as AppCompatActivity?)

            // Set the touch listener for the main view to be our custom gesture
            // listener
            this.listView.setOnTouchListener(onTouchListener)
        }
    }
}