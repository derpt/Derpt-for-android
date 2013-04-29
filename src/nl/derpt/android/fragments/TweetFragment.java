package nl.derpt.android.fragments;

import java.util.ArrayList;
import java.util.Collections;

import nl.derpt.android.MainActivity;
import nl.derpt.android.R;
import nl.derpt.android.internal.Account;
import nl.derpt.android.internal.Replace;
import nl.derpt.android.internal.ReplaceComparer;
import nl.derpt.android.internal.JSON.TweetData;
import android.app.Fragment;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
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
		View rootView = inflater.inflate(R.layout.tweetfragment, container,
				false);

		activity = ((MainActivity) this.getActivity());
		tweet = activity.account.getCurrent();
		account = activity.account;

		if (tweet == null) {
			return null;
			// throw new RuntimeException();
		}

		activity.manager.markUnread(tweet.tweet._id);

		TextView tweetv = (TextView) rootView.findViewById(R.id.tweet);
		TextView userv = (TextView) rootView.findViewById(R.id.username);
		TextView joinv = (TextView) rootView.findViewById(R.id.join);

		Log.e("derpt", tweet.tweet.user_image_url);

		RemoteImageView imagev = (RemoteImageView) rootView
				.findViewById(R.id.avatar);
		imagev.setImageUrl(tweet.tweet.user_image_url.replace("normal",
				"bigger"));
		imagev.loadImage();

		int change = 0;
		Log.d("derpt", "URL length: " + tweet.tweet.urls.length);

		ArrayList<Replace> replacer = new ArrayList<Replace>();
		for (int i = 0; i < tweet.tweet.urls.length; i++) {
			Replace text = new Replace();
			text.start = tweet.tweet.urls[i].indices[0];
			text.stop = tweet.tweet.urls[i].indices[1];
			text.replace = "<a href=\"" + tweet.tweet.urls[i].url + "\">"
					+ tweet.tweet.urls[i].display_url + "</a>";

			replacer.add(text);

		}

		for (int i = 0; i < tweet.tweet.hashtags.length; i++) {
			try {
				// Needed due to a nubbage in database in older versions
				if (tweet.tweet.hashtags[i].indices == null) {
					continue;
				}
			} catch (NullPointerException e) {
				continue;
			}

			Replace text = new Replace();
			text.start = tweet.tweet.hashtags[i].indices[0];
			text.stop = tweet.tweet.hashtags[i].indices[1];
			text.replace = "<a href=\"" + tweet.tweet.hashtags[i].text + "\">#"
					+ tweet.tweet.hashtags[i].text + "</a>";
			replacer.add(text);
		}
		for (int i = 0; i < tweet.tweet.mentions.length; i++)
		{
			Replace text = new Replace();
			text.start = tweet.tweet.mentions[i].indices[0];
			text.stop = tweet.tweet.mentions[i].indices[1];
			text.replace = "<a href=\"" + tweet.tweet.mentions[i].screen_name + "\">@"
					+ tweet.tweet.mentions[i].screen_name + "</a>";
			replacer.add(text);
		}

		Collections.sort(replacer, new ReplaceComparer());

		for (int i = 0; i < replacer.size(); i++) {
			Replace rp = replacer.get(i);

			int start = rp.start + change;
			int stop = rp.stop + change;
			int diff = rp.stop - rp.start;

			String star = tweet.tweet.text.substring(0, start);
			String end = tweet.tweet.text.substring(stop,
					tweet.tweet.text.length());

			change += (rp.replace.length() - diff);
			tweet.tweet.text = star + rp.replace + end;
		}

		tweetv.setText(Html.fromHtml(tweet.tweet.text));
		userv.setText(tweet.tweet.user_name);
		joinv.setText(tweet.tweet.user_created);

		tweetv.setMovementMethod(LinkMovementMethod.getInstance());

		rootView.setOnTouchListener(this);
		return rootView;
	}

	static final int MIN_DISTANCE = 100;
	private float downX, upX;

	public void onRightToLeftSwipe() {
		// prev

		if (tweet.prev == null || tweet.prev.equals("null")) {
			Log.d("derpt", "No tweets anymore...");

			return;
		}

		this.activity.manager.getTweet(tweet.prev);
	}

	public void onLeftToRightSwipe() {
		// next

		if (tweet.next == null || tweet.next.equals("null")) {
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
