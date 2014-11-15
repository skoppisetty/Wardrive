package com.wardrive.adapter;

import java.io.File;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBAdapter extends SQLiteOpenHelper {

	  
  public static final String TABLE_STATS = "statistics";
  public static final String STAT_ID = "STAT_ID";
  public static final String IMEI = "IMEI";
  public static final String IMSI = "IMSI";
  public static final String PHONE_MODEL = "PHONE_MODEL";
  public static final String SIM = "SIM_SN";
  public static final String GSM_TYPE = "GSM_TYPE";
  public static final String NETWORK_MCC = "NETWORK_MCC";
  public static final String NETWORK_MNC = "NETWORK_MNC";
  public static final String NETWORK_NAME = "NETWORK_NAME";
  public static final String NETWORK_COUNTRY = "NETWORK_COUNTRY";
  public static final String NETWORK_TYPE = "NETWORK_TYPE";
  public static final String CELL_ID = "CELL_ID";
  public static final String CELL_PSC = "CELL_PSC";
  public static final String CELL_LAC = "CELL_LAC";
  public static final String RSSI = "RSSI";
  public static final String GPS = "GPS";
  public static final String TIMESTAMP = "UNIX_TIMESTAMP";
  
  private static final String DATABASE_NAME = "stats.db";
  private static final int DATABASE_VERSION = 1;

  // Database creation sql statement
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
      + " text);";

  public DBAdapter(Context context) {
	  // for external
	  super(context, context.getExternalFilesDir(null).getAbsolutePath() + "/" + DATABASE_NAME, null, DATABASE_VERSION);
//	  super(context, DATABASE_NAME, null, DATABASE_VERSION);
  }

  @Override
  public void onCreate(SQLiteDatabase database) {
    database.execSQL(DATABASE_CREATE);
  }

  @Override
  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    Log.w(DBAdapter.class.getName(),
        "Upgrading database from version " + oldVersion + " to "
            + newVersion + ", which will destroy all old data");
    db.execSQL("DROP TABLE IF EXISTS " + TABLE_STATS);
    onCreate(db);
  }

} 