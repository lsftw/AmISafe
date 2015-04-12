package phs.bitcamp.amisafe;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import twitter4j.FilterQuery;
import twitter4j.GeoLocation;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.conf.ConfigurationBuilder;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;


public class TwitterConnection {

	private static String ConsumerKey = "KKUby7Msc3mcHJk1hmoYaXYgr";
	private static String ConsumerSecret = "c0XnDPhhdLF1n7uD4xIi3XiG9KrKjaYsqlTecmILRbTjA0UgSD";
	private static String AccessToken = "3157783491-JSwFuVEKaNV0vklJ48E5sXlLfBKvql0Bpfw1Mql";
	private static String AccessTokenSecret = "wz1vxKW0MI8DYCt7izefmWIhp7GZ7vFnIRL8DUmTNWf6c";
//	private static List<TweetListener> tweetListeners = Collections.synchronizedList(new ArrayList<TweetListener>());
	private static TwitterConnection instance;
	private static List<Tweet> tweets = new ArrayList<Tweet>();
	
	public static void start() {
		if (instance == null) {
			instance = new TwitterConnection();
		}
	}

	public TwitterConnection() {
		ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setDebugEnabled(true);
		cb.setOAuthConsumerKey(ConsumerKey);
		cb.setOAuthConsumerSecret(ConsumerSecret);
		cb.setOAuthAccessToken(AccessToken);
		cb.setOAuthAccessTokenSecret(AccessTokenSecret);

		TwitterStream twitterStream = new TwitterStreamFactory(cb.build()).getInstance();

		StatusListener listener = new StatusListener() {
			public void onException(Exception arg0) {
				// TODO Auto-generated method stub

			}
			public void onDeletionNotice(StatusDeletionNotice arg0) {
				// TODO Auto-generated method stub

			}
			public void onScrubGeo(long arg0, long arg1) {
				// TODO Auto-generated method stub

			}
			public void onStatus(Status status) {
				// USERNAME
			//	String username = status.getUser().getScreenName();
				// TWEET
				String content = status.getText();
				// TIME OF TWEET
				Date time = status.getCreatedAt();
				// LOCATION
				GeoLocation geolocation = status.getGeoLocation();
				double latitude = geolocation.getLatitude();
				double longitude = geolocation.getLongitude();
				LatLng loc = new LatLng(latitude, longitude);

				Tweet tweet = new Tweet(content, time, loc);
				Log.i("twitter", "new tweet received.");
				tweets.add(tweet);
//				for (TweetListener tweetListener : tweetListeners) {
//					tweetListener.newTweetReceived(tweet);
//				}

				//	            	User user = status.getUser();         
				//	                String profileLocation = user.getLocation();
				//	                System.out.println(profileLocation);
				//	                System.out.println(username);
				//	                System.out.println("STATUS: " + status);
				//	                System.out.println(content);
				//	                System.out.println("TIMESTAMP: " + status.getCreatedAt());
				//	                System.out.println("lat: " + latitude + "\nlong: " + longitude + "\n");

			}
			public void onTrackLimitationNotice(int arg0) {
				// TODO Auto-generated method stub

			}
			public void onStallWarning(StallWarning arg0) {
				// TODO Auto-generated method stub

			}
		};

		FilterQuery fq = new FilterQuery();

		String keywords[] = {"it'sasecretraspberry"};

		fq.track(keywords);
		twitterStream.addListener(listener);
		twitterStream.filter(fq);  

	}
//	public static void addTweetListener(TweetListener listener) {
//		TwitterConnection.start();
//		tweetListeners.add(listener);
//	}
	public static List<Tweet> getTweets() {
		return tweets;
	}
}
