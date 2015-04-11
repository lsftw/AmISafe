package phs.bitcamp.amisafe;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;

public class MapActivity extends Activity {
	private GoogleMap map;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);
		
		setupGui();
		// Get map fragment
		MapFragment mapFragment = (MapFragment) getFragmentManager()
				.findFragmentById(R.id.preview_map);
		map = mapFragment.getMap();
		map.setMyLocationEnabled(true);
	}

	public void setupGui() {
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
				customrouteButton.setVisibility(View.GONE);
				fromButton.setVisibility(View.VISIBLE);
				toButton.setVisibility(View.VISIBLE);
				
			}
		});
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
