package nl.derpt.android.internal.JSON;

import java.util.Date;

public class Tweet extends Base {
	
	// Twitter ID;
	public String id;
	public boolean unread;
	
	public String coordinatesx;
	public String coordinatexy;
	
	public String created_at;
	public boolean favorited;
	public String in_reply;
	
	public String text;
	
	//contributors
	
	public int retweet_count;
	public boolean retweeted;
	public String source;
	public String user_name;
	public String user_image_url;
	public String user_created;
	
	public String user_location;
	public String user_id;
	
	// Internal ID
	public String _id;
	
}
