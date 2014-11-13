package com.wardrive;

import com.wardrive.adapter.Stats;
import com.wardrive.adapter.StatsDataSource;

import android.support.v4.app.Fragment;
import android.telephony.CellLocation;
import android.telephony.NeighboringCellInfo;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class HomeFragment extends Fragment {
	private StatsDataSource datasource;
	private Context thiscontext;
	PhoneStateListener myPhoneStateListener;
	TelephonyManager ttm;
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		
		thiscontext = container.getContext();
		ttm  = (TelephonyManager)getActivity().getSystemService(Context.TELEPHONY_SERVICE);
		
		final View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        
		Button start_saving = (Button)rootView.findViewById(R.id.btn_save);
		Button stop_saving = (Button)rootView.findViewById(R.id.btn_stop);
        //Listening to button event
		start_saving.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
            	Stats data = new Stats();
    			data = data.gen_data(thiscontext);
            	load_data(data,rootView);
            	getActivity().startService(new Intent(thiscontext, Savedbservice.class));
            }
        });
		stop_saving.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
            	getActivity().stopService(new Intent(thiscontext, Savedbservice.class));
            }
        });
        phoneStateSetup(rootView);
        
        return rootView;
    }
	private void load_data(Stats data, View rootView) {
		// TODO Auto-generated method stub
		TextView IMEI = (TextView)rootView.findViewById(R.id.IMEI_value);
		TextView simSN = (TextView)rootView.findViewById(R.id.SIM_value);
		TextView MNC = (TextView)rootView.findViewById(R.id.MNC_value);
		TextView MCC = (TextView)rootView.findViewById(R.id.MCC_value);
		TextView N_name = (TextView)rootView.findViewById(R.id.networkname_value);
		TextView N_type = (TextView)rootView.findViewById(R.id.networktype_value);
		TextView N_country = (TextView)rootView.findViewById(R.id.networkcountry_value);
		TextView cellid = (TextView)rootView.findViewById(R.id.cellid_value);
		TextView psc = (TextView)rootView.findViewById(R.id.psc_value);
		TextView lac = (TextView)rootView.findViewById(R.id.lac_value);
		TextView timestamp_view = (TextView)rootView.findViewById(R.id.timestamp_value);
		
		IMEI.setText(data.getImei());
		simSN.setText(data.getSimSN());
		MCC.setText(data.getNetwork_mcc());
		MNC.setText(data.getNetwork_mnc());
		N_name.setText(data.getNetwork_name());
		N_type.setText(data.getNetwork_type());
		N_country.setText(data.getNetwork_country());
		cellid.setText(data.getCellid());
		psc.setText(data.getCellpsc());
		lac.setText(data.getCelllac());
		timestamp_view.setText(data.getTimestamp());
		
	}

	private Boolean save_data(Stats data) {
		// TODO Auto-generated method stub
	    datasource = new StatsDataSource(thiscontext);
	    datasource.open(); 
	    Boolean status = datasource.saveStats(data);
	    datasource.close(); 
	    return status;
	}



	
	private void phoneStateSetup(final View rootView){
		 myPhoneStateListener = new PhoneStateListener() {
		 public void onCellLocationChanged(CellLocation location){
			Stats data = new Stats();
			data = data.gen_data(thiscontext);
			load_data(data,rootView);
		 }
	
		 };

		 try{
		    ttm.listen(myPhoneStateListener, PhoneStateListener.LISTEN_CELL_LOCATION);
		 }catch(Exception e){

		 }
		}

}
