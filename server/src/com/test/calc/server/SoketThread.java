package com.test.calc.server;





import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.text.NumberFormat;
import java.util.Iterator;

import expr.Expr;
import expr.Parser;
import android.os.AsyncTask;
import android.util.Log;


public class SoketThread extends AsyncTask <Void, Void, Void>{

	MainActivity activity;
	
	
	public SoketThread(MainActivity activ ){
		activity = activ;	
	}
	
	protected void onPreExecute() {
		
		activity.send.setText("Остановить сервер");// Change text on button start	
		activity.portServ.setEnabled(false);//Blocking server port text field
	
	}
	
	protected void onPostExecute() {
	
		activity.send.setText("Запустить сервер");	
		activity.portServ.setEnabled(true);
		
	}
	
	protected void onCancelled() {
		
		activity.send.setText("Запустить сервер");	
		activity.portServ.setEnabled(true);
		
	}
	
	
	@Override
	protected Void doInBackground(Void... params) {
		
		try {
			
				Selector selector = Selector.open();
				ServerSocketChannel ssc =  ServerSocketChannel.open();
				
				String port;
				if(activity.portServ.getText().toString().length()==0){
					port = activity.portServ.getHint()+"";
					}
				
				else{ port = activity.portServ.getText()+""; }
				
				InetSocketAddress hostAddress = new InetSocketAddress( activity.addressServ.getText().toString(),Integer.parseInt(port));
				
				ssc.configureBlocking(false);
				ssc.register(selector, SelectionKey.OP_ACCEPT);
				ssc.socket().bind(hostAddress);
				ByteBuffer buffer = ByteBuffer.allocate(1024);
			
				Log.e("SoketServer", hostAddress.getAddress()+" : "+hostAddress.getPort());
				
				while(!isCancelled()){
					
					int select = selector.select();
					
					if(select==0) { continue;}
					
					Iterator <SelectionKey> selectedKeys = selector.selectedKeys().iterator();
	               
	                while(selectedKeys.hasNext()){
	                    
	                	SelectionKey k = selectedKeys.next();
	                	selectedKeys.remove();
	                	
	                	if((k.readyOps() & SelectionKey.OP_ACCEPT) == SelectionKey.OP_ACCEPT){
	                        SocketChannel channel =ssc.accept();
	                        channel.configureBlocking(false);
	                        channel.socket().setTcpNoDelay(true);
	                        channel.register(selector, SelectionKey.OP_READ);
	                        
	                	} else
	                            if ((k.readyOps() & SelectionKey.OP_READ) == SelectionKey.OP_READ) {
	                              buffer.clear();
	                              
	                              try{
	                                ((SocketChannel)k.channel()).read(buffer);
	                                buffer.flip();
	                                String inExpression = new String (buffer.array(),buffer.position(),buffer.remaining());
	                                buffer.clear();
	                               
	                                
	                                String result;
	                               try{
	                            	   
	                            	   Expr expr = Parser.parse(inExpression);
	                            	 //  result =NumberFormat.getInstance ().format (expr.value()+"");
	                            	   result =expr.value()+"";
	                               }
	                                catch(Exception e){
	                                	Log.e("SoketParse", e.toString());
	                                	result= e.getMessage();
	                                }
	                               
	                                buffer.put(result.getBytes());
	                                buffer.flip();
	                                ((SocketChannel)k.channel()).write(buffer);
	                                buffer.clear();
	                                k.cancel();
	                                  
	                                  }
	                              
	                              catch(Exception e){ Log.e("Soket 1", e.toString()+" "+e.getMessage());}
	                              
	                           }// if(k.readyOps())
	                        
	                    }//while(selector.hasnext())
				
				    }//while(!isCancelled())
	                
			selector.close();
		    ssc.close();  
		}
				catch (IOException e) {
			
					Log.e("Soket 2", e.toString());
		}
		
	
		
		return null;
	}





}
