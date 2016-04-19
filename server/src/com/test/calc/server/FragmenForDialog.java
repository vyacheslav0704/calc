package com.test.calc.server;





import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Process;
import android.provider.Settings;

public class FragmenForDialog extends DialogFragment {
  
	
	public static  FragmenForDialog   newInstance (int type){
		
		FragmenForDialog frag = new FragmenForDialog();
		Bundle args = new Bundle();
		args.putInt("type",type);
		frag.setArguments(args);
		
		return frag;
	}
	
	
	

	 
		 @Override
		 
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		int type = getArguments().getInt("type");
		
				
		AlertDialog.Builder  dialog= new AlertDialog.Builder(getActivity());
		
	
		
		
		switch (type){
			
		   case 0://type 0 - exit-dialog 
		
		
		     dialog.setTitle("Вы действительно желаете выйти?");// Text for exit-dialog
			  
			
			  
			 dialog.setPositiveButton("Да", new DialogInterface.OnClickListener() {//Exit action for button yes
				
				
			 public void onClick(DialogInterface dialog, int which) {

				// ((MainActivity)((Fragment) dialog).getActivity()).finish();
				 Process.killProcess( Process.myPid() );//kill application	
				 }
			});
			 
			 dialog.setNegativeButton("Нет",  new DialogInterface.OnClickListener() {
					
					
					public void onClick(DialogInterface dialog, int which) {
					((Dialog) dialog).hide(); //hide dialog, not dismiss, for to use later. 
						
					}
				} );
			 
			
			break;
			
			
			
	        case 1://type 1 - network
			  dialog.setMessage("Для работы программы требуется соединение с интернетом, которого сейчас нет. Установите его.");// Text for network-dialog
			  
			  dialog.setPositiveButton("ОК", new DialogInterface.OnClickListener() {// Open network settings why yes
					
					 @Override
					 public void onClick(DialogInterface dialog, int which) {

						 Intent intent = new Intent (Settings.ACTION_WIRELESS_SETTINGS);
						 startActivity(intent);
					}
				});
			  
			  break;  
		}
		
		
		
		
		
		
		Dialog dialogWindow = dialog.create();
		
		setCancelable(true);//dialog not cancel on button back
		dialogWindow.setCanceledOnTouchOutside(false);// to cancel outside touch

		((AlertDialog) dialogWindow).setIcon(R.drawable.ic_menu_exit_dark);//icon for dialog
		
		return  dialogWindow;
		
	}//OnCreateDialog

	

	
}//class FragmenForDialog
