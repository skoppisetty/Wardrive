package com.wardrive;

import com.wardrive.adapter.Stats;
import com.wardrive.adapter.StatsDataSource;

import android.support.v4.app.Fragment;
import android.telephony.NeighboringCellInfo;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;
import android.annotation.SuppressLint;
import android.content.Context;
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
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		
		thiscontext = container.getContext();
        
		final View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        
		Button refresh = (Button)rootView.findViewById(R.id.btn_save);
        //Listening to button event
        refresh.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
            	Stats data = gen_data();
            	Boolean status = save_data(data);
            	long unixTime = System.currentTimeMillis() / 1000L;
            	
            	Toast.makeText(thiscontext, String.valueOf(unixTime) , 
     				   Toast.LENGTH_LONG).show();
            	if(status){
            		Toast.makeText(thiscontext, "Successfully saved to database" , 
            				   Toast.LENGTH_LONG).show();
            	}
            	else{
            		Toast.makeText(thiscontext, "Failed to store to database", 
            				   Toast.LENGTH_LONG).show();
            	}
            }
        });
        Stats data = gen_data();
        load_data(data,rootView);
        save_data(data);
        
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

	@SuppressLint("DefaultLocale")
	private Stats gen_data() {
		// TODO Auto-generated method stub
		//general info
		Stats current_stat = new Stats();
		TelephonyManager  tm=(TelephonyManager)getActivity().getSystemService(Context.TELEPHONY_SERVICE);  
		current_stat.setImei(tm.getDeviceId());
		current_stat.setSimSN(tm.getSimSerialNumber());
				
		// Network Info
		current_stat.setNetwork_country(tm.getNetworkCountryIso().toUpperCase());
		current_stat.setNetwork_name(tm.getNetworkOperatorName());
		current_stat.setNetwork_type(networkType());
		String networkOperatorId = tm.getNetworkOperator();
		
		String MCC = networkOperatorId.substring(0, Math.min(networkOperatorId.length(), 3));
		String MNC = networkOperatorId.substring(networkOperatorId.length() - 3);
		
		current_stat.setNetwork_mcc(MCC);
		current_stat.setNetwork_mnc(MNC);
		
		// Cell tower info
		GsmCellLocation CellLocation = (GsmCellLocation)tm.getCellLocation();
		int cell_ID = CellLocation.getCid();
        int cell_psc= CellLocation.getPsc();
        int cell_lac =CellLocation.getLac();
        current_stat.setCellid(String.valueOf(cell_ID));
        current_stat.setCellpsc(String.valueOf(cell_psc));
        current_stat.setCelllac(String.valueOf(cell_lac));
        long unixTime = System.currentTimeMillis() / 1000L;
        current_stat.setTimestamp(String.valueOf(unixTime));
        return current_stat;
        
	}

	private String networkType() {
		 TelephonyManager teleMan = (TelephonyManager)
		    		getActivity().getSystemService(Context.TELEPHONY_SERVICE);
		    int networkType = teleMan.getNetworkType();
		    switch (networkType) {
		        case TelephonyManager.NETWORK_TYPE_1xRTT: return "1xRTT";
		        case TelephonyManager.NETWORK_TYPE_CDMA: return "CDMA";
		        case TelephonyManager.NETWORK_TYPE_EDGE: return "EDGE";
		        case TelephonyManager.NETWORK_TYPE_EHRPD: return "eHRPD";
		        case TelephonyManager.NETWORK_TYPE_EVDO_0: return "EVDO rev. 0";
		        case TelephonyManager.NETWORK_TYPE_EVDO_A: return "EVDO rev. A";
		        case TelephonyManager.NETWORK_TYPE_EVDO_B: return "EVDO rev. B";
		        case TelephonyManager.NETWORK_TYPE_GPRS: return "GPRS";
		        case TelephonyManager.NETWORK_TYPE_HSDPA: return "HSDPA";
		        case TelephonyManager.NETWORK_TYPE_HSPA: return "HSPA";
		        case TelephonyManager.NETWORK_TYPE_HSPAP: return "HSPA+";
		        case TelephonyManager.NETWORK_TYPE_HSUPA: return "HSUPA";
		        case TelephonyManager.NETWORK_TYPE_IDEN: return "iDen";
		        case TelephonyManager.NETWORK_TYPE_LTE: return "LTE";
		        case TelephonyManager.NETWORK_TYPE_UMTS: return "UMTS";
		        case TelephonyManager.NETWORK_TYPE_UNKNOWN: return "Unknown";
		    }
		    throw new RuntimeException("New type of network");
	}


}
