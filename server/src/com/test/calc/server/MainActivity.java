package com.test.calc.server;





import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;








import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends Activity {

	EditText portServ;
	Button send;
	TextView addressServ;
	Menu menu;
	FragmenForDialog networkDialog = FragmenForDialog.newInstance(1) ,exitDialog = FragmenForDialog.newInstance(0);
	SoketThread sockthr;
	private BroadcastReceiver receiver;
	
		
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		addressServ= (TextView)findViewById(R.id.adress);
		portServ = (EditText)findViewById(R.id.port);
		send = (Button)findViewById(R.id.startServer);
		
		
		
		
		
		send.setOnClickListener(new View.OnClickListener() {
		
			@Override
			public void onClick(View v) {
			
				if((sockthr==null)||(sockthr.isCancelled())){
			    sockthr = new SoketThread(MainActivity.this);
			    sockthr.execute();
		        }
				else{
					sockthr.cancel(true);
				}
			
			
			}
		
		
		});//setOnClickListener
		
		ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
	    if (cm.getActiveNetworkInfo()== null) {
	    	addressServ.setText("n/a");
	    	networkDialog.show(getFragmentManager(), "netDialog"); 
	    	
	    }
		
		
		
	    
	    
	    
	    
	    receiver = new BroadcastReceiver(){
			
			
			

	        @Override			
			public void onReceive(Context context, Intent intent) {
	        	Log.e("FuckSoket2", "Change network");
	        	boolean isconnected = intent.getExtras().getBoolean(ConnectivityManager.EXTRA_NO_CONNECTIVITY,false);
			 if(isconnected){
				 networkDialog.show(getFragmentManager(), "netDialog");
				 if((sockthr!=null)) {
					 sockthr.cancel(true);
				
								
				 }
				 addressServ.setText("n/a");
			 }
			 else{
				 addressServ.setText(getIpAddress());
				 
				 }
			
			
			 
	        	
			 
				
			}
			
		};
	    
	    
		
	    
		
		
	    registerReceiver(receiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
		
	}//OnCreate

	
	
	
	
	
	
	
	
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		this.menu = menu;
		
		getMenuInflater().inflate(R.menu.main, menu);//add menu item's
		
		return true;
	}
	
	
	
	
	

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) 
		{
	    
	      case R.id.Exit:
	       exitDialog.show(getFragmentManager(), "exit1");//Action for menu `exit` item
		   break;
		
		}
	    
	    return true;
	}
	
	
	
	
	
	@Override
	public void onBackPressed() {
		 exitDialog.show(getFragmentManager(), "exit1"); 
	}
	
	
	
	
	
	
	private String getIpAddress() {
		  String ip = "";
		  try {
		   Enumeration<NetworkInterface> enumNetworkInterfaces = NetworkInterface
		     .getNetworkInterfaces();
		   while (enumNetworkInterfaces.hasMoreElements()) {
		    NetworkInterface networkInterface = enumNetworkInterfaces
		      .nextElement();
		    Enumeration<InetAddress> enumInetAddress = networkInterface
		      .getInetAddresses();
		    while (enumInetAddress.hasMoreElements()) {
		     InetAddress inetAddress = enumInetAddress.nextElement();

		     if (inetAddress.isSiteLocalAddress()) {
		      ip = inetAddress.getHostAddress();
		     }
		     
		    }

		   }

		  } catch (SocketException e) {
		   
		   e.printStackTrace();
		   		  }

		  return ip;
		 }
	
	
	
	

	
}//class MainActivity extends Activity


