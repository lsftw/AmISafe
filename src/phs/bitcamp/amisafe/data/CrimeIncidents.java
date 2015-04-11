package phs.bitcamp.amisafe.data;

import java.io.IOException;
import java.util.ArrayList;

import phs.bitcamp.amisafe.DatabaseHelper;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class CrimeIncidents {
	public static SQLiteDatabase myDB;
	private static final double NEARBY_RANGE = 0.0005*0 +5; 
		
	public static Crime[] getNearbyCrimes(double lat, double lon){
		Crime[] crimes;
		ArrayList<Crime> crimeList = new ArrayList<Crime>();
		String selectQuery = "SELECT _id, tod, offense, lat, long FROM android_manifest WHERE " +
				"a.lat BETWEEN ? and ? AND a.long BETWEEN ? and ?";
		String[] ranges = { 
				"" + (lat - NEARBY_RANGE), "" + (lat + NEARBY_RANGE), //latitude range
				"" + (lon - NEARBY_RANGE), "" + (lon + NEARBY_RANGE)};
		
		if (myDB == null) {
			Log.i("NOOOOO", "null");
		}
		Cursor c = myDB.rawQuery(selectQuery, ranges);
		Crime temp;
		if (c.moveToFirst()) {
		    while (c.isAfterLast() == false) {
				temp = new Crime(
						c.getInt(0),				// ID
						c.getString(1),				// time of day
						c.getString(2),				// offense
						(double)c.getLong(3),		// latitude
						(double)c.getLong(4));		// longitude
			    crimeList.add(temp);
		        c.moveToNext();
		    }
		    
		}
		c.close();
		crimes = (Crime[]) crimeList.toArray();
		return crimes;
	}
	
	public static void loadDatabase(Context currentContext) {
		DatabaseHelper myDatabaseHelper = new DatabaseHelper(currentContext);
		try {
			myDatabaseHelper.createDataBase();
		} catch (IOException ioe) {
			throw new Error("Unable to create database");
		}
		try {
			myDatabaseHelper.openDataBase();
		} catch (SQLException sqle) {
			throw sqle;
		}
		myDB = myDatabaseHelper.getReadableDatabase();
//		myDatabaseHelper.close();


	}
	
}
