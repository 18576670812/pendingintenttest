package com.whb.pendingintenttest;

import java.util.Iterator;
import java.util.List;

import android.os.Bundle;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.widget.Toast;

public class PendingIntentActivity extends Activity {
	private static final String TAG = "PendingIntentActivity";
	private NotificationManager nM;
	private Context mContext = null;
	private static final int START_REQUEST = 0;
	private static final int PAUSE_REQUEST = 1;
	private static final int RESUME_REQUEST = 2;
	private static final int STOP_REQUEST = 3;
	private static final boolean API_LEVEL_BIG_THAN_4_0 = true;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		mContext = (Context)this;
		Intent intent = getIntent();
		Log.d(TAG, "onCreate()... intent: " + intent);
		nM = (NotificationManager)super.getSystemService(Activity.NOTIFICATION_SERVICE);
		//sendNotification();
		
		if(intent.getAction().equals("SEND_MESSAGE")) {
			sendSms();
		} else if(intent.getAction().equals("SEND_MESSAGE_BY_NUMBER")){
			sendMessage("18664694703");
		} else {
			sendNotification();
		}
	}
	
	@Override
	protected void onNewIntent(Intent intent) {
		// TODO Auto-generated method stub
		Log.d(TAG, "onNewIntent()... intent: " + intent);
		if(intent.getAction().equals("SEND_MESSAGE")) {
			sendSms();
		} else {
			sendMessage("18664694703");
		}
	}
	
	Context createContext(String packageName) {
		Context context = null;
		try {
			context = createPackageContext(packageName, Context.CONTEXT_IGNORE_SECURITY);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		
		return context;
	}

	@SuppressWarnings("deprecation")
	void sendNotification() {
		Context otherApplication = createContext("com.hongbowang.mycontentprovider");
		boolean startActivityByContext = false;
		Notification notification = null;
		Intent intent = new Intent("START_ACTIVITY");
		
		//PendingIntent 通知：
		// 我们可以发送一个通知，在该通知中指定一个PendingIntent对象，该PendingIntent对象中包含一个
		// Intent对象，该Intent对象又指定了要启动的application的包名(或者application的Context对象)以及
		// 该application名下的activity(service, broadcast receiver等)对象。那么，当我们在通知栏点击该
		// 通知的时候，就会启动我们在该Intent中指定的application了。
		if(startActivityByContext) {
			intent.setClassName("com.hongbowang.mycontentprovider", 
								"com.hongbowang.mycontentprovider.ContentProviderActivity");
		} else {
			intent.setClassName(otherApplication, 
								"com.hongbowang.mycontentprovider.ContentProviderActivity");
		}
		
		PendingIntent pendingIntent = PendingIntent.getActivity(this, 
																START_REQUEST, 
																intent, 
																PendingIntent.FLAG_UPDATE_CURRENT);
		if(API_LEVEL_BIG_THAN_4_0) {
			notification = new Notification.Builder(mContext)
										.setContentTitle("content title")
										.setContentText("content text")
										.setSmallIcon(R.drawable.stat_sys_data_bluetooth_connected)
										.setContentIntent(pendingIntent)
										.build();
		} else {
			notification = new Notification(R.drawable.stat_sys_data_bluetooth_connected, 
								"message comes from pending test app", System.currentTimeMillis());
		}
		
		notification.flags |= Notification.FLAG_NO_CLEAR;
		if(API_LEVEL_BIG_THAN_4_0) {
			nM.notify(R.string.app_name, notification);
		} else {
			notification.setLatestEventInfo(this, "content title", 
											"content text", pendingIntent);
			nM.notify("PendingIntent Application", R.drawable.ic_launcher, notification);
		}
	}
	
	void sendSms(){
		String content = "this message comes from PendingIntent Application, for more safety," +
				"I don't know there is how many charactors, but it is better to more than 70." +
				"now, maybe it is more than 70";
		
		SmsManager smsManager = SmsManager.getDefault();
		
		Context otherApplication = createContext("com.hongbowang.mycontentprovider");
		
		Intent intent = new Intent("MESSAGE_SENT_OVER");
		
		boolean startActivityByContext = false;
		
		//PendingIntent Activity：
		// 我们可以创建一个PendingIntent对象，该PendingIntent对象有一个Intent对象，
		// 该Intent对象又指定了要启动的application的包名(或者application的Context对象)以及
		// 该application名下的activity(service, broadcast receiver等)对象。该PendingIntent
		// 对象挂在一个操作上面，当这个操作完成的时候，该PendingIntent对象所包含的Intent对象
		// 就会被触发，然后被指定的application处理.
		if(startActivityByContext) {
			intent.setClassName("com.hongbowang.mycontentprovider", 
								"com.hongbowang.mycontentprovider.ContentProviderActivity");
		} else {
			intent.setClassName(otherApplication, 
								"com.hongbowang.mycontentprovider.ContentProviderActivity");
		}
		
		PendingIntent sentIntent = PendingIntent.getActivity(this, 
															0, 
															intent, 
															PendingIntent.FLAG_UPDATE_CURRENT);
		
		if(content.length() > 70) {
			List<String> msgs = smsManager.divideMessage(content);
			Iterator<String> iterator = msgs.iterator();
			
			while(iterator.hasNext()) {
				String msg = iterator.next();
				smsManager.sendTextMessage("18688779642", null, msg, sentIntent, null);
			}
		} else {
			smsManager.sendTextMessage("18688779642", null, content, sentIntent, null);
		}
		
		Toast.makeText(this, "send successfully", Toast.LENGTH_LONG).show();
	}
	
	void sendMessage(String callNumber) {
		String content = "Walking through a dream," +
				"I see you," +
				"now, maybe it is more than 70, but it isn't enough, we need to cost more time" +
				"My light in darkness breathing hope of new life," +
				"Now I live through you and you through me," +
				"Enchanting, " +
				"I pray in my heart that this dream never end," +
				"I see me through your eyes," +
				"Living through life flying high," +
				"Your life shines the way into paradise," +
				"So I offer my life as a sacrifice," +
				"I live through your love," +
				"You teach me how to see," +
				"All that’s beautiful," +
				"My senses touch your world I never pictured," +
				"Now I give my hope to you," +
				"I surrender," +
				"I pray in my heart that this world never ends," +
				"I see me through your eyes," +
				"Living through life flying high," +
				"So I offer my life," +
				"I offer my love, for you," +
				"When my heart was never open," +
				"(and my spirit never free)," +
				"To the world that you have shown me," +
				"But my eyes could not division," +
				"All the colours of love and of life ever more," +
				"Evermore," +
				"(I see me through your eyes)," +
				"I see me through your eyes," +
				"(Living through life flying high)," +
				"Flying high," +
				"Your love shines the way into paradise," +
				"So I offer my life as a sacrifice," +
				"And live through your love," +
				"And live through your life," +
				"I see you," +
				"I see you.";
		
		SmsManager smsManager = SmsManager.getDefault();
		
		Context otherApplication = createContext("com.hongbowang.mycontentprovider");
		
		Intent intent = new Intent("com.hongbowang.mycontentprovider.MESSAGE_SENT_OVER");
		
		boolean startActivityByContext = false;
		
		//PendingIntent broadcast：
		// 我们可以创建一个PendingIntent对象，该PendingIntent对象有一个Intent对象，
		// 该Intent对象指定了ACTION。该PendingIntent对象挂在一个操作上面，当这个操作完成的时候，
		// 该PendingIntent对象所包含的Intent对象就会被广播，然后被注册了该ACTION的
		// Broadcast Receiver 处理.

		PendingIntent sentIntent = PendingIntent.getBroadcast(this, 
															0, 
															intent, 
															PendingIntent.FLAG_UPDATE_CURRENT);
		
		if(content.length() > 70) {
			List<String> msgs = smsManager.divideMessage(content);
			Iterator<String> iterator = msgs.iterator();
			
			while(iterator.hasNext()) {
				String msg = iterator.next();
				smsManager.sendTextMessage("18688779642", callNumber, msg, sentIntent, null);
			}
		} else {
			smsManager.sendTextMessage("18688779642", callNumber, content, sentIntent, null);
		}
		
		Toast.makeText(this, "send successfully", Toast.LENGTH_LONG).show();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

}
