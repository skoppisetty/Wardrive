package com.wardrive;

import com.wardrive.adapter.Stats;
import com.wardrive.adapter.StatsDataSource;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.telephony.CellLocation;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class Savedbservice extends Service {

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
    public void onCreate() {
 
        Toast.makeText(this, "Congrats! MyService Created", Toast.LENGTH_LONG).show();
        
    }
 
    @Override
    public void onStart(Intent intent, int startId) {
        Toast.makeText(this, "My Service Started", Toast.LENGTH_LONG).show();
        phoneStateSetup(this);
    	
          
    }
 
    @Override
    public void onDestroy() {
        Toast.makeText(this, "MyService Stopped", Toast.LENGTH_LONG).show();
        
    }

	private Boolean save_data(Stats data) {
		// TODO Auto-generated method stub
		StatsDataSource datasource;
	    datasource = new StatsDataSource(this);
	    datasource.open(); 
	    Boolean status = datasource.saveStats(data);
	    datasource.close(); 
	    return status;
	}
	
  
	private void phoneStateSetup(final Context context){
    	 PhoneStateListener myPhoneStateListener;
    	 TelephonyManager ttm = (TelephonyManager) context
    	            .getSystemService(Context.TELEPHONY_SERVICE);
		 myPhoneStateListener = new PhoneStateListener() {
		 public void onCellLocationChanged(CellLocation location){
			Stats data = new Stats();
			data = data.gen_data(context);
			Boolean status = save_data(data);
        	if(status){
        		Toast.makeText(context, "Successfully saved to database" , 
        				   Toast.LENGTH_LONG).show();
        	}
        	else{
        		Toast.makeText(context, "Failed to store to database", 
        				   Toast.LENGTH_LONG).show();
        	}
		 }
	
		 };
		 
		 try{
			 
			 ttm.listen(myPhoneStateListener, PhoneStateListener.LISTEN_CELL_LOCATION);
			 
		 }catch(Exception e){

		 }
		}
}
