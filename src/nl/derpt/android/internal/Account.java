package nl.derpt.android.internal;

import java.io.Serializable;

import nl.derpt.android.internal.JSON.TweetData;

public class Account implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7110491844330038568L;

	private String name;
	
	private String id;
	
	private TweetData current;
	
	/**
	 * Create a new account based from the server.
	 * 
	 */
	public Account()
	{
		
	}
	
	/**
	 * Create a new account based from the server.
	 * 
	 * @param name
	 */
	public Account(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Override
	public String toString()
	{
		return this.name;
	}

	public TweetData getCurrent() {
		return current;
	}

	public void setCurrent(TweetData current) {
		this.current = current;
	}
}
