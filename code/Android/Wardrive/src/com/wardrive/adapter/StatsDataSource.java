package com.wardrive.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class StatsDataSource {

  // Database fields
  private SQLiteDatabase database;
  private DBAdapter dbHelper;
  private String[] allColumns = { DBAdapter.STAT_ID,
		  DBAdapter.IMEI, DBAdapter.IMSI , DBAdapter.PHONE_MODEL, DBAdapter.SIM , DBAdapter.NETWORK_MCC ,DBAdapter.NETWORK_MNC,
		  DBAdapter.NETWORK_NAME, DBAdapter.NETWORK_COUNTRY, DBAdapter.NETWORK_TYPE,DBAdapter.GSM_TYPE,
		  DBAdapter.CELL_ID, DBAdapter.CELL_PSC, DBAdapter.CELL_LAC,DBAdapter.RSSI,DBAdapter.GPS, DBAdapter.TIMESTAMP  };

  public StatsDataSource(Context context) {
    dbHelper = new DBAdapter(context);
  }

  public void open() throws SQLException {
    database = dbHelper.getWritableDatabase();
  }

  public void close() {
    dbHelper.close();
  }
  
  public boolean saveStats(Stats curr_stats){
	  ContentValues values = new ContentValues();
	  values.put(DBAdapter.IMEI, curr_stats.getImei());
	  values.put(DBAdapter.IMSI, curr_stats.getImsi());
	  values.put(DBAdapter.PHONE_MODEL, curr_stats.getPhone_model());
	  values.put(DBAdapter.SIM, curr_stats.getSimSN());
	  values.put(DBAdapter.NETWORK_MCC, curr_stats.getNetwork_mcc());
	  values.put(DBAdapter.NETWORK_MNC, curr_stats.getNetwork_mnc());
	  values.put(DBAdapter.NETWORK_NAME, curr_stats.getNetwork_name());
	  values.put(DBAdapter.NETWORK_COUNTRY, curr_stats.getNetwork_country());
	  values.put(DBAdapter.NETWORK_TYPE, curr_stats.getNetwork_type());
	  values.put(DBAdapter.GSM_TYPE, curr_stats.getGsm_type());
	  values.put(DBAdapter.CELL_ID, curr_stats.getCellid());
	  values.put(DBAdapter.CELL_PSC, curr_stats.getCellpsc());
	  values.put(DBAdapter.CELL_LAC, curr_stats.getCelllac());
	  values.put(DBAdapter.RSSI, curr_stats.getRssi());
	  values.put(DBAdapter.GPS, curr_stats.getGps());
	  values.put(DBAdapter.TIMESTAMP, curr_stats.getTimestamp());
	  values.put(DBAdapter.IS_SYNCED, 0);
	  long insertId;
	  try {
		insertId = database.insert(DBAdapter.TABLE_STATS, null,
			        values);
		
		if (insertId > 0)
			return true;
		
	  } catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		return false;
		
	  }
//	  Cursor cursor = database.query(DBAdapter.TABLE_STATS,allColumns, DBAdapter.STAT_ID + " = " + insertId, null,null, null, null);
//	  cursor.moveToFirst();
//	  Stats newStat = cursorToStat(cursor);
//	  cursor.close();
	  return true;
	  
  }
  public Stats createStats(String imei,String network_type,String cell_id ) {
    ContentValues values = new ContentValues();
    values.put(DBAdapter.IMEI, imei);
    values.put(DBAdapter.NETWORK_TYPE, network_type);
    values.put(DBAdapter.CELL_ID, cell_id);
    long insertId = database.insert(DBAdapter.TABLE_STATS, null,
        values);
    Cursor cursor = database.query(DBAdapter.TABLE_STATS,
        allColumns, DBAdapter.STAT_ID + " = " + insertId, null,
        null, null, null);
    cursor.moveToFirst();
    Stats newStat = cursorToStat(cursor);
    cursor.close();
    return newStat;
  }

  public void deleteStats(Stats stat_del) {
    long id = stat_del.getId();
    System.out.println("Stats deleted with id: " + id);
    database.delete(DBAdapter.TABLE_STATS, DBAdapter.STAT_ID
        + " = " + id, null);
  }

  public List<Stats> getAllStats() {
    List<Stats> stats = new ArrayList<Stats>();

    Cursor cursor = database.query(DBAdapter.TABLE_STATS,
        allColumns, null, null, null, null, null);

    cursor.moveToFirst();
    while (!cursor.isAfterLast()) {
      Stats stat = cursorToStat(cursor);
      stats.add(stat);
      cursor.moveToNext();
    }
    // make sure to close the cursor
    cursor.close();
    return stats;
  }

  private Stats cursorToStat(Cursor cursor) {
	  Stats stat_new = new Stats();
	  
	  stat_new.setId(cursor.getLong(0));
	  stat_new.setImei(cursor.getString(1));
	  stat_new.setImsi(cursor.getString(2));
	  stat_new.setPhone_model(cursor.getString(3));
	  stat_new.setSimSN(cursor.getString(4));

	  stat_new.setNetwork_mcc(cursor.getString(5));
	  stat_new.setNetwork_mnc(cursor.getString(6));
	  stat_new.setNetwork_name(cursor.getString(7));
	  stat_new.setNetwork_country(cursor.getString(8));
	  stat_new.setNetwork_type(cursor.getString(9));
	  stat_new.setGsm_type(cursor.getString(10));
	  stat_new.setCellid(cursor.getString(11));
	  stat_new.setCellpsc(cursor.getString(12));
	  stat_new.setCelllac(cursor.getString(13));
	  stat_new.setRssi(cursor.getString(14));
	  stat_new.setGps(cursor.getString(15));
	  stat_new.setTimestamp(cursor.getString(16));
	  
	  return stat_new;
  }
} 