package phs.bitcamp.amisafe.data;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import phs.bitcamp.amisafe.DatabaseHelper;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class CrimeIncidents {
	public static SQLiteDatabase myDB;
	private static final double FAR_RANGE = .03;
	private static final double NEARBY_RANGE = 0.002;
		
	public static List<Crime> getNearbyCrimes(double lat, double lon, boolean nearby){
		ArrayList<Crime> crimeList = new ArrayList<Crime>();
		String selectQuery = "SELECT _id, tod, offense, lat, long FROM android_manifest a WHERE " +
//				"a._id BETWEEN 2 and 3000";
				"a.lat BETWEEN ? and ? AND a.long BETWEEN ? and ?";
		double range_mod;
		if(nearby == true){
			range_mod = NEARBY_RANGE;
		}else{
			range_mod = FAR_RANGE;
		}
		String[] ranges = { 
				"" + (lat - range_mod), "" + (lat + range_mod), //latitude range
				"" + (lon - range_mod), "" + (lon + range_mod)};
		
		Cursor c = myDB.rawQuery(selectQuery, ranges);
		Crime temp;
		if (c.moveToFirst()) {
		    while (c.isAfterLast() == false) {
				temp = new Crime(
						c.getInt(0),				// ID
						c.getString(1),				// time of day
						c.getString(2),				// offense
						c.getDouble(3),		// latitude
						c.getDouble(4));		// longitude
			    crimeList.add(temp);
		        c.moveToNext();
		    }
		    
		}
		c.close();
		return crimeList;
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
