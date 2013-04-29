/**
 * 
 */
package nl.derpt.android.internal.jobs;

import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;

import com.google.gson.Gson;

import nl.derpt.android.MainActivity;
import nl.derpt.android.R;
import nl.derpt.android.internal.JSON.UnreadData;

import android.content.Context;
/**
 * @author paul_000
 * 
 */
public class GetUnreadCount extends Job {

	/**
	 * @param context
	 * @param manager
	 */
	public GetUnreadCount(Context context, Manager manager) {
		super(context, manager);
	}

	@Override
	protected Boolean doInBackground(Void... params) {

		HttpResponse response = doGet("/twitter/timeline/get/unread/count/me/"
				+ ((MainActivity) this.context).account.getId());
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
		
		Gson parser = new Gson();
		
		
		String text = this.context.getString(R.string.unknown);

		try {
			UnreadData rs = parser.fromJson(this.response, UnreadData.class);
			
			if (rs.unread != null && !rs.unread.equals("null")) {
				text = rs.unread;
			}
		} catch (Exception e) {

		}
		((MainActivity)this.context).setUnreadCount(text);
		
		this.manager.showProgress(false);
	}

}
