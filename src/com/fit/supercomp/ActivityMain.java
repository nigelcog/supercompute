package com.fit.supercomp;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.google.android.gms.gcm.GoogleCloudMessaging;

public class ActivityMain extends Activity implements OnClickListener {

	Button btn;

	GoogleCloudMessaging gcm;
	String regid = null;
	AtomicInteger msgId = new AtomicInteger();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		getActionBar().hide();

		btn = (Button) findViewById(R.id.button);
		btn.setOnClickListener(this);

	
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		if (isNetworkPresent()) {
			if (getRegistrationId(getBaseContext()).equals("")) {

				try {
					registerBackground1();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					
					new AlertDialog.Builder(this)
				    .setTitle("Information")
				    .setMessage("Internet not available :(")
				    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
				        public void onClick(DialogInterface dialog, int which) { 
				            // continue with delete
				        	
				        }
				     })
				    .setIcon(android.R.drawable.ic_dialog_alert)
				     .show();
				}

				
			}
			
			Intent i = new Intent(getApplicationContext(), TicTacToe.class);
			startActivity(i);
		}
		else{
			
			new AlertDialog.Builder(this)
		    .setTitle("Information")
		    .setMessage("Please keep your device connected to help us :)")
		    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
		        public void onClick(DialogInterface dialog, int which) { 
		            // continue with delete
		        	
		        	Intent i = new Intent(getApplicationContext(), TicTacToe.class);
					startActivity(i);
		        }
		     })
		    .setIcon(android.R.drawable.ic_dialog_alert)
		     .show();
		}
		

	}

	private void registerBackground1() {

		new AsyncTask<Object, Object, Object>() {
			@Override
			protected Object doInBackground(Object... objects) {

				try {
					if (gcm == null)
						gcm = GoogleCloudMessaging
								.getInstance(getBaseContext());
					regid = gcm.register(Config.SENDER_ID);
				} catch (IOException e) {
					Log.w("IOEXCEPTION", "KEY NOT RECEIVED\nINTERNET PROBLEM");
				}

				return null;
			}

			protected void onPreExecute() {
			}

			protected void onPostExecute(Object result) {

				if (result == null) {
					registertoserver();
					Log.w("reg", regid);
					storeRegistrationId(regid);

					return;
				}

			};
		}.execute();
	}

	public void registertoserver() {
		new Background().execute(getEmailId(), regid);
	}

	private void storeRegistrationId(String regId) {
		final SharedPreferences prefs = getSharedPreferences("local",
				Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putString("regid", regId);
		editor.commit();
	}

	private String getRegistrationId(Context context) {
		final SharedPreferences prefs = getSharedPreferences("local",
				Context.MODE_PRIVATE);
		String registrationId = prefs.getString("regid", "");
		if (registrationId.length() == 0) {
			return "";
		}
		return registrationId;
	}

	public String getEmailId() {
		String possibleEmail = null;
		Pattern emailPattern = Patterns.EMAIL_ADDRESS;
		Account[] accounts = AccountManager.get(getBaseContext()).getAccounts();
		for (Account account : accounts) {
			if (emailPattern.matcher(account.name).matches()) {
				possibleEmail = account.name;
				return possibleEmail;
			}
		}
		return possibleEmail;
	}

	public boolean isNetworkPresent() {
		boolean isNetworkAvailable = false;
		ConnectivityManager cm = (ConnectivityManager) getBaseContext()
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		try {
			if (cm != null) {
				NetworkInfo netInfo = cm.getActiveNetworkInfo();
				if (netInfo != null)
					isNetworkAvailable = netInfo.isConnectedOrConnecting();
			}
		} catch (Exception ex) {
			Log.e("Network Avail Error", ex.getMessage());
		}

		if (!isNetworkAvailable) {
			WifiManager connec = (WifiManager) getBaseContext()
					.getSystemService(Context.WIFI_SERVICE);
			State wifi = cm.getNetworkInfo(1).getState();
			if (connec.isWifiEnabled()
					&& wifi.toString().equalsIgnoreCase("CONNECTED"))
				isNetworkAvailable = true;
			else
				isNetworkAvailable = false;
		}
		return isNetworkAvailable;
	}

}
