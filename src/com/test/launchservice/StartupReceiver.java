package com.test.launchservice;
import java.io.DataOutputStream;

import android.content.BroadcastReceiver; 
import android.content.Context; 
import android.content.Intent;

public class StartupReceiver extends BroadcastReceiver {   
   
    @Override   
    public void onReceive(Context context, Intent intent) {   
        // TODO Auto-generated method stub    
        Intent i = new Intent(context,LaunchServiceActivity.class);   
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);   
        //將intent以startActivity傳送給作業系統    
        context.startActivity(i);   
   
        
    }   
   
  
   

public static boolean runRootCommand(String command) 
{ 
	Process process = null; 
	DataOutputStream os = null; 
	  try 
	  {  
    	process = Runtime.getRuntime().exec(" su ");  
    	os = new DataOutputStream(process.getOutputStream());  
	    os.writeBytes(command+"\n"); 
	    os.writeBytes("exit\n");  
	    os.flush(); 
	    process.waitFor(); 
	   } catch (Exception e) 
	   {  
        // 	Log.d("*** DEBUG ***", "Unexpected error - Here is what I know: "+e.getMessage()); 
	   return false;  
	   }  
		finally 
		{ 
				try 
				{
						if (os != null) 
						{  
								os.close();  
						}  
						process.destroy(); 
				} 
				catch (Exception e) 
				{ 
					// nothing 
				}  
		}  
		return true;

	}

   
	
}
