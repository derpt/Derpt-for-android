/**
 * 
 */
package nl.derpt.android.internal.jobs;

import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;

import com.google.gson.Gson;

import nl.derpt.android.MainActivity;
import nl.derpt.android.R;

import nl.derpt.android.fragments.TweetFragment;
import nl.derpt.android.internal.JSON.TweetData;
import android.app.Fragment;
import android.content.Context;
import android.util.Log;


/**
 * @author paul_000
 * 
 */
public class GetFirstUnreadTweet extends Job {

	/**
	 * @param context
	 * @param manager
	 * @param adapter
	 */
	public GetFirstUnreadTweet(Context context, Manager manager) {
		super(context, manager);
	}

	@Override
	protected void onPostExecute(Boolean result) {

		if (!result) {
			// We need a nice way to handle this...
			Log.d("derpt", "Failure");
			this.manager.showProgress(false);
			return;
		}
		Gson parser = new Gson();

		TweetData rs = parser.fromJson(this.response, TweetData.class);
		
		((MainActivity)this.context).account.setCurrent(rs);

		Log.d("derpt", "Data " + rs.tweet.text);
		  
		Fragment fragment = new TweetFragment();

		((MainActivity) this.context).getFragmentManager().beginTransaction()
				.replace(R.id.container, fragment).commit();
		 
		this.manager.showProgress(false);
	}

	@Override
	protected Boolean doInBackground(Void... params) {

		HttpResponse response = doGet("/twitter/timeline/me/"
				+ ((MainActivity) this.context).account.getId()
				+ "/first/unread");
		if (response == null) {
			return false;
		}
		StatusLine st = response.getStatusLine();

		if (st.getStatusCode() == 200) {
			return true;
		}
		return false;
	}

}
