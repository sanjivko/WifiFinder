package com.slowhand.sanjiv.wififinder;

import android.content.Context;
import android.location.LocationManager;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;

import java.util.Vector;

public class MainActivity extends AppCompatActivity {

    public WifiManager sysWifiMgr;
    private LocationManager sysLocnMgr;
    private LocationIHandler locnHandler;

    private ListView listViewScannedSSID;
    private ImageButton imgButtonRefresh;
    private WifiConnManager connMgr = null;


    public void refreshSSID() {
        Vector<SSIDListAdapterEntry> ssids;
        ssids = connMgr.scan();
        if (ssids != null) {
            SSIDListAdapterEntry ssidArr[] = new SSIDListAdapterEntry[ssids.size()];

            ssids.toArray(ssidArr);

            listViewScannedSSID = (ListView) findViewById(R.id.listViewScannedNetworks);
            SSIDListAdapter adap = new SSIDListAdapter(this, R.layout.ssid_list_row_layout, ssidArr);
            listViewScannedSSID.setAdapter(adap);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imgButtonRefresh = (ImageButton) findViewById(R.id.imageButtonRefresh);
        imgButtonRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshSSID();
            }
        });

        //Get system sifi service
        sysWifiMgr = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        connMgr = new WifiConnManager(sysWifiMgr);

        // Acquire a reference to the system Location Manager
        sysLocnMgr = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locnHandler = new LocationIHandler(this, sysLocnMgr);

        //Update the SSIDs and location
        refreshSSID();
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
