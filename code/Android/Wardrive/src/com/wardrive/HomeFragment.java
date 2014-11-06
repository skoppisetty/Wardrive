package com.wardrive;

import java.util.List;

import android.support.v4.app.Fragment;
import android.telephony.NeighboringCellInfo;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class HomeFragment extends Fragment {
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		TextView textView1;
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        textView1=(TextView)rootView.findViewById(R.id.textView1);  
        
        //Get the instance of TelephonyManager  
        TelephonyManager  tm=(TelephonyManager)getActivity().getSystemService(Context.TELEPHONY_SERVICE);  
         
        //Calling the methods of TelephonyManager the returns the information  
        String IMEINumber=tm.getDeviceId();  
        String subscriberID=tm.getDeviceId();  
        String SIMSerialNumber=tm.getSimSerialNumber();  
        String networkCountryISO=tm.getNetworkCountryIso();  
        String SIMCountryISO=tm.getSimCountryIso();  
        GsmCellLocation CellLocation = (GsmCellLocation)tm.getCellLocation();
        String softwareVersion=tm.getDeviceSoftwareVersion();  
        String voiceMailNumber=tm.getVoiceMailNumber();  
        String networkCountry = tm.getNetworkCountryIso();
        String networkOperatorId = tm.getNetworkOperator();
        String networkName = tm.getNetworkOperatorName();
        
                int cell_ID = CellLocation.getCid();
                int cell_psc= CellLocation.getPsc();
                int cell_lac =CellLocation.getLac();
        

                 
                
                
        //Get the phone type  
        String strphoneType="";  
          
        int phoneType=tm.getPhoneType();  
  
        switch (phoneType)   
        {  
                case (TelephonyManager.PHONE_TYPE_CDMA):  
                           strphoneType="CDMA";  
                               break;  
                case (TelephonyManager.PHONE_TYPE_GSM):   
                           strphoneType="GSM";                
                               break;  
                case (TelephonyManager.PHONE_TYPE_NONE):  
                            strphoneType="NONE";                
                                break;  
         }  
          
        //getting information if phone is in roaming  
        boolean isRoaming=tm.isNetworkRoaming();  
          
        String info="Phone Details:\n";  
        info+="\n IMEI Number:"+IMEINumber;  
        info+="\n SubscriberID:"+subscriberID;  
        info+="\n Sim Serial Number:"+SIMSerialNumber;  
        info+="\n Network Country ISO:"+networkCountryISO;  
        info+="\n SIM Country ISO:"+SIMCountryISO;  
        info+="\n Software Version:"+softwareVersion;  
        info+="\n Voice Mail Number:"+voiceMailNumber;  
        info+="\n Phone Network Type:"+strphoneType;  
        info+="\n In Roaming? :"+isRoaming;  
        info+="\n network Country :"+networkCountry;  
        info+="\n networkOperatorId :"+networkOperatorId;  
        info+="\n networkName :"+networkName;  
        info+="\n CellLocation :"+CellLocation; 
        info+="\n cell_ID :"+cell_ID; 
        info+="\n cell_psc :"+cell_psc; 
        info+="\n cell_lac :"+cell_lac; 
        
        List<NeighboringCellInfo> neighboringCellInfos = tm.getNeighboringCellInfo();
        for(NeighboringCellInfo neighboringCellInfo : neighboringCellInfos)
        {
            neighboringCellInfo.getCid();
            neighboringCellInfo.getLac();
            neighboringCellInfo.getPsc();
            neighboringCellInfo.getNetworkType();
            neighboringCellInfo.getRssi();

            info+="\n"+neighboringCellInfo.getCid()+"\t"+ neighboringCellInfo.getRssi();

        }  
       
        
        
        textView1.setText(info);
        return rootView;
    }
}
