package nl.derpt.android.internal;

import java.util.Comparator;

public class ReplaceComparer implements Comparator<Replace> {
	  public int compare(Replace x, Replace y) {
	    // TODO: Handle null x or y values
	    
	    	return x.start - y.start;
	  }
}