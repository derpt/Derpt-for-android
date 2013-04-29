package nl.derpt.android;

import java.util.ArrayList;

import nl.derpt.android.internal.Account;
import nl.derpt.android.internal.jobs.Manager;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;


import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

public class MainActivity extends Activity implements ActionBar.TabListener {

	/**
	 * The serialization (saved instance state) Bundle key representing the
	 * current dropdown position.
	 */
	//private static final String STATE_SELECTED_NAVIGATION_ITEM = "selected_navigation_item";
	
	public Manager manager;
	
	public Menu menu;

	public ArrayList<Account> accounts;
	
	public Account account; 

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(getApplicationContext());
		String user = prefs.getString("username", "");
		String pass = prefs.getString("password", "");

		if (user.isEmpty() || pass.isEmpty()) {
			Intent i = new Intent(getApplicationContext(), LoginActivity.class);
			startActivity(i);
			return;
		}

		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);

		setContentView(R.layout.activity_main);

		manager = new Manager(this);

		// Set up the action bar to show a dropdown list.
		final ActionBar actionBar = getActionBar();
		actionBar.setDisplayShowTitleEnabled(true);
		accounts = new ArrayList<Account>();
	 
		/** Enabling dropdown list navigation for the action bar */
		//getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		/**
		 * Setting dropdown items and item navigation listener for the actionbar
		 */
		manager.getAccounts();
		
		markWaiting(true);
		
		actionBar.removeAllTabs();
	}
	
	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState) {
		// Restore the previously serialized current dropdown position.
		/*if (savedInstanceState.containsKey(STATE_SELECTED_NAVIGATION_ITEM)) {
			getActionBar().setSelectedNavigationItem(
					savedInstanceState.getInt(STATE_SELECTED_NAVIGATION_ITEM));
		}*/
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		// Serialize the current dropdown position.
		/*outState.putInt(STATE_SELECTED_NAVIGATION_ITEM, getActionBar()
				.getSelectedNavigationIndex());*/
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		
		this.menu = menu;
		return true;
	}
	
	public void markWaiting(boolean first) {
		int item = R.string.waiting;
		if (!first) {
			getActionBar().removeAllTabs();
			this.manager.getUnreadCount();
			this.manager.getFirstUnreadTweet();
			item = R.string.waiting2;
			
			getActionBar().setTitle(this.account.getName());
			
			ActionBar bar = getActionBar();
			
			bar.removeAllTabs();

			bar.addTab(bar.newTab().setText(getString(R.string.tweets))
					.setTabListener(this));
			
			bar.addTab(bar.newTab().setText(getString(R.string.mentions))
					.setTabListener(this));

			bar.addTab(bar.newTab().setText(getString(R.string.DM))
					.setTabListener(this));			
			
			//bar.setSelectedNavigationItem(0);		
			currentTab = 0;
		}
		Fragment fragment = new waitingFragment();

		Bundle args = new Bundle();
		args.putInt("lang", item);
		fragment.setArguments(args);
		getFragmentManager().beginTransaction()
				.replace(R.id.container, fragment).commit();
	}

	public static class waitingFragment extends Fragment {		
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main_dummy,
					container, false);
			TextView dummyTextView = (TextView) rootView
					.findViewById(R.id.section_label);
			dummyTextView.setText(this.getArguments().getInt("lang")); 
			return rootView;
		}
	}

	@Override
	public void onTabReselected(Tab arg0, FragmentTransaction arg1) {
		// TODO Auto-generated method stub
		
	}
	
	private int currentTab;

	private String unread;

	@Override
	public void onTabSelected(Tab arg0, FragmentTransaction arg1) {
		// TODO Auto-generated method stub
		
		if (currentTab == arg0.getPosition())
		{
			return;
		}
		
		switch(arg0.getPosition())
		{
		case 0:
			//markWaiting(false);	
		break;
		case 1:
			// Mentions
		break;
		
		case 2:
			//DMs
		break;
		default:
			throw new RuntimeException("Invalid TAB selected.");
		}
		
	}

	@Override
	public void onTabUnselected(Tab arg0, FragmentTransaction arg1) {
		// TODO Auto-generated method stub
		
	}
	
	public void setUnreadCount(String text)
	{
		this.unread = text;
		changeUnreadCountText(this.unread);
	}
	
	private void changeUnreadCountText(String text)
	{
		if (menu != null) {
			MenuItem m = menu
					.findItem(R.id.menu_unread);

			m.setTitle(text);
		} else {
			Log.d("derpt", "menu is null");
		}			
	}
}
