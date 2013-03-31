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
import nl.derpt.android.internal.Account;
import nl.derpt.android.internal.JSON.ServerAccounts;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.ArrayAdapter;

/**
 * @author paul_000
 *
 */
public class getAccounts extends Job {

	private ArrayAdapter<Account> adapter;


	/**
	 * @param context
	 * @param manager
	 * @param adapter
	 */
	public getAccounts(Context context,  Manager manager, ArrayAdapter<Account> adapter) {
		super(context, manager);
		this.adapter = adapter;
	}


	@Override
	protected void onPostExecute(Boolean result) {
		
		if (!result)
		{
			// We need a nice way to handle this...
			return;
		}
		Gson parser = new Gson();
		
		Type listType = new TypeToken<List<ServerAccounts>>() {}.getType();
		
		ArrayList<ServerAccounts> rs = parser.fromJson(this.response, listType);
		
		adapter.clear();
		
		for(int i = 0; i < rs.size(); i++)
		{
			Account ac = new Account(rs.get(i).screen_name);
			adapter.add(ac);
		}
		
		((MainActivity) this.context).onNavigationItemSelected(0,0);

		this.manager.showProgress(false);
	}


	@Override
	protected Boolean doInBackground(Void... params) {
		

		HttpResponse response = doGet("/twitter/accounts");
		if (response == null) 
		{
			return false;
		}
		StatusLine st = response.getStatusLine();

		if (st.getStatusCode() == 200) {
			return true;
		}
		return false;
	}

}
