package nl.derpt.android.internal.jobs;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

public class Manager {
	private Context context;
	protected String user;
	protected String pass;
	protected HttpClient httpclient = new DefaultHttpClient();
	private int currentActions = 0;

	public Manager(Context context) {
		// TODO Auto-generated constructor stub

		this.context = context;

		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(context);

		if (prefs.getString("username", "").isEmpty()
				|| prefs.getString("password", "").isEmpty()) {
			throw new RuntimeException("Missing username or password");
		}

		this.user = prefs.getString("username", "");
		this.pass = prefs.getString("password", "");

		// First, we are going to create a session.

		this.login();
	}

	/**
	 * Show or hide the progress bar icon
	 * 
	 * @param show
	 */
	// @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
	void showProgress(final boolean show) {

		Log.d("derpt", "Current: " + currentActions + " show: " + show);
		if (show) {
			
			currentActions++;
			Log.d("derpt", "New: " + currentActions);
			if (currentActions > 1)
			{
				Log.d("derpt", "return a");
				return;
			}
			
		} else {
			currentActions--;
			Log.d("derpt", "New: " + currentActions);
			if (currentActions > 0) {
				Log.d("derpt", "return b");
				return;
			}
			if (currentActions < 0) {
				currentActions = 0;
			}
		}

		((Activity) this.context).setProgressBarIndeterminateVisibility(show);

	}

	/**
	 * Login a user.
	 * 
	 */
	public void login() {
		RunJob(new Login(this.context, this), false);
	}

	/**
	 * Login a user and execute a nested job.
	 * 
	 * @param job
	 */
	public void login(Job job) {
		RunJob(new Login(this.context, this, job), false);
	}

	/**
	 * Get the first unread tweet from the server
	 * 
	 * @param ac
	 */
	public void getFirstUnreadTweet() {
		RunJob(new GetFirstUnreadTweet(this.context, this));
	}

	/**
	 * Get a specific tweet from the server. This method uses the Derpt specific
	 * ID, not the twitter id.
	 * 
	 * @param ac
	 * @param tweet
	 */
	public void getTweet(String tweet) {
		RunJob(new GetTweet(this.context, this, tweet));
	}

	/**
	 * Receive the accounts from the server and display them in the manager.
	 */
	public void getAccounts() {
		RunJob(new GetAccounts(this.context, this));
	}

	/**
	 * Mark a specific tweet unread at the server
	 * 
	 * @param tweet
	 */
	public void markUnread(String tweet) {
		RunJob(new MarkUnread(this.context, this, tweet));
	}

	public void getUnreadCount() {
		RunJob(new GetUnreadCount(this.context, this));
	}

	/**
	 * Run a self selected job in a new thread.
	 * 
	 * @param job
	 */
	public void RunJob(Job job) {
		RunJob(job, true);
	}

	/**
	 * Run a self selected job in a new thread.
	 * 
	 * @param job
	 * @param checkLogin
	 */
	public void RunJob(Job job, boolean checkLogin) {
		showProgress(true);

		if (checkLogin) {
			// We are first going to make sure the user is logged in.
			TestSession session = new TestSession(this.context, this, job);
			session.execute((Void) null);
			return;
		}

		job.execute((Void) null);
	}

}
