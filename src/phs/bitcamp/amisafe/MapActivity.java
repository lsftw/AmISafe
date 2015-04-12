package phs.bitcamp.amisafe;

import java.util.ArrayList;
import java.util.List;

import phs.bitcamp.amisafe.data.Crime;
import phs.bitcamp.amisafe.data.CrimeIncidents;
import android.app.ActionBar;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.TileOverlay;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.maps.android.heatmaps.Gradient;
import com.google.maps.android.heatmaps.HeatmapTileProvider;

public class MapActivity extends Activity {//implements TweetListener {
	private GoogleMap map;
	//	private TwitterConnection tc;

	private boolean crimesDisplayed = false;
	private ColorDrawable indicator;
	private final int SEVERITY_MEASURE = 450;
	private final int HEATMAP_RADIUS = 50; // must be between 10-50

	private int mInterval = 5000; // 5 seconds by default, can be changed later
	private Handler mHandler;
	private int tweetsDisplayed = 0;
	private HeatmapTileProvider mProvider;
	private TileOverlay mOverlay;

	boolean fromSelected, toSelected, fromLocSet, toLocSet;
	LatLng fromLocation, toLocation;
	Button fromLocationButton;
	Button toLocationButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);

		CrimeIncidents.loadDatabase(this);
		setupGui();
		// Get map fragment

		TwitterConnection.start();
		//		TwitterConnection.addTweetListener(this);
		//		tc = new TwitterConnection(this);
		repeatedlyGetTweets();
	}

	/*
	private void addHeatMap() {
		List<LatLng> list = null;
		
		try {
			//list = crime data?
		} catch (JSONException e) {
	        Toast.makeText(this, "Problem reading list of locations.", Toast.LENGTH_LONG).show();
	    }
		
		

	}
*/
	public void updateButtons() {
		if (fromLocSet && toLocSet) {
			fromSelected = false;
			toSelected = false;
			fromLocSet = false;
			toLocSet = false;
			pathfind();
		}

		if (fromSelected) {
			fromLocationButton.setEnabled(false);
			toLocationButton.setEnabled(true);			
		} else if (toSelected) {
			fromLocationButton.setEnabled(true);
			toLocationButton.setEnabled(false);
		} else {
			fromLocationButton.setEnabled(true);
			toLocationButton.setEnabled(true);
		}

		if (fromLocSet) {
			fromLocationButton.setText("From Set");
		} 
		if (toLocSet) {
			toLocationButton.setText("To Set");
		}

	}

	public void pathfind() {

	}
	public void heatmap(List<Crime> crimes) {
		// convert to coords list
		List<LatLng> coords = new ArrayList<LatLng>(crimes.size());
		for (Crime crime : crimes) {
			double[] latlng = crime.getCoords();
			coords.add(new LatLng(latlng[0], latlng[1]));
		}
		
		// Create the gradient that isn't green/red
		int[] colors = {
			    Color.rgb(255, 255, 0), // yellow
			    Color.rgb(255, 0, 0)    // red
			};

			float[] startPoints = {
			    0.2f, 1f
			};

			Gradient gradient = new Gradient(colors, startPoints);
		
		// Create a heat map tile provider, passing it the latlngs of the police stations.
	    mProvider = new HeatmapTileProvider.Builder()
	        .data(coords)
	        .gradient(gradient)
	        .radius(HEATMAP_RADIUS)
	        .build();
	    // Add a tile overlay to the map, using the heat map tile provider.
	    mOverlay = map.addTileOverlay(new TileOverlayOptions().tileProvider(mProvider));

	}

	public void setupGui() {

		/*fromLocationButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				fromSelected = true;
				toSelected = false;
				updateButtons();
			}
		});

		toLocationButton.setOnClickListener(new OnClickListener() {
			public void onClick (View v) {
				fromSelected = false;
				toSelected = true;
				updateButtons();
			}
		});*/

		MapFragment mapFragment = (MapFragment) getFragmentManager()
				.findFragmentById(R.id.preview_map);
		map = mapFragment.getMap();
		map.setMyLocationEnabled(true);
		indicator = new ColorDrawable(Color.BLACK);
		ActionBar barbar = getActionBar();
		barbar.setBackgroundDrawable(indicator);
		
		
		map.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
			@Override
			public void onMyLocationChange(Location loc) { // find crimes once - after user location found
				if (!crimesDisplayed) {
					crimesDisplayed = true;
					Log.i("findcrime", "about to find some crime");
					zoomToLocation(loc.getLatitude(), loc.getLongitude());
					findCrime(loc.getLatitude(), loc.getLongitude());
				}
			}
		});
		map.setOnMapClickListener(new OnMapClickListener() {
			@Override
			public void onMapClick(LatLng point) {
				// TODO Auto-generated method stub
				//If they click the from button then the point on the map they click
				//will be stored as the location for the from location. Similar setup with
				// the to location.
				if(fromSelected) {
					fromLocation = point;
					fromLocSet = true;
					//updateButtons();
				} else if (toSelected) {
					toLocation = point;
					toLocSet = true;
					//updateButtons();
				}

			}
		});

		/*Button viewRunsButton = (Button) findViewById(R.id.customroute1);
		viewRunsButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//Intent intent = new Intent(MapActivity.this, MainActivity.class);
				//startActivity(intent);


				//If they push custom route, then show the "From" and "To" buttons.
				//They can click "From" and then click on a location to set initial location.
				//Then click "To" for the same thing. This way we'll have our two points
				//that they want to travel in between and we can pull the LatLng from our map.
				//Otherwise it's too complicated to run specific addresses via map 
				//(as opposed to just LatLng). 
				//View customrouteButton = findViewById(R.id.customroute1);
				//customrouteButton.setVisibility(View.INVISIBLE);

			}
		});*/
	}

	private void findCrime(double lat, double lng) {
		List<Crime> crimes_far = CrimeIncidents.getNearbyCrimes(lat, lng, false);
		List<Crime> crimes_near = CrimeIncidents.getNearbyCrimes(lat, lng, true);
		Toast.makeText(this,"Found " + crimes_near.size() + " crimes by (" + lat + "," + lng + ").", 
                Toast.LENGTH_LONG).show();
		/*for (Crime crime : crimes) {
			map.addMarker(new MarkerOptions().position(new LatLng(crime.getCoords()[0], crime.getCoords()[1]))
					.title("Crime")
					.snippet(crime.getOffense()));
		}*/
		
		//generate color schema here
		changeColorIndicator(crimes_near.size());
		heatmap(crimes_far);
		
	}
	
	public void changeColorIndicator(int severity){
		// red (204, 51, 0)
		// green (102, 255, 153)
		int red, green, blue;

		float p = ((float) severity) / SEVERITY_MEASURE;
		float q = 1 - p;
		if( severity < (SEVERITY_MEASURE / 2)){
			p *= 2;
			q = 1 - p;
			//use yellow and green
			red = (int) (255 * p + 0 * q);
			green = (int) (255 * p + 255 * q);
			blue = (int) (0 * p + 0 * q);
		}else{
			//use yellow and red
			red = (int) (255 * p + 255 * q);
			green = (int) (0 * p + 150 * q);
			blue = (int) (0 * p + 0 * q);
		}
		indicator.setColor(Color.rgb(red, green, blue));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		for (int i = 0; i < menu.size(); i++){
			menu.getItem(i).setVisible(false);
		}
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	// Zoom to a given point on the map.
	private void zoomToLocation(double lat, double lon) {
		if (map != null) {
			LatLng coordinates = new LatLng(lat, lon);
			map.animateCamera(CameraUpdateFactory.newLatLngZoom(coordinates, 14.3f));
		}
	}
	
	private void repeatedlyGetTweets() {
		mHandler = new Handler();

		final Runnable mStatusChecker = new Runnable() {
			@Override 
			public void run() {
				updateTweets();
				mHandler.postDelayed(this, mInterval);
			}
		};
		mStatusChecker.run(); 
	}
	
	private void updateTweets() { // get tweet list and display as markers
		List<Tweet> tweets = TwitterConnection.getTweets();
		// once a marker is added, don't add it again. do this by keeping track of the index of the latest displayed tweet
		for (; tweetsDisplayed < tweets.size(); tweetsDisplayed++) {
			Tweet curTweet = tweets.get(tweetsDisplayed);
			map.addMarker(new MarkerOptions().position(curTweet.getLocation())
					.title(curTweet.getTweet())
					.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_notification)));
		}
	}
	
	

	//	@Override
	//	public void newTweetReceived(Tweet newTweet) {
	//		String tweet = newTweet.getTweet();
	//		//System.out.println("TWEET " + tweet);
	//		Log.i("twitter", "got the tweet");
	//		//Date date = newTweet.getTime();
	//		LatLng location = newTweet.getLocation();
	//		//System.out.println("LOCATION " + location);
	//		Log.i("twitter", "got the location");
	//		Log.i("twitter", "about to put '" + tweet + "' at + '" + location + "'.");
	//		Log.i("twitter", "about to add marker");
	//		System.out.println(this);
	//		map.addMarker(new MarkerOptions()
	//	        .position(new LatLng(38.9875, -76.9400))
	//	        .title("hello world"));
	//		Log.i("twitter", "completed adding marker");
	//
	//	}
}
