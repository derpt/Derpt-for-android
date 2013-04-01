/**
 * 
 */
package nl.derpt.android.internal.jobs;

import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;

import nl.derpt.android.MainActivity;

import android.content.Context;
import android.util.Log;

/**
 * @author paul_000
 * 
 */
public class GetTweet extends GetFirstUnreadTweet {
	private String tweet;

	/**
	 * @param context
	 * @param manager
	 * @param adapter
	 */
	public GetTweet(Context context, Manager manager, String tweet) {
		super(context, manager);
		this.tweet = tweet;
	}

	@Override
	protected Boolean doInBackground(Void... params) {

		HttpResponse response = doGet("/twitter/timeline/me/"
				+ ((MainActivity) this.context).account.getId() + "/"
				+ this.tweet);
		if (response == null) {
			return false;
		}
		StatusLine st = response.getStatusLine();

		Log.d("derpt", st.toString());

		if (st.getStatusCode() == 200) {
			return true;
		}
		return false;
	}

}
