package com.wardrive.adapter;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.json.JSONException;
import org.json.JSONObject;

import com.wardrive.HomeFragment;
import com.wardrive.app.JSONDataClass;

import android.os.Build;
import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;
import android.util.Base64;
import android.util.Log;


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
		
				
		// NETWORK INFO
		current_stat.setNetwork_country(tm.getNetworkCountryIso().toUpperCase());
		current_stat.setNetwork_name(tm.getNetworkOperatorName());
		current_stat.setNetwork_type(networkType(context));
		String networkOperatorId = tm.getNetworkOperator();
		
		// MCC AND MNC
		String MCC = networkOperatorId.substring(0, Math.min(networkOperatorId.length(), 3));
		String MNC = networkOperatorId.substring(Math.min(networkOperatorId.length(), 3),networkOperatorId.length());
		current_stat.setNetwork_mcc(MCC);
		current_stat.setNetwork_mnc(MNC);
		current_stat.setGsm_type("test-hardcoded");
		
		// CELL TOWER INFO
		GsmCellLocation CellLocation = (GsmCellLocation)tm.getCellLocation();
		int cell_ID = CellLocation.getCid() & 0xffff;
        int cell_psc= CellLocation.getPsc();
        int cell_lac =CellLocation.getLac();
        current_stat.setCellid(String.valueOf(cell_ID));
        current_stat.setCellpsc(String.valueOf(cell_psc));
        current_stat.setCelllac(String.valueOf(cell_lac));
        
        // CURRENT LOCATION INFO
        getCurrentLocation();
        current_stat.setGps(Double.toString(x) + "," + Double.toString(y));
        
        // CONVERT FROM UNIX TIME TO REAL TIME
        long unixTime = System.currentTimeMillis() / 1000L;
        current_stat.setTimestamp(String.valueOf(unixTime));
        return current_stat;
        
	}
	
	/*
	 * AVAILABLE NETWORK TYPES
	 */
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
	
	// SAVE THE DATA TO THE LOCAL DATABASE
	public static Boolean save_data(Context context, Stats data) {
		// TODO Auto-generated method stub
		StatsDataSource datasource;
	    datasource = new StatsDataSource(context);
	    datasource.open(); 
	    Boolean status = datasource.saveStats(data);
	    datasource.close(); 
	    return status;
	}
	
	
	// GETS THE LOGGED DATA TO DISPLAY ON THE 2ND TAB
	public static List<Stats> get_data(Context context) {
		// TODO Auto-generated method stub
		StatsDataSource datasource;
	    datasource = new StatsDataSource(context);
	    datasource.open(); 
	    List<Stats> statsList = datasource.getAllStats();
	    datasource.close(); 
	    return statsList;
	}
	
	
	// PUSH THE DATA TO THE SERVER USING VOLLEY
	public static boolean PushToServer(Context context, Stats data) {
		// TODO Auto-generated method stub
		try {
			JSONDataClass jsonObj = new JSONDataClass();
//			jsonObj.makeJsonObjReq(data);
			jsonObj.makeStringReq(data);
			
			//jsonObj.makeJsonArryReq();
			
			// CHECK IF THE JSON REQUEST WAS SENT SUCCESSFULLY
			if (jsonObj.IsSuccess())
				return true;
			else
				return false;
		}
		catch (Exception e) {
			Log.e("PushToServer", e.getMessage().toString());
			return false;
		}
	}
	
	/*
	 * CHECK IF THE SERVICE IS RUNNING
	 */
	public static boolean isMyServiceRunning(Context context,Class<?> serviceClass) {
	    ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
	    for (RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
	        if (serviceClass.getName().equals(service.service.getClassName())) {
	            return true;
	        }
	    }
	    return false;
	}
	
    
	final protected static char[] hexArray = "0123456789ABCDEF".toCharArray();
	
	/*
	 * CONVERT ASCII TO HEX
	 */
	public static String bytesToHex( byte[] bytes )
	{
	    char[] hexChars = new char[ bytes.length * 2 ];
	    for( int j = 0; j < bytes.length; j++ )
	    {
	        int v = bytes[ j ] & 0xFF;
	        hexChars[ j * 2 ] = hexArray[ v >>> 4 ];
	        hexChars[ j * 2 + 1 ] = hexArray[ v & 0x0F ];
	    }
	    return new String( hexChars );
	}
    
	
	/*
	 * FUNCTION TO COMPUTE HASH USING SHA1
	 */
	public static String computeSHAHash(String password)
     {	 String SHAHash = null;
         MessageDigest mdSha1 = null;
           try
           {
             mdSha1 = MessageDigest.getInstance("SHA-1");
           } catch (NoSuchAlgorithmException e1) {
             Log.e("myapp", "Error initializing SHA1 message digest");
           }
           try {
               mdSha1.update(password.getBytes("ASCII"));
           } catch (UnsupportedEncodingException e) {
               // TODO Auto-generated catch block
               e.printStackTrace();
           }
           
           byte[] data = mdSha1.digest();
           SHAHash=bytesToHex(data);
           return SHAHash;
           
       }
    
   /*
    * HASHING FUNCTION
    */
   public static String sha1(String s, String keyString) {

	   String result = null;
	   Mac mac = null;
	   SecretKeySpec key = null;
	   byte[] bytes = null;
	   try {
		   key = new SecretKeySpec((keyString).getBytes("UTF-8"), "HmacSHA1");
	   } catch (UnsupportedEncodingException e) {
		   // TODO Auto-generated catch block
		   e.printStackTrace();
	   }

	   try {
		   mac = Mac.getInstance("HmacSHA1");
	   } catch (NoSuchAlgorithmException e) {
		   // TODO Auto-generated catch block
		   e.printStackTrace();
	   }
   
	   try {
		   mac.init(key);
	   } catch (InvalidKeyException e) {
		   // TODO Auto-generated catch block
		   e.printStackTrace();
	   }

	   try {
		   bytes = mac.doFinal(s.getBytes("UTF-8"));
	   } catch (IllegalStateException e) {
		   // TODO Auto-generated catch block
		   e.printStackTrace();
	   } catch (UnsupportedEncodingException e) {
		   // TODO Auto-generated catch block
		   e.printStackTrace();
	   }
	   
	   result = bytesToHex(bytes);
	   return result;
}
	
