/**
 * 
 */
package nl.derpt.android.internal;

/**
 * @author paul_000
 * 
 */
public class API {
	/**
	 * 
	 * @param str
	 * @param index
	 * @param end
	 * @param replace
	 * @return
	 */
	public static String replace(String str, int index, int end, String replace)
	{
		char[] data = replace.toCharArray();
		for (int i = index, j = 0; i < end; i++, j++)
		{
			str = replace(str, i, data[j]);
		}
		
		return str;
	}
	
	/**
	 * Replace a charter in a string based on index
	 * @param str
	 * @param index
	 * @param replace
	 * @return
	 */
	public static String replace(String str, int index, char replace) {
		if (str == null) {
			return str;
		} else if (index < 0 || index >= str.length()) {
			return str;
		}
		char[] chars = str.toCharArray();
		chars[index] = replace;
		return String.valueOf(chars);
	}
}
