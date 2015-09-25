package com.slowhand.sanjiv.wififinder;


import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

import java.util.List;
import java.util.Vector;

/**
 * Created by sanjiv on 18/9/15.
 */
public class WifiConnManager {

    private static final int MAX_SSID_RESULTS = 10;
    WifiManager sysWifiMgr;
    List<ScanResult> mScannedSSIDList;

    public WifiConnManager(WifiManager mgr) {
        sysWifiMgr = mgr;
    }

    Vector<SSIDListAdapterEntry> scan() {
        Vector<SSIDListAdapterEntry> ssids = new Vector<>();

        if (sysWifiMgr.isWifiEnabled()) {
            mScannedSSIDList = sysWifiMgr.getScanResults();

            int max_results = (MAX_SSID_RESULTS > mScannedSSIDList.size()) ? mScannedSSIDList.size() : MAX_SSID_RESULTS;
            for (int i = 0; i < max_results; i++) {
                SSIDListAdapterEntry ssidEntry = new SSIDListAdapterEntry();
                ssidEntry.ssid_name = mScannedSSIDList.get(i).SSID;
                ssidEntry.wifi_security = mScannedSSIDList.get(i).capabilities;

                ssids.add(ssidEntry);
            }
            return ssids;
        } else {
            Log.d("WifiFinder", "Wifi is disabled");
            sysWifiMgr.setWifiEnabled(true);
        }
        return null;
    }

    boolean isConnected() {
        WifiInfo wifiInfo = sysWifiMgr.getConnectionInfo();

        Log.d("WiFinder", "isConnected:" + wifiInfo.getSSID());
        return true;
    }

    void connect(SSIDListAdapterEntry entry) {
        WifiConfiguration wifiConf = new WifiConfiguration();
        wifiConf.SSID = "\"" + entry.ssid_name + "\"";
        wifiConf.preSharedKey = "\"" + entry.pass_key + "\"";

        List<WifiConfiguration> configuredNetworkList = sysWifiMgr.getConfiguredNetworks();

        if (configuredNetworkList.size() == 0) {
            Log.d("WiFinder", "No configured WiFi networks");
        }

        for (int i = 0; i < configuredNetworkList.size(); i++) {
            Log.d("WiFinder", "(" + configuredNetworkList.get(i).SSID + ")");
            Log.d("WiFinder", " ++++" + configuredNetworkList.get(i).networkId);
            if (configuredNetworkList.get(i).SSID.equals("\"" + entry.ssid_name + "\"")) {
                Log.d("WiFinder", "HERE>>>>>>>>>>>>>>>>>> ");

                wifiConf.allowedProtocols.set(WifiConfiguration.Protocol.RSN); // For WPA2
                wifiConf.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
                wifiConf.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
                wifiConf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
            }
        }

        int id = sysWifiMgr.addNetwork(wifiConf);
        sysWifiMgr.saveConfiguration();
        configuredNetworkList = sysWifiMgr.getConfiguredNetworks();

        for (int i = 0; i < configuredNetworkList.size(); i++) {
            if (configuredNetworkList.get(i).SSID.equals("\"" + entry.ssid_name + "\"")) {
                id = configuredNetworkList.get(i).networkId;
                sysWifiMgr.enableNetwork(id, true);
            }
        }
    }

    void connect() {
        WifiConfiguration wifiConf = new WifiConfiguration();
        wifiConf.SSID = "\"saipra123\"";
        wifiConf.preSharedKey = "\"saisai85\"";

        for (int i = 0; i < mScannedSSIDList.size(); i++) {
            if (mScannedSSIDList.get(i).SSID == "\"saipra123\"") {
                Log.d("WiFinder", mScannedSSIDList.get(i).capabilities);
            }
        }

        List<WifiConfiguration> configuredNetworkList = sysWifiMgr.getConfiguredNetworks();

        if (configuredNetworkList.size() == 0) {
            Log.d("WiFinder", "No configured WiFi networks");
        }

        for (int i = 0; i < configuredNetworkList.size(); i++) {
            Log.d("WiFinder", "(" + configuredNetworkList.get(i).SSID + ")");
            Log.d("WiFinder", " ++++" + configuredNetworkList.get(i).networkId);
            if (configuredNetworkList.get(i).SSID.equals("\"saipra123\"")) {
                Log.d("WiFinder", "HERE>>>>>>>>>>>>>>>>>> ");

                wifiConf.allowedProtocols.set(WifiConfiguration.Protocol.RSN); // For WPA2
                wifiConf.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
                wifiConf.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
                wifiConf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
            }
        }


        int id = sysWifiMgr.addNetwork(wifiConf);
        sysWifiMgr.saveConfiguration();
        configuredNetworkList = sysWifiMgr.getConfiguredNetworks();

        for (int i = 0; i < configuredNetworkList.size(); i++) {
            if (configuredNetworkList.get(i).SSID.equals("\"saipra123\"")) {
                id = configuredNetworkList.get(i).networkId;
                sysWifiMgr.enableNetwork(id, true);
            }
        }

    }
}
