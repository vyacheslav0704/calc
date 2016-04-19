package com.test.calc.client;


import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.method.KeyListener;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SlidingDrawer;
import android.widget.SlidingDrawer.*;
import android.widget.TextView;


public class MainActivity extends Activity {
	
	TextView resultat,historKnop;
	ListView historText;
	Button send;
	EditText addressServ, portServ, vyrag;
	SocketSend sendToServ;
    FragmenForDialog networkDialog ,exitDialog;
    ArrayAdapter<String> mAdapter;
    ArrayList <String> items4Histor;
    Menu menu;
    
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		historText = (ListView)findViewById(R.id.historText);
		historKnop = (TextView)findViewById(R.id.historKnop);//Button  opened history
		resultat = (TextView)findViewById(R.id.result);//result of expression. To the right of expression 
		
		send= (Button)findViewById(R.id.ravno);//Button sending expression 
		
		addressServ= (EditText)findViewById(R.id.adress);
		portServ = (EditText)findViewById(R.id.port);
		vyrag = (EditText)findViewById(R.id.vyrag);//expression
		
		exitDialog = FragmenForDialog.newInstance(0);
		networkDialog = FragmenForDialog.newInstance(1);
		
		
		 		
		ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
				    if (cm.getActiveNetworkInfo()== null) {
				    	networkDialog.show(getFragmentManager(), "netDialog"); 
				    	
				    }
				        
		
						
		
		 SlidingDrawer   slidingdrawer  = ( SlidingDrawer ) findViewById(R.id.drawer);
		 slidingdrawer.setOnDrawerOpenListener(new OnDrawerOpenListener() {
				public void onDrawerOpened() {
					historKnop.setText("История ⇩");//change text, when open  SlidingDrawer. We change arrow.
				}
			});
			
			 slidingdrawer.setOnDrawerCloseListener(new OnDrawerCloseListener() {
				public void onDrawerClosed() {
					historKnop.setText("История ⇧");
				}
			});
		
		
			
			 
			 if ((savedInstanceState!=null)&&(savedInstanceState.containsKey("histrKey")))
			    {
				 items4Histor = savedInstanceState.getStringArrayList("histrKey");
			    }
			    else
			    {
			    	 items4Histor = new ArrayList ();//items for history list 
			    }
			 
			 
			 mAdapter = new ArrayAdapter<String>(this,R.layout.list_item, items4Histor);
			 historText.setAdapter(mAdapter);
			 
			 
			 SwipeDismissListViewTouchListener touchListener = new SwipeDismissListViewTouchListener(historText,new SwipeDismissListViewTouchListener.DismissCallbacks() {
		        //Listener for List history                    
		             		 @Override
		                            public boolean canDismiss(int position) {
		                                return true;
		                            }

		                     
		             		 @Override
		                            public void onDismiss(ListView listView, int[] reverseSortedPositions) {
		                                for (int position : reverseSortedPositions) {
		                                    mAdapter.remove(mAdapter.getItem(position));
		                                }
		                                mAdapter.notifyDataSetChanged();
		                            }
		                        });//histor text
		       
		        historText.setOnTouchListener(touchListener);
			 
		        vyrag.setOnKeyListener(new View.OnKeyListener(){
		        	public boolean onKey(View v, int keyCode, KeyEvent event){	 
		        		
		        		if ((keyCode == KeyEvent.KEYCODE_ENTER))
                        {   
							sendToServ =  new SocketSend();
							sendToServ.execute(MainActivity.this);	
                        }
		        	  
		        	return false;
		        	
		        	  }
		              	});
		        
		          	 
		send.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
			
					
				  	sendToServ =  new SocketSend();
					sendToServ.execute(MainActivity.this);	
			
				
			}
		});
		
		
			
		
	
	}//onCreate

	@Override
	public void onBackPressed() {
		 exitDialog.show(getFragmentManager(), "exit1"); 
	}
	
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) 
	{
	   
	    switch (item.getItemId()) 
		{
	    
	    case R.id.Exit:
	     exitDialog.show(getFragmentManager(), "exit1");//show exit dialog
		break;
		
		
	    case R.id.Sound:
	     item.setChecked(!item.isChecked());	
	    	break;
		
	    
		case R.id.Vibro:
	     item.setChecked(!item.isChecked());	
	    	break;
		}
	    
	    return true;
	}
	
	
	
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		this.menu = menu;
		
		getMenuInflater().inflate(R.menu.main, menu);//add menu item's
		return true;
	}       
	
	
	public void onSaveInstanceState(Bundle outState){//save history list
		super.onSaveInstanceState(outState);
		
		outState.putStringArrayList("histrKey" ,items4Histor);
		
		
	}
	
	
	

}//MainActivity

