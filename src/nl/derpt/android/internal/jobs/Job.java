/**
 * 
 */
package nl.derpt.android.internal.jobs;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.util.EntityUtils;

import nl.derpt.android.internal.JSON.Base;
import android.content.Context;
import android.os.AsyncTask;

/**
 * @author paul_000
 * 
 */
public abstract class Job extends AsyncTask<Void, Void, Boolean> {
	protected Manager manager;
	protected Context context;
	protected Base base;
	protected ArrayList<NameValuePair> params;
	
	protected String response;

	/**
	 * 
	 */
	public Job(Context context, Manager manager) {
		//
		this.manager = manager;
		this.context = context;
		params = new ArrayList<NameValuePair>();

	}

	public Base getResult() {
		return base;
	}

	public void setManager(Manager man) {
		this.manager = man;
	}

	@Override
	protected void onCancelled() {
		this.manager.showProgress(false);
	}

	@Override
	abstract protected void onPostExecute(Boolean result);

	@Override
	abstract protected Boolean doInBackground(Void... params);
	
	protected HttpResponse doGet(String path)
	{
		HttpGet httpget = new HttpGet("http://www.derpt.nl" + path);
		
		try {
			HttpResponse r = this.manager.httpclient.execute(httpget);
			
			this.response = EntityUtils.toString(r.getEntity());
			
			return r;
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	protected HttpResponse doPost(String path) {
		try {
			HttpPost httppost = new HttpPost("http://www.derpt.nl" + path);

			httppost.setEntity(new UrlEncodedFormEntity(params));

			// Execute HTTP Post Request
			HttpResponse r = this.manager.httpclient.execute(httppost);
			
			this.response = EntityUtils.toString(r.getEntity());
			
			return r;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
