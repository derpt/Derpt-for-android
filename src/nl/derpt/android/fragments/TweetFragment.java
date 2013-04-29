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
import com.github.ignition.core.widgets.RemoteImageView;


public class TweetFragment extends Fragment implements View.OnTouchListener {

	MainActivity activity;
	TweetData tweet;
	Account account;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.tweetfragment,
				container, false);

		activity = ((MainActivity) this.getActivity());
		tweet = activity.account.getCurrent();
		account = activity.account; 
		
		if (tweet == null)
		{
			return null;
			//throw new RuntimeException();
		}
		
		activity.manager.markUnread(tweet.tweet._id);
		
		TextView tweetv = (TextView)rootView.findViewById(R.id.tweet);
		TextView userv = (TextView)rootView.findViewById(R.id.username);
		TextView joinv = (TextView)rootView.findViewById(R.id.join);
		
		Log.d("derpt", tweet.tweet.user_image_url);
		
		RemoteImageView imagev = (RemoteImageView)rootView.findViewById(R.id.avatar);
		imagev.setImageUrl(tweet.tweet.user_image_url.replace("normal", "bigger"));
		imagev.loadImage();
		
		
		tweetv.setText(tweet.tweet.text);
		userv.setText(tweet.tweet.user_name);
		joinv.setText(tweet.tweet.user_created);

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
