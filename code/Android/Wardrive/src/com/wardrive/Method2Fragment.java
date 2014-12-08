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
	
	// THE MANGANESE SERVER URL
	public static final String SERVER_URL = "http://104.236.14.26/";
	
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

		@Override
		public void onClick(View v) {
				//makeJsonObjReq();
			}

	
}
