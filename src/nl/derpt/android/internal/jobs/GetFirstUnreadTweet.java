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
import android.view.MenuItem;

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
			return;
		}
		Gson parser = new Gson();

		TweetData rs = parser.fromJson(this.response, TweetData.class);
		
		((MainActivity)this.context).account.setCurrent(rs);

		Log.d("derpt", "Data " + rs.tweet.text);


		
		  
		  
		Fragment fragment = new TweetFragment();

		((MainActivity) this.context).getFragmentManager().beginTransaction()
				.replace(R.id.container, fragment).commit();
		 
		if (((MainActivity) this.context).menu != null) {
			MenuItem m = ((MainActivity) this.context).menu
					.findItem(R.id.menu_unread);

			String text = this.context.getString(R.string.unknown);

			try {
				if (rs.unread != null && !rs.unread.equals("null")) {
					text = rs.unread;
				}
			} catch (NullPointerException e) {

			}

			m.setTitle(text);
		} else {
			Log.d("derpt", "menu is null");
		}

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

		Log.d("derpt", st.toString());

		if (st.getStatusCode() == 200) {
			return true;
		}
		return false;
	}

}
