package com.wardrive;

import com.wardrive.adapter.GatherStats;
import com.wardrive.adapter.Stats;
import com.wardrive.adapter.StatsDataSource;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.telephony.CellLocation;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class Savedbservice extends Service {
	PhoneStateListener myPhoneStateListener;
	TelephonyManager ttm;
	int latest_rssi = 0;
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
    public void onCreate() {

    }
 
    @Override
    public void onStart(Intent intent, int startId) {
        Toast.makeText(this, "Daemon Started", Toast.LENGTH_LONG).show();
        phoneStateSetup(this);
    	
          
    }
 
    @Override
    public void onDestroy() {
        Toast.makeText(this, "Daemon Stopped", Toast.LENGTH_LONG).show();
        try{
        	 if(myPhoneStateListener != null){ttm.listen(myPhoneStateListener, PhoneStateListener.LISTEN_NONE);}
        	}catch(Exception e){
        	 e.printStackTrace();
        }
    }


	
  
	private void phoneStateSetup(final Context context){
		 ttm = (TelephonyManager) context
	            .getSystemService(Context.TELEPHONY_SERVICE);
		 myPhoneStateListener = new PhoneStateListener() {
		 public void onCellLocationChanged(CellLocation location){
			Stats data = GatherStats.gen_data(context);
			data.setRssi(String.valueOf(latest_rssi));
			Boolean status = GatherStats.save_data(context,data);
        	if(status){
        		Toast.makeText(context, "Successfully saved to database" , 
        				   Toast.LENGTH_LONG).show();
        	}
        	else{
        		Toast.makeText(context, "Failed to store to database", 
        				   Toast.LENGTH_LONG).show();
        	}
		 }
		 public void onSignalStrengthsChanged(SignalStrength signalStrength ){
			 latest_rssi = -113 + 2 * signalStrength.getGsmSignalStrength();

		 }
	
		 };
		 
		 try{
			 
			 ttm.listen(myPhoneStateListener, PhoneStateListener.LISTEN_CELL_LOCATION | PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);
			 
		 }catch(Exception e){

		 }
		}
}
