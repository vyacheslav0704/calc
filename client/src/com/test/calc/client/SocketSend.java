package com.test.calc.client;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Scanner;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Vibrator;
import android.util.Log;
import android.view.MenuItem;


class SocketSend extends AsyncTask <MainActivity, Void, String> {// Thread for socket 
	Socket s;
	String result;
	MainActivity activity;
		
	
	protected String doInBackground(MainActivity... params) {
		
		activity = params[0];
	    result ="";
		try {
			
			
								
			 	s = new Socket();//new socket				 			 	
				
			 	String adress = activity.addressServ.getText().toString();// adress for socket
			 	
			 	if(adress.length()==0){
			 		adress =activity.addressServ.getHint().toString();
			 	
			 	}
			 	
                String port = activity.portServ.getText().toString();// port for socket
			 	
			 	if(port.length()==0){
			 		
			 		port =activity.portServ.getHint().toString();
			 	
			 	}
			 	
			 	s.connect(new InetSocketAddress(adress,Integer.parseInt(port)),1000*10);//connection timer 10 sec
			 	
			 	
			 	
			 	
			 	InputStream inStream = s.getInputStream();
			 	
			 					 	
			 	OutputStream outStream = s.getOutputStream();
			 	
			 	Log.e("Soket", "Послал на сервер "+activity.vyrag.getText()+ " "+adress+" :"+port);
			 				 	
			 	PrintWriter  out = new PrintWriter(outStream,true);
			 	out.println(activity.vyrag.getText());//send to server exception
			 
			 	Scanner in = new Scanner(inStream);//new scanner for "in data"
			 	result= in.nextLine();
			 	
			 	//Log.e("Soket", s.getInetAddress()+" result "+result );
			 	
			 	inStream.close();
			 	outStream.close();
			 	out.close();
			 	
					  
		
		} 
		
		
        catch (IOException e) {
			result = "Ошибка при работе с сервером. Попробуйте позже.";
		}
		
		finally{
			
		    if(s != null){
		    	
		     try 
		     {
		      s.close();
		     } 
		    
		     catch (final IOException e) {
		           e.printStackTrace();
		     }
		    
		    }
		    
		    return result;
		}
		
		
		
	
	}
	
	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		activity.resultat.setText(result);
		
		activity.mAdapter.add(activity.vyrag.getText()+" = "+result);
		activity.mAdapter.notifyDataSetChanged();
		MenuItem soundMenu = (MenuItem)activity.menu.findItem(R.id.Sound);
		
		if(soundMenu.isChecked()){// if sound enable play music
		 MediaPlayer player = MediaPlayer.create(activity, R.raw.sms);
		 player.start();
		}
		
		
		MenuItem vibro = (MenuItem) activity.menu.findItem(R.id.Vibro);
		
		if(vibro.isChecked()){
			Vibrator vib = (Vibrator) activity.getSystemService(Context.VIBRATOR_SERVICE);
			vib.vibrate(750);
		}
	}

	
	
	
}
