package com.wardrive;


import com.wardrive.app.AppController;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.Request.Method;
import com.android.volley.toolbox.JsonObjectRequest;

import android.support.v4.app.Fragment;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;


public class Method2Fragment extends Fragment implements OnClickListener {

	private String TAG = Method2Fragment.class.getSimpleName();
	private Button btnJsonObj;
	//private TextView msgResponse;
	public static final String SERVER_URL = "http://104.236.14.26/";
	//public static final String SERVER_URL = "http://www.google.com";
	//public static final String SERVER_URL = "http://api.androidhive.info/volley/person_object.json";
	
	// These tags will be used to cancel the requests
	private String tag_json_req = "json_req";
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		
        final View rootView = inflater.inflate(R.layout.fragment_method2, container, false);
        
        //btnJsonObj = (Button) rootView.findViewById(R.id.btn_request);
        //btnJsonObj.setOnClickListener(this);
        
        return rootView;
    }

/*		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.fragment_method2);

			btnJsonObj = (Button) findViewById(R.id.btn_request);
			//btnJsonArray = (Button) findViewById(R.id.btnJsonArray);
			//msgResponse = (TextView) findViewById(R.id.msgResponse);

			btnJsonObj.setOnClickListener(this);
			//btnJsonArray.setOnClickListener(this);
		}*/
		

		/**
		 * Making JSON object request
		 * */
		private void makeJsonObjReq() {
			
			JsonObjectRequest jsonObjReq =
					new JsonObjectRequest(Method.GET, SERVER_URL, null, new Response.Listener<JSONObject>() {

						@Override
						public void onResponse(JSONObject response) {
							Log.e("LOG RESPONSE", response.toString());
							//msgResponse.setText(response.toString());
						}
					}, new Response.ErrorListener() {

						@Override
						public void onErrorResponse(VolleyError error) {
							Log.e("ERROR LOG", error.getMessage().toString());
							VolleyLog.e("ERROR", error.getMessage());
						}
					}) {

				/**
				 * Passing some request headers
				 * */
				@Override
				public Map<String, String> getHeaders() throws AuthFailureError {
					HashMap<String, String> headers = new HashMap<String, String>();
					headers.put("Content-Type", "application/json");
					return headers;
				}

				//@Override
				protected Map<String, String> getParams() {
					Map<String, String> params = new HashMap<String, String>();
					
					params.put("name", "Androidhive");
					params.put("email", "abc@androidhive.info");
					params.put("pass", "password123");

					return params;
				}

			};

			// Adding request to request queue
			AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_req);

			// Cancelling request
			// ApplicationController.getInstance().getRequestQueue().cancelAll(tag_json_obj);		
		}

		@Override
		public void onClick(View v) {
				makeJsonObjReq();
			}

	
}
