package nl.derpt.android;

import java.util.ArrayList;

import nl.derpt.android.internal.Account;
import nl.derpt.android.internal.jobs.Manager;
import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

import android.view.LayoutInflater;
import android.view.Menu;

import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class MainActivity extends Activity implements
		ActionBar.OnNavigationListener {

	/**
	 * The serialization (saved instance state) Bundle key representing the
	 * current dropdown position.
	 */
	private static final String STATE_SELECTED_NAVIGATION_ITEM = "selected_navigation_item";

	public ArrayAdapter<Account> adapter;

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

		Manager manager = new Manager(this);

		// Set up the action bar to show a dropdown list.
		final ActionBar actionBar = getActionBar();
		actionBar.setDisplayShowTitleEnabled(false);
		ArrayList<Account> actions = new ArrayList<Account>();

		actions.add(new Account(getString(R.string.waiting)));

		/** Create an array adapter to populate dropdownlist */
		adapter = new ArrayAdapter<Account>(actionBar.getThemedContext(),
				android.R.layout.simple_list_item_1, android.R.id.text1,
				actions);

		/** Enabling dropdown list navigation for the action bar */
		getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);

		/**
		 * Setting dropdown items and item navigation listener for the actionbar
		 */
		getActionBar().setListNavigationCallbacks(adapter, this);
		manager.getAccounts(this.adapter);

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
		return true;
	}

	@Override
	public boolean onNavigationItemSelected(int position, long id) {
		// When the given dropdown item is selected, show its contents in the
		// container view.

		Fragment fragment = null;
		if (adapter.getItem(position).getName()
				.equals(getString(R.string.waiting))) {
			// Waiting for server :).
			fragment = new waitingFragment();
		} else {
			fragment = new DummySectionFragment();
		}

		Bundle args = new Bundle();
		args.putInt(DummySectionFragment.ARG_SECTION_NUMBER, position + 1);
		fragment.setArguments(args);
		getFragmentManager().beginTransaction()
				.replace(R.id.container, fragment).commit();
		return true;
	}

	public static class waitingFragment extends Fragment {
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main_dummy,
					container, false);
			TextView dummyTextView = (TextView) rootView
					.findViewById(R.id.section_label);
			dummyTextView.setText(getString(R.string.waiting));
			return rootView;
		}
	}

	/**
	 * A dummy fragment representing a section of the app, but that simply
	 * displays dummy text.
	 */
	public static class DummySectionFragment extends Fragment {
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		public static final String ARG_SECTION_NUMBER = "section_number";

		public DummySectionFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main_dummy,
					container, false);
			TextView dummyTextView = (TextView) rootView
					.findViewById(R.id.section_label);
			dummyTextView.setText(Integer.toString(getArguments().getInt(
					ARG_SECTION_NUMBER)));
			return rootView;
		}
	}

}
