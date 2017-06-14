package com.test.launchservice;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import android.app.Instrumentation;
import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.inputmethodservice.InputMethodService;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
//import android.os.ServiceManager;
import android.os.SystemClock;
import android.util.Log;
//import android.view.IWindowManager;
import android.view.InputDevice;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.inputmethod.InputConnection;
import android.widget.ImageView;
import android.widget.Toast;

public class cmdService extends Service 
{
	//String savePath = FILE_PATH;
	int bufferSize = 8192;
	byte[] buf = new byte[bufferSize];
	int passedlen = 0;
	long len = 0;
		
	ServerSocket ss=null;
	private static final int HOST_PORT = 6060;
	private static final int HTTP_SERVICE_PORT = 6066;
	private MyHandler mHandler = null;  
	Socket s = null;
//	private static final String FILE_PATH = "/sdcard/";
	FileOutputStream fos = null;
	private Thread background; 
	Intent si;
	private Timer timer_y, timer_z, timer_cap;
	boolean  wait_flag = true;
	int xx;
	int yy;
	 
	
@Override
public IBinder onBind(Intent intent) {
	return null;
}

@Override
public void onCreate() {
	
	
	 try {
		new NanoHTTPD( HTTP_SERVICE_PORT  , new File("/sdcard/") );
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
	
	/*
	if( ss == null )
	{
    	try {
			ss = new ServerSocket(HOST_PORT);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	*/

}	
	
@Override
public void onDestroy() {
	return;
}

//============================================================================
Timer timer = new Timer();  
int timer_sw =1;

private Handler handler = new Handler()  
{  
    @Override  
    public void handleMessage(Message msg)  
    {  
    	//iv.setImageBitmap(bm); 
    	//linearlayout.invalidate();  
    	
        super.handleMessage(msg);  
    }  
};

@Override
public void onStart(Intent intent, int startid) 
{
	si = intent;
	     
	 String msgs = null;
	 Looper curLooper = Looper.myLooper();  
	 Looper mainLooper = Looper.getMainLooper();  
	  String msgStr;  
	   
	  if (curLooper == null) {  
	                 mHandler = new MyHandler(mainLooper);  
	                 msgStr = "curLooper is null";  
	  } else {  
	                 mHandler = new MyHandler(curLooper);  
	                 msgStr = "This is curLooper";  
	  }  
	
	  
	  
	  timer.scheduleAtFixedRate(new TimerTask()  
      {   
          @Override  
          public void run()  
          {  
              // TODO Auto-generated method stub  
        	  Run_Screen_Capture_activity();
        /*	  try { 
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
  		   	  Log.d("=======333======","======444=========");
  		   	*/  
  		   	  if(   timer_sw == 1)
  		   	  {
  		   		renameFile("/sdcard/DCIM/","fb0.jpg","fb1.jpg");
  		   			timer_sw = 2;
  		   	  }
  		   	  else
  		   	  {
  		   		renameFile("/sdcard/DCIM/","fb0.jpg","fb2.jpg");
  		   			timer_sw = 1;  
  		   	  }
  		     	
          }   
      }, 0, 5000);  
	  
	  /*
	  //-------------- 定時 5 秒, 截圖程式 ---------------------
	    timer_cap = new Timer(); 	
		TimerTask task_capture = new TimerTask(){ 
		public void run(){
		    Run_Screen_Capture_activity();
			Log.d("=======111======","======2222=========");

		} };
		timer_cap.schedule( task_capture, 2000);  
		//--------------------------------------------------
	   */
	  
	  background = new Thread() {
		  
		  private boolean watchdog_2 = true;
		  
          public void run() {	
        	  
        	
      		
      		
		while(true)
		{
			
				    timer_y = new Timer(); 	
					TimerTask task_exit = new TimerTask(){ 
	      			public void run(){
    				
	      				if( watchdog_2 ==true )
	      				{
	      					Message msg2 = mHandler.obtainMessage(0, 1, 1, "2");
	      					mHandler.handleMessage(msg2);
	      				}
	      			} };
			
			
			try {
					if( ss == null )
					{
				    	try {
							ss = new ServerSocket(HOST_PORT);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							if( ss != null )
							{	
								ss.close();  
								ss=null; 
							}
							if( ss == null )
							{
							
				
								runRootCommand( " kill -9 $(ps |busybox grep 'com.test.launchservice' |busybox awk '{print $2}')");
							}
							System.exit(0);
						}
					} 
					
					
					//ss.setSoTimeout( 20000);
					//s = ss.accept();
					//watchdog_2 = true;
					//s.setSoTimeout( 13000 );
					//timer_y.schedule( task_exit, 10000);  // 設定timer防當機
					
					
				
					// 以下是 debug 用, 加長時間
					ss.setSoTimeout( 60000);
					s = ss.accept();
					watchdog_2 = true;
					s.setSoTimeout( 60000 );
					timer_y.schedule( task_exit, 60000);  // 設定timer防當機
				
					
					
					
					if( getMessage() == true )
					{
					}
					
					timer_y.cancel();  // 沒有當機, 所以取消timer
					timer_y.purge(); 
	
						if( s != null )
						{	s.close();  s=null;  }
					
						if( ss != null )
						{	ss.close();  ss=null;  }
		
		
				} catch (IOException e) {
					// TODO Auto-generated catch block
  					e.printStackTrace();
 					try {
 						
 						if( s != null )
 						{	s.close();  s=null;  }
						
 						if( ss != null )
 						{	ss.close();  ss=null;  }
 											
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					
		//			msgs =  e.getMessage();//    .getCause().getMessage();
				}
		
		} //while
		
		
		
          }  // run()
	  }; //new Thread()
	  background.start();
    
     
    
    
	//return;
	
}


class MyHandler extends Handler {  
    // 指定Looper对象来构造Handler对象，而我们平时直接使用的Handler无参构造方法实际上默认是本线程的looper，可通过查看SDk源代码了解。  
        public MyHandler(Looper looper) {  
            super(looper);  
       }  
  
        @Override  
        public void handleMessage(Message msg) {  
            switch (msg.what) {  
            case 0:
                	System.exit(0);
                	break;
            case 1:  
              //  tv.setText(String.valueOf(msg.obj));  
         //   	ImageView jpgView = (ImageView)findViewById(R.id.imageView1);
		 //   	Bitmap bm = BitmapFactory.decodeFile("/sdcard/fb0.jpg");
	     //			 jpgView.setImageBitmap(bm);	
     	runRootCommand("am start -a android.intent.action.VIEW -d file:///sdcard/fb0.jpg -t image/*");
                break;  
  
            }  
        }  
    }  
//------------------------------------------------------------------------------------
private boolean getMessage()  {
	if (ss == null)
		return false;
	
	 DataInputStream inputStream = null;
	try {
		inputStream = new DataInputStream(  
		         new BufferedInputStream(s.getInputStream()));
	} catch (IOException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
			if( s != null )
			{	try {
				s.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}  s=null;  }
		
			if( ss != null )
			{	try {
				ss.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}  ss=null;  }	
		return false;
	}  

	
	
	timer_z = new Timer(); 	
	TimerTask task_cmdin = new TimerTask(){ 
		public void run(){
	
			wait_flag = false;
			
			timer_z.cancel();  // 取消timer
			timer_z.purge(); 
			
				//Message msg2 = mHandler.obtainMessage(0, 1, 1, "2");
				// mHandler.handleMessage(msg2);
			
		} };
		
	
	try {
		   String  launch_command = null, cmd0 , cmd1 , cmd2, cmd3;
		//int bufferSize = 8192;
		 
		 wait_flag = true;
		 timer_z.schedule( task_cmdin, 100000);  // 設定timer
		   
		//  int counts;
		  
		//  counts = 10;
		  
		while( wait_flag )   //用來連續收資料, 有Timer 防當機
		{
			int len = inputStream.available();
			if( len == 0)
			{
		//		Log.d("3.5 ", "len=0, Sleep 300ms");	
				
					
						//background.sleep(300);
				//		android.os.SystemClock.sleep(1000);
                      
					
				
				wait_flag = false;
				timer_z.cancel();  // 取消timer
				timer_z.purge(); 	
			
				//   len = inputStream.available();	
			}
			else  if( len == -1)
			{
				//try {
					//background.sleep(100);
				//	android.os.SystemClock.sleep(1000);

                 /*
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				*/
			}
			
			
		//	Log.d("3.6 ", "len="+len);	
		  // if( len < 3 ) return false;    //以免阻塞
   
	if( len >=  3 )
	{
		   launch_command= inputStream.readUTF();  
		   
	   do{   
		   cmd0 = launch_command.substring(0, 1);
		   
		   if( cmd0.matches( ">" ))
		   {
			   cmd1 = launch_command.substring(1, 2);  
			   launch_command = launch_command.substring(2, launch_command.length());
			   if( launch_command.matches( "" )) launch_command = null;
				   
			   if( cmd1.matches( "#" )) 
		        { 	
		           // MeLE TV-BOX  IR remote : Back返回
		        	IR_Remote_Back();
		        }
			   if( cmd1.matches( "~" )) 
		        { 
				   // 截屏, SAVE to /sdcard/DCIM/fb0.jpg
				  // runRootCommand("am startservice -n tw.edu.nccu.vipl.toImage//tw.edu.nccu.vipl.toImage.CaptureServ");
				   Run_Screen_Capture_activity();
		        }
		        else if( cmd1.matches( "U" ))  // IR 遙控器的 上, 下, 左, 右
		           	IR_Remote_Cursor_Up();
		        else if( cmd1.matches( "D" )) 
		          	IR_Remote_Cursor_Down();
		        else if( cmd1.matches( "L" )) 
		        	IR_Remote_Cursor_Left();
		        else if( cmd1.matches( "R" )) 
		            IR_Remote_Cursor_Right();
		        	
		        	//for(int k=0;k<500;k++)
		        	//	IR_Remote_Mouse_Right_10step();	
		        	
		        	//IR_Remote_Mouse_Location( 700, 900 );
		        	//IR_Remote_Mouse_LeftButton();
		         
		   }
		   else if( cmd0.matches( "$" ))
		   {
			   IR_Remote_Mouse_LeftButton();      // 按下左鍵
			   inputStream.reset();
		   }
		   else if( cmd0.matches( "@" ))
		   {
			   cmd1 = launch_command.substring(1, launch_command.length()); 
			   int n1 = cmd1.indexOf(",");
			   cmd2 = cmd1.substring( 0, n1 );
			   cmd3 = cmd1.substring( n1+1, cmd1.length()-1);
			   
			   final int x = Integer.parseInt(cmd2);
			   final int y = Integer.parseInt(cmd3);
			   xx = x; 
			   yy = y;
			  IR_Remote_Mouse_Location( x, y );  // mouse 定位
			 //  IR_Remote_Mouse_LeftButton();      // 按下左鍵
			   
				   

				   	
			 //  int bufsize = 4096;
			 //  byte[] buf = new byte[bufsize];
			   inputStream.reset();
			  //  inputStream.readFully(buf);
		   }
        else
        {
        
        //	runRootCommand( launch_command );
        	
        	String pkg_name,class_name;
        	int  i;
        	
        	i = launch_command.indexOf("/");
        	pkg_name = launch_command.substring(0, i );
        	class_name = launch_command.substring( i+1, launch_command.length() );
        	
              
		  		 String className = "com.evernote.skitch.app.SkitchShareActivity";  
			     Intent internet = new Intent(Intent.ACTION_VIEW); 
			     internet.addCategory(Intent.CATEGORY_LAUNCHER);  
			     internet.setClassName( pkg_name,class_name );  
			     internet.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_SINGLE_TOP);
			     startActivity(internet); 
           
			launch_command = null;
	       
        } // else   
	 }while( launch_command != null );
	   } // if ( len >=  3 )
  
	
		} // while(.... )
		
       //  if( fos.getFD().valid() )
        //    fos.close();
	} catch (IOException e) {
		e.printStackTrace();
		
		return false;
	}
	
	
	return true;
}
//------------------------------------------------------------------------------------

public static boolean runRootCommand(String command) 
{ 
	Process process = null; 
	DataOutputStream os = null; 
	  try 
	  {  
    	process = Runtime.getRuntime().exec("su ");  
    	os = new DataOutputStream(process.getOutputStream());  
    //	os.writeBytes("su -c\n"); 
	    os.writeBytes(command+"\n"); 
	  //  os.flush(); 
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

	}  //runRootCommand(

//------------------------------------------------------------------------------------
public static void simulateKeystroke(final int KeyCode) { 
	 

    new Thread(new Runnable() {
        
        public void run() {
            // TODO Auto-generated method stub
            try {
                
                Instrumentation inst=new Instrumentation();
                inst.sendKeyDownUpSync(KeyCode);
            } catch (Exception e) {
                // TODO: handle exception
            }
        }
    }).start();
}
 //------------------------------------------------------------------------------------
private void simulateKeystroke_not_work(int KeyCode) {
	
	//KeyEvent k1 = new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_BACK);
	//KeyEvent k2 = new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_BACK);
	
	InputMethodService isvc = new InputMethodService();
	InputConnection ic = isvc.getCurrentInputConnection();

		
	long eventTime = SystemClock.uptimeMillis();
	
	ic.sendKeyEvent(new KeyEvent(eventTime, eventTime,
	    KeyEvent.ACTION_DOWN, KeyCode, 0, 0, 0, 0,
	    KeyEvent.FLAG_SOFT_KEYBOARD|KeyEvent.FLAG_KEEP_TOUCH_MODE));
	
	  try{

	        Thread.sleep(50);

	   } catch (InterruptedException exc) {

	   }
	  
	ic.sendKeyEvent(new KeyEvent(SystemClock.uptimeMillis(), eventTime,
	    KeyEvent.ACTION_UP, KeyCode, 0, 0, 0, 0,
	    KeyEvent.FLAG_SOFT_KEYBOARD|KeyEvent.FLAG_KEEP_TOUCH_MODE));
	                                                                 
    //  isvc.getCurrentInputConnection().sendKeyEvent(
     //          new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_HOME));
//	isvc.getCurrentInputConnection().sendKeyEvent(k1);
//	isvc.getCurrentInputConnection().sendKeyEvent(k2);
	//isvc.getCurrentInputConnection().sendKeyEvent(
	//	               new KeyEvent(KeyEvent.ACTION_DOWN, KeyCode));

	
	// boolean sendKeyEvent = InputMethodService.getCurrentInputConnection().sendKeyEvent(
     //     new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_BACK));

	
	//getCurrentInputConnection().sendKeyEvent(
	//        new KeyEvent(KeyEvent.ACTION_DOWN, KeyCode));
	/*
    try{

        Thread.sleep(50);

   } catch (InterruptedException exc) {

   }
  */
	
 //   getCurrentInputConnection().sendKeyEvent(
  //          new KeyEvent(KeyEvent.ACTION_UP, KeyCode));
  //  isvc.getCurrentInputConnection().sendKeyEvent(
   //         new KeyEvent(KeyEvent.ACTION_UP, KeyCode));
    
}

/*

	private void simulateKeystroke_old(int KeyCode) {
		
	    doInjectKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyCode));
	    try{

            Thread.sleep(50);

       } catch (InterruptedException exc) {

       }

	    doInjectKeyEvent(new KeyEvent(KeyEvent.ACTION_UP, KeyCode));
	}

	private void doInjectKeyEvent(KeyEvent kEvent) {
        
		final IWindowManager windowManager = IWindowManager.Stub.asInterface(ServiceManager.getService("window"));
		
        try {
             windowManager.injectKeyEvent(kEvent, true);
                                 
        } catch (Exception e) {
             e.printStackTrace();
        }
        
    }
*/

	//------------------------------------------------------------------------------------
	   public void IR_Remote_Back()
	   {
		   //  MeLE TV-BOX  IR remote : Back返回 , 可能為 event2 or event3 不一定
	       runRootCommand( "sendevent /dev/input/event2 1 79 1");
	       runRootCommand( "sendevent /dev/input/event2 0 0 0");
	       runRootCommand( "sendevent /dev/input/event2 1 79 2");
	       runRootCommand( "sendevent /dev/input/event2 0 0 0");
	      runRootCommand( "sendevent /dev/input/event2 1 79 0");
	      runRootCommand( "sendevent /dev/input/event2 0 0 0");
	      	
	       runRootCommand( "sendevent /dev/input/event3 1 79 1");
	       runRootCommand( "sendevent /dev/input/event3 0 0 0");
	       runRootCommand( "sendevent /dev/input/event3 1 79 2");
	       runRootCommand( "sendevent /dev/input/event3 0 0 0");
	       runRootCommand( "sendevent /dev/input/event3 1 79 0");
	       runRootCommand( "sendevent /dev/input/event3 0 0 0");
	    }
	  
	   public void IR_Remote_Pause_Play()
	   {
		   //  MeLE TV-BOX  IR remote : Pause暫停/Play繼續播放 , 可能為 event2 or event3 不一定
	       runRootCommand( "sendevent /dev/input/event2 1 80 1");
	       runRootCommand( "sendevent /dev/input/event2 0 0 0");
	       runRootCommand( "sendevent /dev/input/event2 1 80 2");
	       runRootCommand( "sendevent /dev/input/event2 0 0 1");
	       runRootCommand( "sendevent /dev/input/event2 1 80 0");
	       runRootCommand( "sendevent /dev/input/event2 0 0 0");
		   
	       runRootCommand( "sendevent /dev/input/event3 1 80 1");
	       runRootCommand( "sendevent /dev/input/event3 0 0 0");
	       runRootCommand( "sendevent /dev/input/event3 1 80 2");
	       runRootCommand( "sendevent /dev/input/event3 0 0 1");
	       runRootCommand( "sendevent /dev/input/event3 1 80 0");
	       runRootCommand( "sendevent /dev/input/event3 0 0 0");
	       
	   }
	   
	   public void IR_Remote_Mouse_LeftButton()
	   {
		   //  MeLE TV-BOX  mouse:left key 左鍵
	        runRootCommand( "sendevent /dev/input/event2 4 4 589825");
	       	runRootCommand( "sendevent /dev/input/event2 1 272 1");
	       	runRootCommand( "sendevent /dev/input/event2 0 0 0");
	       	runRootCommand( "sendevent /dev/input/event2 4 4 589825");
	       	runRootCommand( "sendevent /dev/input/event2 1 272 0");
	       	runRootCommand( "sendevent /dev/input/event2 0 0 0");
	       	
	        runRootCommand( "sendevent /dev/input/event3 4 4 589825");
	       	runRootCommand( "sendevent /dev/input/event3 1 272 1");
	       	runRootCommand( "sendevent /dev/input/event3 0 0 0");
	       	runRootCommand( "sendevent /dev/input/event3 4 4 589825");
	       	runRootCommand( "sendevent /dev/input/event3 1 272 0");
	       	runRootCommand( "sendevent /dev/input/event3 0 0 0");
	   }
	
	   public void IR_Remote_Mouse_RightButton()
	   {
			//  MeLE TV-BOX  mouse:left key 右鍵 , 可能是 event2 or event3 不一定
	        runRootCommand( "sendevent /dev/input/event2 4 4 589826");
	       	runRootCommand( "sendevent /dev/input/event2 1 273 1");
	       	runRootCommand( "sendevent /dev/input/event2 0 0 0");
	       	runRootCommand( "sendevent /dev/input/event2 4 4 589826");
	       	runRootCommand( "sendevent /dev/input/event2 1 273 0");
	       	runRootCommand( "sendevent /dev/input/event2 0 0 0");
	        
	        runRootCommand( "sendevent /dev/input/event3 4 4 589826");
	       	runRootCommand( "sendevent /dev/input/event3 1 273 1");
	       	runRootCommand( "sendevent /dev/input/event3 0 0 0");
	       	runRootCommand( "sendevent /dev/input/event3 4 4 589826");
	       	runRootCommand( "sendevent /dev/input/event3 1 273 0");
	       	runRootCommand( "sendevent /dev/input/event3 0 0 0");
	   }
	   
	   public void IR_Remote_Cursor_Up()
	   {
		    runRootCommand( "sendevent /dev/input/event2 1 67 1");
	       	runRootCommand( "sendevent /dev/input/event2 0 0 0");
	      // 	runRootCommand( "sendevent /dev/input/event2 1 67 2");
	      // 	runRootCommand( "sendevent /dev/input/event2 0 0 1");
	       	runRootCommand( "sendevent /dev/input/event2 1 67 0");
	       	runRootCommand( "sendevent /dev/input/event2 0 0 0");
	       	
	        runRootCommand( "sendevent /dev/input/event3 1 67 1");
	       	runRootCommand( "sendevent /dev/input/event3 0 0 0");
	     //  	runRootCommand( "sendevent /dev/input/event3 1 67 2");
	     //  	runRootCommand( "sendevent /dev/input/event3 0 0 1");
	       	runRootCommand( "sendevent /dev/input/event3 1 67 0");
	       	runRootCommand( "sendevent /dev/input/event3 0 0 0");
	   }
	   
	   public void IR_Remote_Cursor_Down()
	   {
		    runRootCommand( "sendevent /dev/input/event2 1 10 1");
	       	runRootCommand( "sendevent /dev/input/event2 0 0 0");
	       	runRootCommand( "sendevent /dev/input/event2 1 10 0");
	       	runRootCommand( "sendevent /dev/input/event2 0 0 0");
	       	
	        runRootCommand( "sendevent /dev/input/event3 1 10 1");
	       	runRootCommand( "sendevent /dev/input/event3 0 0 0");
	       	runRootCommand( "sendevent /dev/input/event3 1 10 0");
	       	runRootCommand( "sendevent /dev/input/event3 0 0 0");
	   }
	   
	   public void IR_Remote_Cursor_Left()
	   {
		    runRootCommand( "sendevent /dev/input/event2 1 6 1");
	       	runRootCommand( "sendevent /dev/input/event2 0 0 0");
	       	runRootCommand( "sendevent /dev/input/event2 1 6 0");
	       	runRootCommand( "sendevent /dev/input/event2 0 0 0");
	       	
	        runRootCommand( "sendevent /dev/input/event3 1 6 1");
	       	runRootCommand( "sendevent /dev/input/event3 0 0 0");
	       	runRootCommand( "sendevent /dev/input/event3 1 6 0");
	       	runRootCommand( "sendevent /dev/input/event3 0 0 0");
	   }
	   
	   public void IR_Remote_Cursor_Right()
	   {
		    runRootCommand( "sendevent /dev/input/event2 1 14 1");
	       	runRootCommand( "sendevent /dev/input/event2 0 0 0");
	       	runRootCommand( "sendevent /dev/input/event2 1 14 0");
	       	runRootCommand( "sendevent /dev/input/event2 0 0 0");
	       	
	        runRootCommand( "sendevent /dev/input/event3 1 14 1");
	       	runRootCommand( "sendevent /dev/input/event3 0 0 0");
	       	runRootCommand( "sendevent /dev/input/event3 1 14 0");
	       	runRootCommand( "sendevent /dev/input/event3 0 0 0");
	   }
	   
	   public void IR_Remote_Mouse_Right_1step()
	   {
		    runRootCommand( "sendevent /dev/input/event2 2 0 1");
	       	runRootCommand( "sendevent /dev/input/event2 0 0 0");
	       	       	
	       	runRootCommand( "sendevent /dev/input/event3 2 0 1");
	       	runRootCommand( "sendevent /dev/input/event3 0 0 0");
	   }
	   
	   public void IR_Remote_Mouse_Left_1step()
	   {
		    runRootCommand( "sendevent /dev/input/event2 2 0 -1");
	       	runRootCommand( "sendevent /dev/input/event2 0 0 0");
	       	       	
	       	runRootCommand( "sendevent /dev/input/event3 2 0 -1");
	       	runRootCommand( "sendevent /dev/input/event3 0 0 0");
	   }
	   
	   public void IR_Remote_Mouse_Up_1step()
	   {
		    runRootCommand( "sendevent /dev/input/event2 2 1 -1");
	       	runRootCommand( "sendevent /dev/input/event2 0 0 0");
	       	       	
	       	runRootCommand( "sendevent /dev/input/event3 2 1 -1");
	       	runRootCommand( "sendevent /dev/input/event3 0 0 0");
	   }
	   
	   public void IR_Remote_Mouse_Down_1step()
	   {
		    runRootCommand( "sendevent /dev/input/event2 2 1 1");
	       	runRootCommand( "sendevent /dev/input/event2 0 0 0");
	       	       	
	       	runRootCommand( "sendevent /dev/input/event3 2 1 1");
	       	runRootCommand( "sendevent /dev/input/event3 0 0 0");
	   }
	   
	   public void IR_Remote_Mouse_Right_10step()
	   {
		    runRootCommand( "sendevent /dev/input/event2 2 0 10");
	       	runRootCommand( "sendevent /dev/input/event2 0 0 0");
	       	       	
	     //  	runRootCommand( "sendevent /dev/input/event3 2 0 10");
	     //  	runRootCommand( "sendevent /dev/input/event3 0 0 0");
	   }
	   
	   public void IR_Remote_Mouse_Location( int x, int y )
	   {
		   int x2 = x *2;
		   int y2 = y *2;
		   runRootCommand( "sendevent /dev/input/event2 2 0 -3000");
	       runRootCommand( "sendevent /dev/input/event2 0 0 0");
		   
	       runRootCommand( "sendevent /dev/input/event2 2 1 -3000");
	       runRootCommand( "sendevent /dev/input/event2 0 0 0");
	       
	       runRootCommand( "sendevent /dev/input/event2 2 0 " + x2);
	       runRootCommand( "sendevent /dev/input/event2 0 0 0");
		   
	       runRootCommand( "sendevent /dev/input/event2 2 1 " + y2);
	       runRootCommand( "sendevent /dev/input/event2 0 0 0");
	   }
	   
	   public void Run_Screen_Capture_activity()
	   {    // 截屏, SAVE to /sdcard/DCIM/fb0.jpg
		     String pn = "tw.edu.nccu.vipl.toImage";  
		     String cn = "tw.edu.nccu.vipl.toImage.CaptureServ";  
		     Intent it = new Intent(Intent.ACTION_VIEW); 
		     it.addCategory(Intent.CATEGORY_LAUNCHER);  
		     it.setClassName(pn, cn);  
		     it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); //|Intent.FLAG_ACTIVITY_SINGLE_TOP);
		     startService(it);
	   }
	   
	   /** 文件重命名 
	   * @param path 文件目錄 
	   * @param oldname 原始的文件名 
	   * @param newname 新文件名 
	   */ 

	   public boolean renameFile(String path,String oldname,String newname)
	   { 
	   		if(!oldname.equals(newname))
	   		{ 
	   				File oldfile=new File(path+"/"+oldname); 
	   				File newfile=new File(path+"/"+newname); 
	   					if(newfile.exists())
	   					{
	   						newfile.delete();
	   					    oldfile.renameTo(newfile); 
	   					  //  System.out.println(newname+" is exist, delete it！"); 
	   					  return false;
	   					}
	   					else
	   					{ 
	   					   oldfile.renameTo(newfile); 
	   					} 
	   		} 
	   		return true;
	   } 

}