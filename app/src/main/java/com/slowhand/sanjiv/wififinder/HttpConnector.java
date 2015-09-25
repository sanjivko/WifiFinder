package com.slowhand.sanjiv.wififinder;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.location.Location;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Vector;


/**
 * Created by sanjiv on 19/9/15.
 */
public class HttpConnector {
    String URL_root = "http://wifinder.phpwebservice.com/api/";
    MainActivity ctx = null;
    private RequestQueue mRequestQueue;

    public HttpConnector(MainActivity act) {
        ctx = act;
        // Instantiate the cache
        Cache cache = new DiskBasedCache(ctx.getCacheDir(), 1024 * 1024); // 1MB cap

        // Set up the network to use HttpURLConnection as the HTTP client.
        Network network = new BasicNetwork(new HurlStack());

        mRequestQueue = new RequestQueue(cache, network);
        // Start the queue
        mRequestQueue.start();
    }

    public boolean showDialog(final SSIDListAdapterEntry entry) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
        builder.setMessage("Connect:" + entry.ssid_name).setTitle("Sure?");
        // Add the buttons
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                WifiConnManager connMgr = new WifiConnManager(ctx.sysWifiMgr);
                connMgr.connect(entry);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();

        return true;
    }


    Vector<SSIDListAdapterEntry> getSSID_by_location(Location loc) {
        final Vector<SSIDListAdapterEntry> serverSSIDs = new Vector<>();
        String url = URL_root + "getallssidbylatlong";

        //{"lat":"-33.8263832","lang":"151.0086468"}
        String jsonReq = "{\"lat\":\"" + loc.getLatitude() + "\",\"lang\":\"" + loc.getLongitude() + "\"}";

        //String jsonReq = "{\"lat\":\"-33.8263832\",\"lang\":\"151.0086468\"}";
        JSONObject reqJSON = null;
        try {
            reqJSON = new JSONObject(jsonReq);
        } catch (JSONException e) {
            Log.w("WiFinder", "Error in parsing the json string:" + e.getMessage());
            return null;
        }

        JsonObjectRequest jsObjRequest = new JsonObjectRequest(
                Request.Method.POST, url, reqJSON, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //mTxtDisplay.setText("Response: " + response.toString());
                Log.d("WiFinder", "JSON Response:" + response.toString());

                try {
                    if (response.getString("status").equals("pass")) {
                        JSONArray jsonArr = response.getJSONArray("msg");
                        for (int i = 0; i < jsonArr.length(); i++) {
                            SSIDListAdapterEntry entry = new SSIDListAdapterEntry();

                            entry.ssid_name = ((JSONObject) jsonArr.get(i)).getString("SSID");
                            entry.auth_method = ((JSONObject) jsonArr.get(i)).getString("authentication_method");
                            entry.cipher = ((JSONObject) jsonArr.get(i)).getString("cipher");
                            entry.pass_key = ((JSONObject) jsonArr.get(i)).getString("password");

                            serverSSIDs.add(entry);
                        }

                        final SSIDListAdapterEntry serverSsidArr[] = new SSIDListAdapterEntry[serverSSIDs.size()];
                        serverSSIDs.toArray(serverSsidArr);
                        final ListView listViewServerSSID = (ListView) ctx.findViewById(R.id.listView);
                        SSIDListAdapter adap = new SSIDListAdapter(ctx, R.layout.ssid_list_row_layout, serverSsidArr);
                        listViewServerSSID.setAdapter(adap);

                        listViewServerSSID.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Object o = listViewServerSSID.getItemAtPosition(position);
                                SSIDListAdapterEntry obj = (SSIDListAdapterEntry) o;//As you are using Default String Adapter
                                Toast.makeText(ctx, obj.ssid_name, Toast.LENGTH_SHORT).show();

                                //Show dialog
                                showDialog(obj);
                            }
                        });

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO Auto-generated method stub
                Log.d("WiFinder", "VolleyError:" + error.getMessage());
            }
        });

        // Access the RequestQueue through your singleton class.
        mRequestQueue.add(jsObjRequest);

        return serverSSIDs;
    }
}
