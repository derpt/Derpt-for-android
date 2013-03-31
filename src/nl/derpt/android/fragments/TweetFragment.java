package nl.derpt.android.fragments;

import nl.derpt.android.R;
import nl.derpt.android.internal.JSON.TweetData;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class TweetFragment extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_main_dummy,
				container, false);
		TextView dummyTextView = (TextView) rootView
				.findViewById(R.id.section_label);
		
		TweetData rs = (TweetData) this.getArguments().getSerializable("data");
		
		dummyTextView.setText(rs.tweet.text);  
		return rootView;
	}

}
