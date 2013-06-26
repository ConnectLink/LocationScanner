package com.connectlinkcorp.app.locationscanner;

import java.util.ArrayList;
import java.util.HashMap;    
import java.util.List;    
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;    
import android.content.Intent;     
import android.content.IntentFilter;    
import android.net.wifi.ScanResult;    
import android.net.wifi.WifiConfiguration;   
import android.net.wifi.WifiManager;    
import android.os.Bundle;    
import android.util.Log;   
import android.view.View;    
import android.view.View.OnClickListener;    
import android.widget.AdapterView;    
import android.widget.Button;    
import android.widget.ListView;    
import android.widget.SimpleAdapter;    
import android.widget.TextView;    
import android.widget.Toast;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

/**
 * @author amuramatsu
 * should look into https://gist.github.com/granoeste/1026558
 * to detect signal strength see the following link
 * http://stackoverflow.com/questions/3437694/how-to-get-the-connection-strength-of-wifi-access-points
 */
public class MainActivity extends Activity implements OnClickListener{
	
    WifiManager wifi;       
    ListView lv;
    TextView textStatus;
    Button buttonScan;
    int size = 0;
    List<ScanResult> results;

    String ITEM_KEY = "key";
    ArrayList<HashMap<String, String>> arraylist = new ArrayList<HashMap<String, String>>();
    SimpleAdapter adapter;

    /* Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textStatus = (TextView) findViewById(R.id.textStatus);
        buttonScan = (Button) findViewById(R.id.buttonScan);
        buttonScan.setOnClickListener(this);
        lv = (ListView)findViewById(R.id.list);

        wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        if (wifi.isWifiEnabled() == false)
        {
            Toast.makeText(getApplicationContext(), "wifi is disabled..making it enabled", Toast.LENGTH_LONG).show();
            wifi.setWifiEnabled(true);
        }   
        this.adapter = new SimpleAdapter(MainActivity.this, arraylist, R.layout.row, new String[] { ITEM_KEY }, new int[] { R.id.list_value });
        lv.setAdapter(this.adapter);

        registerReceiver(new BroadcastReceiver()
        {
            @Override
            public void onReceive(Context c, Intent intent) 
            {
               results = wifi.getScanResults();
               size = results.size();
            }
        }, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));                    
    }

    public void onClick(View view) 
    {
        arraylist.clear();          
        wifi.startScan();

        Toast.makeText(this, "Scanning...." + size, Toast.LENGTH_SHORT).show();
        try 
        {
            size = size - 1;
            while (size >= 0) 
            {   
                HashMap<String, String> item = new HashMap<String, String>();                       
//                public String	BSSID	The address of the access point.
//                public String	SSID	The network name.
//                public String	capabilities	Describes the authentication, key management, and encryption schemes supported by the access point.
//                public int	frequency	The frequency in MHz of the channel over which the client is communicating with the access point.
//                public int	level	The detected signal level in dBm.
//                public long	timestamp	Time Synchronization Function (tsf) timestamp in microseconds when this result was last seen.
                item.put(ITEM_KEY, results.get(size).SSID + "  "  + " " + results.get(size).BSSID + " " + results.get(size).level + results.get(size).capabilities);

                arraylist.add(item);
                size--;
                adapter.notifyDataSetChanged();                 
            } 
        }
        catch (Exception e)
        { }         
    }    	

}
