/**
 * 
 */
package nl.derpt.android.internal.jobs;

import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;

import nl.derpt.android.MainActivity;

import android.content.Context;

/**
 * @author paul_000
 * 
 */
public class MarkUnread extends Job {
	private String tweet;

	/**
	 * @param context
	 * @param manager
	 * @param adapter
	 */
	public MarkUnread(Context context, Manager manager, String tweet) {
		super(context, manager);
		this.tweet = tweet;
	}

	@Override
	protected Boolean doInBackground(Void... params) {

		HttpResponse response = doGet("/twitter/timeline/mark/unread/me/"
				+ ((MainActivity) this.context).account.getId() + "/"
				+ this.tweet);
		if (response == null) {
			return false;
		}
		StatusLine st = response.getStatusLine();

		if (st.getStatusCode() == 200) {
			return true;
		}
		return false;
	}

	@Override
	protected void onPostExecute(Boolean result) {
		// TODO: save this locally to mark unread in a later stage!
		
		this.manager.getUnreadCount();
		this.manager.showProgress(false);
		
	}

}
