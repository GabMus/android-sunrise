package com.gabmus.sunrise;

import android.app.Activity;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new ForecastFragment())
                    .commit();
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(this , SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }

        if (id == R.id.action_showonmap) {

            Geocoder geocoder = new Geocoder(this);
            String zip= PreferenceManager.getDefaultSharedPreferences(this).getString(getString(R.string.pref_location_key),getString(R.string.pref_location_default));
            try {
                List<Address> addresses = geocoder.getFromLocationName(zip, 1);
                if (addresses != null && !addresses.isEmpty()) {
                    Address address = addresses.get(0);
                    Uri geotag = Uri.parse(String.format("geo:%f,%f", address.getLatitude(), address.getLongitude()));
                    Intent mapIntent = new Intent(Intent.ACTION_VIEW);
                    mapIntent.setData(geotag);
                    startActivity(mapIntent);
                }
                else {
                    // Display appropriate message when Geocoder services are not available
                    Toast.makeText(this, "Position not valid", Toast.LENGTH_SHORT).show();
                }
            } catch (IOException e) {
                // handle exception
            }

            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
