package com.wardrive;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.wardrive.adapter.GatherStats;
import com.wardrive.adapter.Stats;

import android.support.v4.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;


public class Method1Fragment extends Fragment {
	private Context thiscontext;
	private List<Stats> stats_data;
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		thiscontext = container.getContext();
        final View rootView = inflater.inflate(R.layout.fragment_method1, container, false);
        
        display_data(rootView);
    
        // BUTTON TO SHOW THE STATS STORED IN THE DATABASE
        Button showStats = (Button)rootView.findViewById(R.id.btn_show_stats);
        showStats.setOnClickListener(new View.OnClickListener() {
        public void onClick(View arg0) {
        	display_data(rootView);
        	
        }
    });
    
    return rootView;
	}
    
	/*
	 * DISPLAY THE DATA FETCHED FROM THE DATABASE TO THE SCREEN
	 */
    private void display_data(View rootView) {
		// TODO Auto-generated method stub
    	
//    	// GET THE DATA FROM THE DATABASE
//    	stats_data = GatherStats.get_data(thiscontext);
//		ListView lView = (ListView)rootView.findViewById(R.id.list_stats);
//    	List<String> data = new ArrayList<String>();
//    	
//    	// ITERATE AND GET THE RECORDS (DATA) FROM THE DATABASE CURSOR
//    	Iterator<Stats> iterator = stats_data.iterator();
//    	while (iterator.hasNext()) 
//    		data.add(iterator.next().toString());
//    	
//    	// DISPLAY IT IN THE SCREEN IN A LISTVIEW
//    	ArrayAdapter<String> arrayAdapter =
//    			new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, data);
//
//    	// SET THE ADAPTER TO THE LISTVIEW TO DISPLAY THE DATA ON THE SCREEN
//        lView.setAdapter(arrayAdapter); 
//        


        List<Stats> stats_all;
        TableLayout country_table;
        country_table=(TableLayout)rootView.findViewById(R.id.log_table);
        country_table.removeAllViews();
        set_row("TIME","NETWORK","LAC","CELL ID",rootView);
        
        
        stats_all = GatherStats.get_data(thiscontext);
        Iterator<Stats> iterators = stats_all.iterator();
    	while (iterators.hasNext()){
    		Stats s = iterators.next();
    	    set_row(s.getRealTime(),s.getNetwork_name(),s.getCelllac(),s.getCellid(),rootView);
    		
    	}
 
   
   
	}
    
	 private void set_row(String s1,String s2,String s3,String s4, View rootView ) {
	        int dip = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
	                (float) 1, getResources().getDisplayMetrics());
	        TableLayout country_table;
	        TableRow row;
	        TextView t1, t2, t3, t4;
	        
	        country_table=(TableLayout)rootView.findViewById(R.id.log_table);
//	        country_table.removeAllViews();
		 
         row = new TableRow(thiscontext);
         t1 = new TextView(thiscontext);
         t2 = new TextView(thiscontext);
         t3 = new TextView(thiscontext);
         t4 = new TextView(thiscontext);

 		 t1.setText(s1);
 		 t2.setText(s2);
 	     t3.setText(s3);
 	     t4.setText(s4);
         t1.setTextSize(15);
         t2.setTextSize(15);
         t3.setTextSize(15);
         t4.setTextSize(15);
         t1.setTextColor(getResources().getColor(R.color.abc_search_url_text_holo));
         t3.setTextColor(getResources().getColor(R.color.abc_search_url_text_holo));
         
         
         t1.setWidth(120 * dip);
         t2.setWidth(75 * dip);
         t3.setWidth(50 * dip);
         t4.setWidth(75 * dip);
         
         t1.setPadding(10*dip, 0, 0, 0);
         row.addView(t1);
         row.addView(t2);
         row.addView(t3);
         row.addView(t4);
         
         country_table.addView(row, new TableLayout.LayoutParams(
                 LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
	 }
    
   
	
	
}
