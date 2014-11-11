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
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class HomeFragment extends Fragment {
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		final TextView textView1;
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        textView1=(TextView)rootView.findViewById(R.id.textView1);  
        final String info;
        
        Button refresh = (Button)rootView.findViewById(R.id.btn_refresh);
      //Listening to button event
        refresh.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
            	textView1.setText("Null");
            	try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            	Context context = getActivity().getApplicationContext();
            	int duration = Toast.LENGTH_SHORT;

            	Toast toast = Toast.makeText(context, refresh(), duration);
            	toast.show();
            	textView1.setText(refresh());
            }
        });
        textView1.setText(refresh());
        return rootView;
    }

private String refresh(){
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
    
    String networkType = networkType();
    
    
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
    info+="\n network Type :" + networkType;
    
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
   
	return info;
}

private String networkType() {
    TelephonyManager teleMan = (TelephonyManager)
    		getActivity().getSystemService(Context.TELEPHONY_SERVICE);
    int networkType = teleMan.getNetworkType();
    switch (networkType) {
        case TelephonyManager.NETWORK_TYPE_1xRTT: return "1xRTT";
        case TelephonyManager.NETWORK_TYPE_CDMA: return "CDMA";
        case TelephonyManager.NETWORK_TYPE_EDGE: return "EDGE";
        case TelephonyManager.NETWORK_TYPE_EHRPD: return "eHRPD";
        case TelephonyManager.NETWORK_TYPE_EVDO_0: return "EVDO rev. 0";
        case TelephonyManager.NETWORK_TYPE_EVDO_A: return "EVDO rev. A";
        case TelephonyManager.NETWORK_TYPE_EVDO_B: return "EVDO rev. B";
        case TelephonyManager.NETWORK_TYPE_GPRS: return "GPRS";
        case TelephonyManager.NETWORK_TYPE_HSDPA: return "HSDPA";
        case TelephonyManager.NETWORK_TYPE_HSPA: return "HSPA";
        case TelephonyManager.NETWORK_TYPE_HSPAP: return "HSPA+";
        case TelephonyManager.NETWORK_TYPE_HSUPA: return "HSUPA";
        case TelephonyManager.NETWORK_TYPE_IDEN: return "iDen";
        case TelephonyManager.NETWORK_TYPE_LTE: return "LTE";
        case TelephonyManager.NETWORK_TYPE_UMTS: return "UMTS";
        case TelephonyManager.NETWORK_TYPE_UNKNOWN: return "Unknown";
    }
    throw new RuntimeException("New type of network");
}

}
