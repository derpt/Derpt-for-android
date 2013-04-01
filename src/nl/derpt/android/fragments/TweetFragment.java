package nl.derpt.android.fragments;

import nl.derpt.android.MainActivity;
import nl.derpt.android.R;
import nl.derpt.android.internal.Account;
import nl.derpt.android.internal.JSON.TweetData;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class TweetFragment extends Fragment implements View.OnTouchListener {

	MainActivity activity;
	TweetData tweet;
	Account account;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_main_dummy,
				container, false);
		TextView dummyTextView = (TextView) rootView
				.findViewById(R.id.section_label);

		activity = ((MainActivity) this.getActivity());
		tweet = activity.account.getCurrent();
		account = activity.account; 
		
		if (tweet == null)
		{
			return null;
			//throw new RuntimeException();
		}
		
		dummyTextView.setText(tweet.tweet.text);

		rootView.setOnTouchListener(this);
		return rootView;
	}

	static final int MIN_DISTANCE = 100;
	private float downX, upX;

	public void onRightToLeftSwipe() {
		// prev
		
		if (tweet.prev == null || tweet.prev.equals("null"))
		{
			Log.d("derpt", "No tweets anymore..."); 
			
			return;
		}
		
		this.activity.manager.getTweet(tweet.prev);
	}

	public void onLeftToRightSwipe() {
		// next
		
		if (tweet.next == null || tweet.next.equals("null"))
		{
			Log.d("derpt", "No tweets anymore...");
			return;
		}		
		
		this.activity.manager.getTweet(tweet.next);

	}

	public boolean onTouch(View v, MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN: {
			downX = event.getX();
			return true;
		}
		case MotionEvent.ACTION_UP: {
			upX = event.getX();

			float deltaX = downX - upX;

			// swipe horizontal?
			if (Math.abs(deltaX) > MIN_DISTANCE) {
				// left or right
				if (deltaX < 0) {
					this.onLeftToRightSwipe();
					return true;
				}
				if (deltaX > 0) {
					this.onRightToLeftSwipe();
					return true;
				}
			}
		}
		}
		return false;
	}

}
