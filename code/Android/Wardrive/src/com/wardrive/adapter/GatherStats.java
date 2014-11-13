package com.wardrive.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;


public class GatherStats {

		
	@SuppressLint("DefaultLocale")
	public static Stats gen_data(Context context) {
		Stats current_stat = new Stats();
		TelephonyManager  tm=(TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);  
		current_stat.setImei(tm.getDeviceId());
		current_stat.setSimSN(tm.getSimSerialNumber());
		current_stat.setImsi(tm.getSubscriberId());
		current_stat.setPhone_model(android.os.Build.MODEL);
		
				
		// Network Info
		current_stat.setNetwork_country(tm.getNetworkCountryIso().toUpperCase());
		current_stat.setNetwork_name(tm.getNetworkOperatorName());
		current_stat.setNetwork_type(networkType(context));
		String networkOperatorId = tm.getNetworkOperator();
		
		String MCC = networkOperatorId.substring(0, Math.min(networkOperatorId.length(), 3));
		String MNC = networkOperatorId.substring(networkOperatorId.length() - 3);
		
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
	
}
