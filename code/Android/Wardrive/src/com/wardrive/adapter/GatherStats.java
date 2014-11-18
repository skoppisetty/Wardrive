package com.wardrive.adapter;

import com.wardrive.HomeFragment;
import android.os.Build;
import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;


public class GatherStats {
	
	static boolean gps_enabled;
	static boolean network_enabled;
    static double x, y;
	static LocationManager lm;
	
	@SuppressLint("DefaultLocale")
	public static Stats gen_data(Context context) {
		Stats current_stat = new Stats();
		TelephonyManager  tm=(TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);  
		lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
		
		current_stat.setImei(tm.getDeviceId());
		current_stat.setSimSN(tm.getSimSerialNumber());
		current_stat.setImsi(tm.getSubscriberId());
		
		// PHONE MODEL
		String phoneModel = Build.MODEL;
		String phoneManufacturer = Build.MANUFACTURER;
		if (phoneModel.startsWith(phoneManufacturer)) {
			current_stat.setPhone_model(Capitalize(phoneModel));
		  } else {
			  current_stat.setPhone_model(Capitalize(phoneManufacturer) + " " + phoneModel); 
		  }
		
		
				
		// Network Info
		current_stat.setNetwork_country(tm.getNetworkCountryIso().toUpperCase());
		current_stat.setNetwork_name(tm.getNetworkOperatorName());
		current_stat.setNetwork_type(networkType(context));
		String networkOperatorId = tm.getNetworkOperator();
		
		String MCC = networkOperatorId.substring(0, Math.min(networkOperatorId.length(), 3));
		String MNC = networkOperatorId.substring(Math.min(networkOperatorId.length(), 3),networkOperatorId.length());
		
		current_stat.setNetwork_mcc(MCC);
		current_stat.setNetwork_mnc(MNC);
		current_stat.setGsm_type("test-hardcoded");
		
		// Cell tower info
		GsmCellLocation CellLocation = (GsmCellLocation)tm.getCellLocation();
		int cell_ID = CellLocation.getCid() & 0xffff;
        int cell_psc= CellLocation.getPsc();
        int cell_lac =CellLocation.getLac();
        current_stat.setCellid(String.valueOf(cell_ID));
        current_stat.setCellpsc(String.valueOf(cell_psc));
        current_stat.setCelllac(String.valueOf(cell_lac));
        
        // Location Info
        getCurrentLocation();
        current_stat.setGps(Double.toString(x) + "," + Double.toString(y));
        
        long unixTime = System.currentTimeMillis() / 1000L;
        current_stat.setTimestamp(String.valueOf(unixTime));
        return current_stat;
        
	}
	private static String networkType(Context context) {
		 TelephonyManager teleMan = (TelephonyManager)
				 context.getSystemService(Context.TELEPHONY_SERVICE);
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
	
	public static Boolean save_data(Context context, Stats data) {
		// TODO Auto-generated method stub
		StatsDataSource datasource;
	    datasource = new StatsDataSource(context);
	    datasource.open(); 
	    Boolean status = datasource.saveStats(data);
	    datasource.close(); 
	    return status;
	}
	public static boolean isMyServiceRunning(Context context,Class<?> serviceClass) {
	    ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
	    for (RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
	        if (serviceClass.getName().equals(service.service.getClassName())) {
	            return true;
	        }
	    }
	    return false;
	}
	
    
    
    /*
    *	LOCATION RELATED SUB-ROUTINES
    */
    private static void getCurrentLocation() {
    	
    	// LOCATION STUFF
        gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    	
        	if (lm != null)
        	{
        		lm.removeUpdates(HomeFragment.locationListenerGps);
        		lm.removeUpdates(HomeFragment.locationListenerNetwork);
        	}

            Location net_loc=null, gps_loc=null;
            if(gps_enabled)
                gps_loc=lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if(network_enabled)
                net_loc=lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

            //if there are both values use the latest one
            if(gps_loc!=null && net_loc!=null){
                if(gps_loc.getTime()>net_loc.getTime())
               {
                	x = gps_loc.getLatitude();
                	y = gps_loc.getLongitude();
               }
                else
               {
                	x = net_loc.getLatitude();
                	y = net_loc.getLongitude();
               }

            }
            else if(gps_loc!=null){
                 {
                	 x = gps_loc.getLatitude();
                	 y = gps_loc.getLongitude();
                 }

            }
            else if(net_loc!=null)
               {
            	x = net_loc.getLatitude();
            	y = net_loc.getLongitude();
            }
            else
            {
            	// No Provider
            }
        
        }
    
    	private static String Capitalize(String s) {
    	  if (s == null || s.length() == 0) {
    	    return "";
    	  }
    	  char first = s.charAt(0);
    	  if (Character.isUpperCase(first)) {
    	    return s;
    	  } else {
    	    return Character.toUpperCase(first) + s.substring(1);
    	  }
    	}

}
