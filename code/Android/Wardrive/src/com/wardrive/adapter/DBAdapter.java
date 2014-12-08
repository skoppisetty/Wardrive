package com.wardrive.adapter;

import java.io.File;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBAdapter extends SQLiteOpenHelper {

  // DATABASE TABLE COLUMNS STORED ON THE PHONE	
  public static final String TABLE_STATS = "statistics";					// TABLE NAME
  public static final String STAT_ID = "STAT_ID";							// AUTO-INCREMENTED UNIQUE ID (PRIMARY KEY)
  public static final String IMEI = "IMEI";									// IMEI
  public static final String IMSI = "IMSI";									// IMSI
  public static final String PHONE_MODEL = "PHONE_MODEL";					// PHONE MODEL
  public static final String SIM = "SIM_SN";								// SIM NUMBER
  public static final String GSM_TYPE = "GSM_TYPE";							// GSM TYPE
  public static final String NETWORK_MCC = "NETWORK_MCC";					// MCC
  public static final String NETWORK_MNC = "NETWORK_MNC";					// MNC
  public static final String NETWORK_NAME = "NETWORK_NAME";					// NETWORK OPERATOR NAME
  public static final String NETWORK_COUNTRY = "NETWORK_COUNTRY";			// NETWORK OPERATOR COUNTRY
  public static final String NETWORK_TYPE = "NETWORK_TYPE";					// NETWORK TYPE
  public static final String CELL_ID = "CELL_ID";							// CELL ID
  public static final String CELL_PSC = "CELL_PSC";							// CELL PSC
  public static final String CELL_LAC = "CELL_LAC";							// CELL LAC
  public static final String RSSI = "RSSI";									// RSSI
  public static final String GPS = "GPS";									// USER LOCATION
  public static final String TIMESTAMP = "UNIX_TIMESTAMP";					// TIMESTAMP (UNIX TIME)
  public static final String IS_SYNCED = "IS_SYNCED";						// RECORD PUSHED TO THE SERVER?
  
  private static final String DATABASE_NAME = "stats.db";					// DATABASE NAME
  private static final int DATABASE_VERSION = 1;							// DATABASE VERSION

  // Database creation SQL statement
  private static final String DATABASE_CREATE = "create table "
      + TABLE_STATS + "(" + STAT_ID
      + " integer primary key autoincrement, " + IMEI
      + " text,"  + IMSI
      + " text,"  + PHONE_MODEL
      + " text,"  + SIM
      + " text,"  + GSM_TYPE
      + " text, " + NETWORK_MCC
      + " text, " + NETWORK_MNC
      + " text, " + NETWORK_NAME
      + " text, " + NETWORK_COUNTRY
      + " text, " + NETWORK_TYPE
      + " text, " + CELL_ID
      + " text, " + CELL_PSC
      + " text, " + CELL_LAC
      + " text, " + RSSI
      + " text, " + GPS
      + " text, " + TIMESTAMP
      + " text, " + IS_SYNCED
      + " integer);";

  // DATABASE ADAPTER
  public DBAdapter(Context context) {
	  // for external
//	  super(context, context.getExternalFilesDir(null).getAbsolutePath() + "/" + DATABASE_NAME, null, DATABASE_VERSION);
	  super(context, DATABASE_NAME, null, DATABASE_VERSION);
  }

  @Override
  public void onCreate(SQLiteDatabase database) {
    database.execSQL(DATABASE_CREATE);
  }

  /*
   * (non-Javadoc)
   * @see android.database.sqlite.SQLiteOpenHelper#onUpgrade(android.database.sqlite.SQLiteDatabase, int, int)
   * UPGRADING TO A NEW DATABASE VERSION
   */
  @Override
  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    Log.w(DBAdapter.class.getName(),
        "Upgrading database from version " + oldVersion + " to "
            + newVersion + ", which will destroy all old data");
    db.execSQL("DROP TABLE IF EXISTS " + TABLE_STATS);
    onCreate(db);
  }

} 