package com.runningstars.tool;

import java.text.NumberFormat;

import org.gdocument.gtracergps.launcher.domain.Localisation;
import org.gdocument.gtracergps.launcher.domain.Session;
import org.gdocument.gtracergps.launcher.util.GpsUtil;
import org.gdocument.gtracergps.launcher.util.NumberUtil;

import com.cameleon.common.tool.ToolDatetime;
import com.runningstars.ApplicationData.METHODE_CALCUL_DISTANCE;


public class ToolCalculate {

	private static final NumberFormat nf = NumberFormat.getNumberInstance();

	public static float getDiffTimeToKmH(Session session) {
		return getDiffTimeToKmH(session.getStart(), session.getEnd(), session.getCalculateDistance());
	}

	public static float getDiffTimeToKmH(METHODE_CALCUL_DISTANCE methode, Localisation start, Localisation end) {
		if (start != null && end != null) {
			double distance = 0d;
			if (end.getCalculateDistanceCumul()<=0) {
				distance = getDistance(
						methode,
						start.getLatitude(), start.getLongitude(), end.getLatitude(), end.getLongitude(),
						start.getAltitude(), end.getAltitude()
					);
			}
			else {
				distance = end.getCalculateDistanceCumul() - start.getCalculateDistanceCumul();
			}
			return getDiffTimeToKmH(start.getTime(), end.getTime(), distance);
		}
		else
			return 0;
	}

	public static float getDiffTimeToKmH(Localisation start, Localisation end, double distance) {
		if (start != null && end != null)
			return getDiffTimeToKmH(start.getTime(), end.getTime(), distance);
		else
			return 0;
	}

	public static float getDiffTimeToKmH(long timeStart, long timeStop, double distanceKm) {
		long elapsedTime = timeStop-timeStart;
		return getKmH(elapsedTime, distanceKm);
	}

	public static float getKmH(long elapsedTime, double distanceKm) {
		if (elapsedTime<=0)
			return 0f;
		float nbMinutes = elapsedTime / 60000f;
		float nbMinutesReste = nbMinutes - ((float)((int)nbMinutes));
		if (nbMinutesReste>0.6d) {
			nbMinutes += 0.4d;
		}
		return ((float)(distanceKm * 60f)) / nbMinutes;
	}

	public static double getDistance(METHODE_CALCUL_DISTANCE methode, double lat1, double lon1, double lat2, double lon2, double alt1, double alt2) {
		switch (methode) {
			case METHODE_3:
				return GpsUtil.calDistanceKM3(lat1, lon1, lat2, lon2, alt1, alt2);
			case METHODE_2:
				return GpsUtil.calDistanceKM2(lat1, lon1, lat2, lon2, alt1, alt2);
			default:
				return GpsUtil.calDistanceKM(lat1, lon1, lat2, lon2, alt1, alt2);
		}
	}

//	public static double getDistance(double lat1, double lon1, double lat2, double lon2) {
//		return GpsUtil.calDistanceKM(lat1, lon1, lat2, lon2);
//	}

	public static String formatKmH(METHODE_CALCUL_DISTANCE methode, Localisation start, Localisation end) {
		return formatSpeed(getDiffTimeToKmH(methode, start, end));
	}

	public static String formatSpeedKmH(Localisation localisation) {
		return formatSpeed(localisation.getSpeedKmHRounded());
	}

	public static String formatCalculateSpeedAverageKmH(Localisation localisation) {
		return formatSpeed(localisation.getCalculateSpeedAverageKmHRounded());
	}

	public static String formatSpeedKmH(Session session) {
		return formatSpeed(ToolCalculate.getDiffTimeToKmH(session));
	}

	public static String formatSpeed(float speed) {
		return (speed < 1f) ? nf.format(NumberUtil.formatDouble(speed * 1000)) + " M/H" : nf.format(NumberUtil.formatDouble(speed)) + " Km/H";
	}

	public static String formatCalculateDistanceKm(Localisation localisation) {
		double distanceKm = localisation.getCalculateDistance();
		return formatDistanceKm(NumberUtil.formatDouble(distanceKm));
	}

	public static String formatCalculateDistanceCumulKm(Localisation localisation) {
		double distanceKm = localisation.getCalculateDistanceCumul();
		return formatDistanceKm(NumberUtil.formatDouble(distanceKm));
	}

	public static String formatDistanceKm(Session session) {
		double distanceKm = session.getCalculateDistance();
		return formatDistanceKm(NumberUtil.formatDouble(distanceKm));
	}

	public static String formatDistanceKm(double distanceKm) {
		return nf.format(NumberUtil.formatDouble(distanceKm)) + " Km";
	}

	public static String formatDistance(Session session) {
		double distance = session.getCalculateDistance();
		String szDistance = (distance<1) ? (nf.format(NumberUtil.formatDouble(distance * 1000)) + " M") : (nf.format(NumberUtil.formatDouble(distance)) + " Km");
		return szDistance;
	}

	public static String formatElapsedTime(Session session) {
		return formatElapsedTime(session.getCalculateElapsedTime());
	}

	public static String formatElapsedTime(Localisation previous, Localisation localisation) {
		long elapsedTime = localisation.getTime()-previous.getTime();
		return formatElapsedTime(elapsedTime);
	}

	public static String formatCalculateElapsedTime(Localisation localisation) {
		return formatElapsedTime(localisation.getCalculateElapsedTime());
	}

	public static String formatElapsedTime(long elapsedTime) {
		return ToolDatetime.toTimeDefault(elapsedTime);
	}
}
