package phs.bitcamp.amisafe;

import java.util.Date;
import com.google.android.gms.maps.model.LatLng;

public class Tweet {
	
	private static String tweet;
	private static Date timestamp;
	private static LatLng location;
	
	public Tweet(String msg, Date time, LatLng loc){
		tweet = msg;
		timestamp = time;
		location = loc;
	}
	
	public String getTweet(){
		return tweet;
	}
	
	public Date getTime(){
		return timestamp;
	}
	
	public LatLng getLocation(){
		return location;
	}
	
	
}
