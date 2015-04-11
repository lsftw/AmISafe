package phs.bitcamp.amisafe;

import java.io.IOException;

import phs.bitcamp.amisafe.data.CrimeIncidents;
import android.app.Activity;
import android.content.Intent;
import android.database.SQLException;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// CrimeIncidents.readCrimeIncidents(this);
		Log.i("BETTERTAG", "in db load");
		loadDatabase();
		
		setupGui();



	}

	private void setupGui() {
		Button viewRunsButton = (Button) findViewById(R.id.nextScreenButton);
		viewRunsButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this, MapActivity.class);
				startActivity(intent);
			}
		});
	}

	private void loadDatabase() {
		Log.i("testWOOGAOOGAtest", "in db load");
		DatabaseHelper myDbHelper = new DatabaseHelper(this);
		
		Log.i("testWOOGAOOGAtest", "in db load");
		try {

			myDbHelper.createDataBase();
			
			Log.i("testWOOGAOOGAtest", "creating db");
		} catch (IOException ioe) {
			
			Log.i("testWOOGAOOGAtest", "error in creating");
			throw new Error("Unable to create database");

		}

		try {

			myDbHelper.openDataBase();

		} catch (SQLException sqle) {

			throw sqle;

		}

		myDbHelper.close();

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
}
