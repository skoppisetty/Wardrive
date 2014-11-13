package com.wardrive.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;


public class Stats {
	  private long id;
	  private String imei;
	  private String simSN;
	  private String network_mnc;
	  private String network_mcc;
	  private String network_name;
	  private String network_type;
	  private String network_country;
	  private String cellid;
	  private String cellpsc;
	  private String celllac;
	  private String timestamp;
//	  private String imei;

	  public long getId() {
	    return id;
	  }

	  public void setId(long id) {
	    this.id = id;
	  }

	public String getNetwork_type() {
		return network_type;
	}

	public void setNetwork_type(String network_type) {
		this.network_type = network_type;
	}

	public String getCellid() {
		return cellid;
	}

	public void setCellid(String cellid) {
		this.cellid = cellid;
	}

	public String getImei() {
		return imei;
	}

	public void setImei(String imei) {
		this.imei = imei;
	}

	public String getSimSN() {
		return simSN;
	}

	public void setSimSN(String simSN) {
		this.simSN = simSN;
	}

	public String getNetwork_mnc() {
		return network_mnc;
	}

	public void setNetwork_mnc(String network_mnc) {
		this.network_mnc = network_mnc;
	}

	public String getNetwork_mcc() {
		return network_mcc;
	}

	public void setNetwork_mcc(String network_mcc) {
		this.network_mcc = network_mcc;
	}

	public String getNetwork_name() {
		return network_name;
	}

	public void setNetwork_name(String network_name) {
		this.network_name = network_name;
	}

	public String getNetwork_country() {
		return network_country;
	}

	public void setNetwork_country(String network_country) {
		this.network_country = network_country;
	}

	public String getCelllac() {
		return celllac;
	}

	public void setCelllac(String celllac) {
		this.celllac = celllac;
	}

	public String getCellpsc() {
		return cellpsc;
	}

	public void setCellpsc(String cellpsc) {
		this.cellpsc = cellpsc;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
	
	@SuppressLint("DefaultLocale")
	public Stats gen_data(Context context) {
		// TODO Auto-generated method stub
		//general info
		Stats current_stat = new Stats();
		TelephonyManager  tm=(TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);  
		current_stat.setImei(tm.getDeviceId());
		current_stat.setSimSN(tm.getSimSerialNumber());
				
		// Network Info
		current_stat.setNetwork_country(tm.getNetworkCountryIso().toUpperCase());
		current_stat.setNetwork_name(tm.getNetworkOperatorName());
		current_stat.setNetwork_type(networkType(context));
		String networkOperatorId = tm.getNetworkOperator();
		
		String MCC = networkOperatorId.substring(0, Math.min(networkOperatorId.length(), 3));
		String MNC = networkOperatorId.substring(networkOperatorId.length() - 3);
		
		current_stat.setNetwork_mcc(MCC);
		current_stat.setNetwork_mnc(MNC);
		
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
	private String networkType(Context context) {
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
	
	} 