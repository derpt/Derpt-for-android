package nl.derpt.android.internal;

public class Account {

	private String name;
	
	private String id;
	
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
}
