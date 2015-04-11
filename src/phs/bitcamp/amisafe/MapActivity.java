package phs.bitcamp.amisafe;

import java.util.List;

import phs.bitcamp.amisafe.data.Crime;
import phs.bitcamp.amisafe.data.CrimeIncidents;
import android.app.Activity;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapActivity extends Activity {
	private GoogleMap map;
	private boolean crimesDisplayed = false;

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

	}

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

	public void setupGui() {
		fromLocationButton = (Button) findViewById(R.id.FromLocation);
		toLocationButton = (Button) findViewById(R.id.ToLocation);

		fromLocationButton.setOnClickListener(new OnClickListener() {
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
		});

		MapFragment mapFragment = (MapFragment) getFragmentManager()
				.findFragmentById(R.id.preview_map);
		map = mapFragment.getMap();
		map.setMyLocationEnabled(true);
		map.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
			@Override
			public void onMyLocationChange(Location loc) { // find crimes once - after user location found
				if (!crimesDisplayed) {
					crimesDisplayed = true;
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
					updateButtons();
				} else if (toSelected) {
					toLocation = point;
					toLocSet = true;
					updateButtons();
				}

			}
		});

		Button viewRunsButton = (Button) findViewById(R.id.customroute1);
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
				View customrouteButton = findViewById(R.id.customroute1);
				View fromButton = findViewById(R.id.FromLocation);
				View toButton = findViewById(R.id.ToLocation);
				customrouteButton.setVisibility(View.INVISIBLE);
				fromButton.setVisibility(View.VISIBLE);
				toButton.setVisibility(View.VISIBLE);

			}
		});
	}

	private void findCrime(double lat, double lng) {
		List<Crime> crimes = CrimeIncidents.getNearbyCrimes(lat, lng);

		Log.i("findCrime", "Found " + crimes.size() + " crimes by (" + lat + "," + lng + ").");
		for (Crime crime : crimes) {
			Log.i("findCrime", "Crime: " + crime);
			map.addMarker(new MarkerOptions().position(new LatLng(crime.getCoords()[0], crime.getCoords()[1]))
					.title("Crime")
					.snippet(crime.getOffense()));
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
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
			map.animateCamera(CameraUpdateFactory.newLatLngZoom(coordinates, 17f));
		}
	}
}
