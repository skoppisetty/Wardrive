package com.wardrive;

import java.util.List;

import com.wardrive.adapter.Stats;
import com.wardrive.adapter.StatsDataSource;

import android.support.v4.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

public class Method1Fragment extends Fragment {
	private StatsDataSource datasource;
	private Context thiscontext;
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		thiscontext = container.getContext();
        View rootView = inflater.inflate(R.layout.fragment_method1, container, false);
        datasource = new StatsDataSource(thiscontext);
        datasource.open(); 
//        List<Stats> values = datasource.getAllStats();
//        ArrayAdapter<Stats> adapter = new ArrayAdapter<Stats>(getActivity(),
//                android.R.layout.simple_list_item_1, values);
//        setListAdapter(adapter);
            
        return rootView;
    }
}
