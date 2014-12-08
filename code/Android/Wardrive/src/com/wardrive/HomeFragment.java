package com.wardrive;

import java.util.List;

import com.wardrive.adapter.Stats;
import com.wardrive.adapter.StatsDataSource;
import com.wardrive.adapter.GatherStats;
//import com.wardrive.adapter.Toast;


import android.support.v4.app.Fragment;
import android.telephony.CellLocation;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;

public class HomeFragment extends Fragment
implements LocationListener
{
	private StatsDataSource datasource;
	private Context thiscontext;
	PhoneStateListener myPhoneStateListener;
	TelephonyManager ttm;
	static LocationManager lm;
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		
		// GET THE CONTEXT
		thiscontext = container.getContext();
		ttm  = (TelephonyManager)getActivity().getSystemService(Context.TELEPHONY_SERVICE);
		
		final View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        
		// BUTTON TO START THE DAEMON
		Button start_saving = (Button)rootView.findViewById(R.id.btn_save);
		
		//BUTTON TO STOP THE DAEMON
		Button stop_saving = (Button)rootView.findViewById(R.id.btn_stop);
        
		//Listening to button event - to start daemon
		start_saving.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
            	Stats data = GatherStats.gen_data(thiscontext);
            	load_data(data, rootView);
            	getActivity().startService(new Intent(thiscontext, Savedbservice.class));
            	setdaemonstatus(rootView);
            }
        });
		
		//Listening to button event - to stop daemon
		stop_saving.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
            	getActivity().stopService(new Intent(thiscontext, Savedbservice.class));
            	setdaemonstatus(rootView);
            }
        });
        phoneStateSetup(rootView);
        
        return rootView;
    }
	
	/*
	 * LOAD DATA ON THE SCREEN 
	 */
	private void load_data(Stats data, View rootView) {
		// TODO Auto-generated method stub
		TextView IMEI = (TextView)rootView.findViewById(R.id.IMEI_value);					// IMEI
		TextView IMSI = (TextView)rootView.findViewById(R.id.IMSI_value);					// IMSI
		TextView Phone_model = (TextView)rootView.findViewById(R.id.phonemodel_value);		// PHONE MODEL
		TextView simSN = (TextView)rootView.findViewById(R.id.SIM_value);					// SIM NUMBER
		TextView MNC = (TextView)rootView.findViewById(R.id.MNC_value);						// MNC
		TextView MCC = (TextView)rootView.findViewById(R.id.MCC_value);						// MCC
		TextView N_name = (TextView)rootView.findViewById(R.id.networkname_value);			// NETWORK OPERATOR NAME
		TextView N_type = (TextView)rootView.findViewById(R.id.networktype_value);			// NETWORK TYPE
		TextView N_country = (TextView)rootView.findViewById(R.id.networkcountry_value);	// NETWORK OPERTOR COUNTRY
		TextView cellid = (TextView)rootView.findViewById(R.id.cellid_value);				// CELL ID
		TextView psc = (TextView)rootView.findViewById(R.id.psc_value);						// PSC
		TextView lac = (TextView)rootView.findViewById(R.id.lac_value);						// LAC
		TextView gps = (TextView)rootView.findViewById(R.id.gps_value);						// USER LOCATION
		TextView rssi = (TextView)rootView.findViewById(R.id.rssi_value);					// RSSI
		TextView timestamp_view = (TextView)rootView.findViewById(R.id.timestamp_value);	// TIMESTAMP
		
		/*
		 * SET ALL THE OBJECT VARIABLES
		 */
		IMEI.setText(data.getImei());
		IMSI.setText(data.getImsi());
		Phone_model.setText(data.getPhone_model());
		simSN.setText(data.getSimSN());
		MCC.setText(data.getNetwork_mcc());
		MNC.setText(data.getNetwork_mnc());
		N_name.setText(data.getNetwork_name());
		N_type.setText(data.getNetwork_type());
		N_country.setText(data.getNetwork_country());
		gps.setText(data.getGps());
		cellid.setText(data.getCellid());
		psc.setText(data.getCellpsc());
		lac.setText(data.getCelllac());
		if(data.getRssi()!= null){
			rssi.setText(data.getRssi()+"db");
		}
		setdaemonstatus(rootView);
		timestamp_view.setText(data.getRealTime().toString());
		
	}

	/*
	 * CONTAINS A LISTENER ON onCellLocationChanged
	 * TRIGGER AN EVENT ONCE THE CELL LOCATION CHANGES AND FETCH NEW DATA FROM THE NETWORK
	 */
	private void phoneStateSetup(final View rootView){
		 myPhoneStateListener = new PhoneStateListener() {
		 public void onCellLocationChanged(CellLocation location){
			Stats data = GatherStats.gen_data(thiscontext);
			load_data(data,rootView);
		 }
		 
		 /*
		  * (non-Javadoc)
		  * @see android.telephony.PhoneStateListener#onSignalStrengthsChanged(android.telephony.SignalStrength)
		  * LISTENER ON SIGNAL STRENGTH CHANGE
		  */
		 public void onSignalStrengthsChanged(SignalStrength signalStrength){
			
			 try {
				 
				// GET LATEST RSSI VALUE 
				int rssi = -113 + 2 * signalStrength.getGsmSignalStrength();
				
				// GET LATEST NETWORK DATA
				Stats data = GatherStats.gen_data(thiscontext);
				data.setRssi(String.valueOf(rssi));
				
				// DISPLAY DATA ON THE SCREEN
				load_data(data,rootView);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			 
		 }
		 };

		 try{
		    ttm.listen(myPhoneStateListener, PhoneStateListener.LISTEN_CELL_LOCATION | PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);
		 }catch(Exception e){
			 e.printStackTrace();
		 }
		}
	
	/*
	 * DISPLAY THE DAEMON STAUS ON THE UI (SCREEN)
	 */
	private void setdaemonstatus(final View rootView){
		TextView daemon = (TextView)rootView.findViewById(R.id.daemon_value);
		if(GatherStats.isMyServiceRunning(thiscontext,Savedbservice.class)){
			daemon.setText("Running");
		}
		else{
			daemon.setText("Not Running");
		}
	}

	
	@Override
    public void onLocationChanged(Location loc) { }

  
    //@Override
    public void onProviderDisabled(String provider) { }


    //@Override
    public void onProviderEnabled(String provider) { }


    //@Override
    public void onStatusChanged(String provider, int status, Bundle extras) { }

    /*
     * GPS LOCATION LISTENER
     * removeUpdates CALLED ON BOTH THE LOCATION LISTENERS
     */
    public static LocationListener locationListenerGps = new LocationListener() {
        public void onLocationChanged(Location location) {
            //timer.cancel();
            //double x =location.getLatitude();
            //double y = location.getLongitude();
            lm.removeUpdates(this);
            lm.removeUpdates(locationListenerNetwork);
        }

        public void onProviderDisabled(String provider) { }

        public void onProviderEnabled(String provider) { }

        public void onStatusChanged(String provider, int status, Bundle extras) { }
    };

    /*
     * NETWORK LOCATION LISTENER
     * removeUpdates CALLED ON BOTH THE LOCATION LISTENERS
     */
    public static LocationListener locationListenerNetwork = new LocationListener() {
        public void onLocationChanged(Location location) {
            //timer.cancel();
            //double x = location.getLatitude();
            //double y = location.getLongitude();
            lm.removeUpdates(this);
            lm.removeUpdates(locationListenerGps);

        }

        public void onProviderDisabled(String provider) { }

        public void onProviderEnabled(String provider) { }

        public void onStatusChanged(String provider, int status, Bundle extras) { }
    };

	
}
