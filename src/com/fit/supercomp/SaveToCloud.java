package com.fit.supercomp;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.os.AsyncTask;
import android.support.v4.app.NotificationCompat;

public class SaveToCloud  extends AsyncTask<String, Void, Void>{

	String cur_pos;
	@Override
	protected Void doInBackground(String... params) {
		// TODO Auto-generated method stub
		cur_pos = params[6];
		String url = "https://steam-machine-590.appspot.com/ignoustudent.php";
		HttpClient httpClient = new DefaultHttpClient();
        HttpPost request = new HttpPost(url);
        
        //storing data on server [APP ENGINE]
        List<NameValuePair> postParameters = new ArrayList<NameValuePair>();
        postParameters.add(new BasicNameValuePair("enrollment",params[0]));
        postParameters.add(new BasicNameValuePair("fullname",params[1]));
        postParameters.add(new BasicNameValuePair("program",params[2]));
        postParameters.add(new BasicNameValuePair("year",params[3]));
        postParameters.add(new BasicNameValuePair("email",params[4]));
        postParameters.add(new BasicNameValuePair("num",params[5]));
        
        try {
			UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(postParameters);
			request.setEntity(formEntity);
			httpClient.execute(request);
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return null;
	}
	
	@Override
	protected void onPostExecute(Void result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		if(Integer.parseInt(cur_pos)<GcmIntentService.enrollList.length-1)
		{
			String new_pos = (Integer.parseInt(cur_pos)+1)+"";
			new back().execute(new_pos);
		}
		else{
			
			 GcmIntentService.mBuilder.setSmallIcon(R.drawable.scloud_ic)
					.setContentTitle("Processing Completed")
					.setStyle(new NotificationCompat.BigTextStyle().bigText("Thank You :)"))
					// .setContentText("Right now we are using your device for processing our data");
					.setContentText("Thank you :)");
			 GcmIntentService.mBuilder.setAutoCancel(true);
			GcmIntentService.mNotificationManager.notify(1, GcmIntentService.mBuilder.build());
		}
	}
	

}