//	private static String getADID(){
//		Info adInfo = null;
//		adInfo = AdvertisingIdClient.getAdvertisingIdInfo(mContext);
//		String AdId = adInfo.getId();
//		return AdId;
//	}
	
   /*
    * FORMAT THE OBJECT VARIABLES TO BE SENT IN THE JSON REQUEST TO THE SERVER
    */
	public static String toJSON(Stats data) {
		  JSONObject object = new JSONObject();
		  String key = "74c2e4d2b7";
		  try {
		    object.put("IMEI", sha1(data.getImei(),key));
		    object.put("IMSI",sha1(data.getImsi(),key) );
		    object.put("PHONE_MODEL",data.getPhone_model() );
		    object.put("SIM_SN",sha1(data.getSimSN(),key) );
		    object.put("NETWORK_MNC",data.getNetwork_mnc() );
		    object.put("NETWORK_MCC",data.getNetwork_mcc() );
		    object.put("NETWORK_NAME",data.getNetwork_name() );
		    object.put("NETWORK_TYPE",data.getNetwork_type() );
		    object.put("NETWORK_COUNTRY",data.getNetwork_country() );
		    object.put("GSM_TYPE",data.getGsm_type() );
		    object.put("CELL_ID",data.getCellid() );
		    object.put("CELL_PSC",data.getCellpsc() );
		    object.put("CELL_LAC",data.getCelllac() );
		    object.put("RSSI",data.getRssi() );
		    object.put("GPS",data.getGps() );
		    object.put("COLLECTED_TIME",data.getTimestamp() );
//		    object.put("ADID",getADID());
		    
		  } catch (JSONException e) {
		    e.printStackTrace();
		  }
		  System.out.println(object);
		  return object.toString();
		} 
    
    /*
    *	LOCATION RELATED SUB-ROUTINES
    *	GET THE TIMESTAMP OF THE LOCATIONS ACQUIRED BY BOTH THE GPS AND NETWORK PROVIDERS. USE THE LATEST ONE.
    *	IF GPS IS NOT PRESENT, USE THE NETWORK PROVIDER
    *	IF NETWORK IS NOT PRESENT, USE THE GPS PROVIDER
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
    
    /*
     * CAPITALIZE THE INPUT STRING
     */
    private static String Capitalize(String s)
    {
    	  if (s == null || s.length() == 0)
    	    return "";
    	  
    	  char first = s.charAt(0);
    	  if (Character.isUpperCase(first))
    	    return s;
    	  else
    	    return Character.toUpperCase(first) + s.substring(1);
    }    	

}
