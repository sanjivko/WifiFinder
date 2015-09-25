package com.slowhand.sanjiv.wififinder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Created by sanjiv on 18/9/15.
 */

public class SSIDListAdapter extends ArrayAdapter<SSIDListAdapterEntry> {

    private Context context = null;
    private SSIDListAdapterEntry[] values;

    public SSIDListAdapter(Context context, int resource, SSIDListAdapterEntry[] objects) {
        super(context, resource, objects);
        this.context = context;
        values = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //return super.getView(position, convertView, parent);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.ssid_list_row_layout, parent, false);

        TextView textViewSSID = (TextView) rowView.findViewById(R.id.ssid);
        TextView textViewWifiSec = (TextView) rowView.findViewById(R.id.wifi_sec);

        textViewSSID.setText(values[position].ssid_name);
        textViewWifiSec.setText(values[position].wifi_security);

        return rowView;
    }
}
