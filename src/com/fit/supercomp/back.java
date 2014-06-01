package com.fit.supercomp;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.os.AsyncTask;
import android.text.Html;
import android.util.Log;

class back extends AsyncTask<String, Void, String> {
	String curr_pos = "";

	@Override
	protected String doInBackground(String... params) {
		curr_pos = params[0];
		params[0] = GcmIntentService.enrollList[Integer.parseInt(params[0])];
		BufferedReader inBuffer = null;
		String url = "http://admission.ignou.ac.in/changeadmdata/AdmissionStatusNew.ASP";
		String result = "fail";
		try {
			HttpClient httpClient = new DefaultHttpClient();
			HttpPost request = new HttpPost(url);
			List<NameValuePair> postParameters = new ArrayList<NameValuePair>();
			postParameters.add(new BasicNameValuePair("EnrNo", params[0]));
			postParameters.add(new BasicNameValuePair("program", "MCA"));
			postParameters.add(new BasicNameValuePair("Submit", "true"));
			UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(
					postParameters);
			request.setEntity(formEntity);
			HttpResponse response = httpClient.execute(request);
			HttpEntity entity = response.getEntity();
			if (entity != null)
				result = EntityUtils.toString(entity);

		} catch (Exception e) {
			Log.e("GetError 1", e.toString());
		} finally {
			if (inBuffer != null) {
				try {
					inBuffer.close();
				} catch (IOException e) {
					Log.e("GetError 2", e.toString());
				}
			}
		}
		return result;
	}

	protected void onPostExecute(String page) {
		// enrolment.append("\n"+kite);
		Document doc = Jsoup.parse(page);
		Elements s = doc.select("table.bkctable");
		int i = 0, j = 0;
		String par[] = new String[6];
		for (Element table : s) {
			for (Element t : table.select("tbody")) {
				for (Element row : t.select("tr").select("td")) {
					if (i == 1 || i == 2 || i == 3 || i == 11 || i == 25
							|| i == 27) // ||i==23
					{
						Elements tds = row.select("td");
						String text = page.replace("</br>", "");
						text = Html.fromHtml(tds.get(0).toString()
								.replace("Change", "").replace("Welcome", "")
								.replace("Program: ", "")
								.replace("Enrollment No.:", "")
								.replace("</br>", "-").trim())
								+ "\n";
						par[j++] = Html.fromHtml(text) + "";
						// enrolment.append(par[j++]+"\n-");
					}
					i++;
				}
			}
		}
		Log.i("My Value", par[1] + par[0] + par[2] + par[3] + par[5] + par[4]);

		new SaveToCloud().execute(par[1], par[0], par[2], par[3], par[5],
				par[4], curr_pos);
	}

}