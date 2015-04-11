package phs.bitcamp.amisafe.data;

import java.io.InputStream;
import java.util.Scanner;

import phs.bitcamp.amisafe.R;
import android.content.Context;
import android.util.Log;

public class CrimeIncidents {
	private static final String TAG = CrimeIncidents.class.getSimpleName();
	public static void readCrimeIncidents(Context context) {
	    InputStream inputStream = context.getResources().openRawResource(R.raw.crime_incidents_2014);
	    Scanner reader = new Scanner(inputStream);
	    while (reader.hasNextLine()) {
	    	String curLine = reader.nextLine();
	    	//Log.i(TAG, curLine); // logging every line takes too long
	    }
	    reader.close();
	}
}
