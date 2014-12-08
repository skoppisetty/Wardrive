package com.wardrive;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.wardrive.adapter.GatherStats;
import com.wardrive.adapter.Stats;

import android.support.v4.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;


public class Method1Fragment extends Fragment {
	private Context thiscontext;
	private List<Stats> stats_data;
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		thiscontext = container.getContext();
        final View rootView = inflater.inflate(R.layout.fragment_method1, container, false);
        
        display_data(rootView);
        
	Button showStats = (Button)rootView.findViewById(R.id.btn_show_stats);
	showStats.setOnClickListener(new View.OnClickListener() {
        public void onClick(View arg0) {
        	display_data(rootView);
        	
        }
    });
    
    return rootView;
	}
    
    private void display_data(View rootView) {
		// TODO Auto-generated method stub
    	
    	stats_data = GatherStats.get_data(thiscontext);
		ListView lView = (ListView)rootView.findViewById(R.id.list_stats);
    	List<String> data = new ArrayList<String>();
    	
    	Iterator<Stats> iterator = stats_data.iterator();
    	while (iterator.hasNext()) 
    		data.add(iterator.next().toString());
    	
    	ArrayAdapter<String> arrayAdapter =
    			new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, data);

        lView.setAdapter(arrayAdapter); 
	}
    
    
   
	
	
}
