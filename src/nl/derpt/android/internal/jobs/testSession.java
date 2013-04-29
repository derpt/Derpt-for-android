/**
 * 
 */
package nl.derpt.android.internal.jobs;

import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;

import android.content.Context;
import android.util.Log;

/**
 * @author paul sohier
 * 
 */
public class TestSession extends Job {

	Job nested;

	/**
	 * @param context
	 */
	public TestSession(Context context, Manager manager, Job job) {
		super(context, manager);

		nested = job;

		if (manager == null) {
			throw new RuntimeException();
		}
	}

	@Override
	protected Boolean doInBackground(Void... params) {
		HttpResponse response = doGet("/account/authenticated");
		if (response == null) {
			return false;
		}

		StatusLine st = response.getStatusLine();

		if (st.getStatusCode() == 200) {
			// It is OK.
			Log.d("derpt", "OK");
			return true;
		} else {
			Log.d("derpt", "NOT OK");
			return false;
		}
	}

	@Override
	protected void onPostExecute(final Boolean success) {

		if (!success) {
			this.manager.login(nested);
		} else {
			this.manager.RunJob(nested, false);
		}
		this.manager.showProgress(false);
	}

}
