package com.wardrive.adapter;


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

	} 