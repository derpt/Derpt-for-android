/**
 * 
 */
package nl.derpt.android.internal.jobs;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.message.BasicNameValuePair;
import android.content.Context;
import android.util.Log;

/**
 * @author paul_000
 * 
 */
public class Login extends Job {
	
	private Job nested;

	/**
	 * @param context
	 * @param session
	 */
	public Login(Context context, Manager manager) {
		super(context, manager);
	}
	
	public Login(Context context, Manager manager, Job job)
	{
		super(context, manager);
		nested = job;
	}

	@Override
	protected Boolean doInBackground(Void... params) {
		// TODO Auto-generated method stub

		this.params.add(new BasicNameValuePair("email", this.manager.user));
		this.params.add(new BasicNameValuePair("password", this.manager.pass));

		HttpResponse response = doPost("/login");
		if (response == null)
		{
			return false;
		}

		StatusLine st = response.getStatusLine();

		if (st.getStatusCode() == 200) {
			// It is OK.
			return true;
		} else {
			Log.d("derpt", "NOT OK");
			return false;
		}
	}

	@Override
	protected void onPostExecute(Boolean result) {
		if (nested != null)
		{
			this.manager.RunJob(nested);
		}
		this.manager.showProgress(false);
	}
}
