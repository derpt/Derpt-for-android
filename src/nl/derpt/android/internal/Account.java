package nl.derpt.android.internal;

public class Account {

	private String name;
	
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

	@Override
	public String toString()
	{
		return this.name;
	}
}
