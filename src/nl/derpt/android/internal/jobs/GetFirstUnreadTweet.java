/**
 * 
 */
package nl.derpt.android.internal.jobs;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import nl.derpt.android.MainActivity;
import nl.derpt.android.R;
import nl.derpt.android.MainActivity.waitingFragment;
import nl.derpt.android.fragments.TweetFragment;
import nl.derpt.android.internal.Account;

import nl.derpt.android.internal.JSON.ServerAccounts;
import nl.derpt.android.internal.JSON.TweetData;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

/**
 * @author paul_000
 * 
 */
public class GetFirstUnreadTweet extends Job {

	Account account;

	/**
	 * @param context
	 * @param manager
	 * @param adapter
	 */
	public GetFirstUnreadTweet(Context context, Manager manager, Account ac) {
		super(context, manager);
		this.account = ac;
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
		
		Log.d("derpt", "Data " + rs.tweet.text);
		
		Fragment fragment = new TweetFragment();

		Bundle args = new Bundle();
		args.putSerializable("data", rs);
		
		
		fragment.setArguments(args);
		((MainActivity)this.context).getFragmentManager().beginTransaction()
				.replace(R.id.container, fragment).commit();		

		this.manager.showProgress(false);
	}

	@Override
	protected Boolean doInBackground(Void... params) {

		HttpResponse response = doGet("/twitter/timeline/me/"
				+ this.account.getId() + "/first/unread");
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
