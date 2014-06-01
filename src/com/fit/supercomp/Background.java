package com.fit.supercomp;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.os.AsyncTask;

public class Background extends AsyncTask<String, Void, String>{

	String output;
	@Override
	protected String doInBackground(String... params) {

		String url = "http://nigelcrasto.com/JAVAAPP/gcmregister.php";
		HttpClient httpClient = new DefaultHttpClient();
        HttpPost request = new HttpPost(url);
        
        //storing data on server [APP ENGINE]
        List<NameValuePair> postParameters = new ArrayList<NameValuePair>();
        
        
        try {
			postParameters.add(new BasicNameValuePair("regid",URLEncoder.encode(params[1], "utf-8")));
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        postParameters.add(new BasicNameValuePair("email",params[0]));
        
        try {
			UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(postParameters);
			request.setEntity(formEntity);
			httpClient.execute(request);
		} catch (Exception e) {
			e.printStackTrace();
		} 
		
		return output;
	}
	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		
	}


}
