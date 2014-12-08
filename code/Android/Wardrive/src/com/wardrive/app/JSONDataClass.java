package com.wardrive.app;


import android.util.Log;

import com.wardrive.app.AppController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import com.wardrive.adapter.GatherStats;
import com.wardrive.adapter.Stats;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONStringer;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.Request.Method;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;



public class JSONDataClass {

	private String TAG = JSONDataClass.class.getSimpleName();
	private boolean success = false;
	
	// MANGANESE SERVER URL
//	public static final String SERVER_URL = "http://192.168.1.115:8000/save_data/";
	public static final String SERVER_URL = "http://manganese.cc.gatech.edu:51234/save_data/";

	// These tags will be used to cancel the requests
	private static String tag_json_req = "json_req";
	
	public boolean IsSuccess()
	{
		return success;
	}
	
	public void makeStringReq(final Stats data){
		 StringRequest sr = new StringRequest(Request.Method.POST,SERVER_URL, new Response.Listener<String>() {
		        @Override
		        public void onResponse(String response) {
		            Log.e("LOG RESPONSE", response.toString());
		            success = true;
		        }
		    }, new Response.ErrorListener() {
		        @Override
		        public void onErrorResponse(VolleyError error) {
//		            Log.e("ERROR LOG", error.getMessage().toString());
		            VolleyLog.e("ERROR", error.getMessage());
		            success = false;
		        }
		    }){
		        @Override
		        protected Map<String,String> getParams(){
		            Map<String,String> params = new HashMap<String, String>();
		            params.put("stats",GatherStats.toJSON(data));
		            return params;
		        }
		 
		        @Override
		        public Map<String, String> getHeaders() throws AuthFailureError {
		            Map<String,String> params = new HashMap<String, String>();
		            params.put("Content-Type","application/x-www-form-urlencoded");
		            return params;
		        }
		    };
		    AppController.getInstance().addToRequestQueue(sr, "ss");
	}
	
		/**
		 * Making JSON object request
		 * */
	public void makeJsonObjReq(final Stats data) {
			
			JsonObjectRequest jsonObjReq =
					new JsonObjectRequest(Method.POST, SERVER_URL, null, new Response.Listener<JSONObject>() {

						@Override
						public void onResponse(JSONObject response) {
							Log.e("LOG RESPONSE", response.toString());
							//msgResponse.setText(response.toString());
							success = true;
						}
					}, new Response.ErrorListener() {

						@Override
						public void onErrorResponse(VolleyError error) {
							Log.e("ERROR LOG", error.getMessage().toString());
							VolleyLog.e("ERROR", error.getMessage());
							success = false;
						}
					}) {

				/**
				 * Passing some request headers
				 * */
				@Override
				public Map<String, String> getHeaders() throws AuthFailureError {
		            Map<String,String> params = new HashMap<String, String>();
		            params.put("Content-Type","application/x-www-form-urlencoded");
		            return params;
				}

				@Override
				protected Map getParams() {
					
					//params.put("stats", data.getCellid());
					//params.put("cell_id", data.getCellid());
					//params.put("lac", data.getCelllac());

			         Map statsReq = new HashMap();
			         HashMap<String, String> statsParams = new HashMap<String, String>();
			         
			         statsParams.put("IMEI", data.getImei());
			         statsParams.put("stats", data.getImsi().toString());
			         
			         statsReq.put("stats", statsParams);
					
					return statsParams;
				}

			};

			Log.e("JSONOBJECT", jsonObjReq.toString());
			
			// Adding request to request queue
			AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_req);

			// Cancelling request
			// ApplicationController.getInstance().getRequestQueue().cancelAll(tag_json_obj);		
		}
	
	
	/**
	 * Making json array request
	 * */
	public void makeJsonArryReq() {
		JsonArrayRequest req = new JsonArrayRequest(SERVER_URL,
				new Response.Listener<JSONArray>() {
					@Override
					public void onResponse(JSONArray response) {
						Log.d(TAG, response.toString());
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						VolleyLog.d(TAG, "Error: " + error.getMessage());
					}
				});

		Log.e("JSONOBJECT", req.toString());
		
		// Adding request to request queue
		AppController.getInstance().addToRequestQueue(req, tag_json_req);

		// Cancelling request
		// ApplicationController.getInstance().getRequestQueue().cancelAll(tag_json_arry);
	}
	
}
