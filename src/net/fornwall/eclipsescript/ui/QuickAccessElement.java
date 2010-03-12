package net.fornwall.eclipsescript.ui;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.resource.ImageDescriptor;

public abstract class QuickAccessElement {

	private QuickAccessProvider provider;

	/**
	 * @param provider
	 */
	public QuickAccessElement(QuickAccessProvider provider) {
		super();
		this.provider = provider;
	}

	/**
	 * Returns the label to be displayed to the user.
	 * 
	 * @return the label
	 */
	public abstract String getLabel();

	/**
	 * Returns the image descriptor for this element.
	 * 
	 * @return an image descriptor, or null if no image is available
	 */
	public abstract ImageDescriptor getImageDescriptor();

	/**
	 * Returns the id for this element. The id has to be unique within the QuickAccessProvider that provided this
	 * element.
	 * 
	 * @return the id
	 */
	public abstract String getId();

	/**
	 * Executes the associated action for this element.
	 */
	public abstract void execute(String command);

	/**
	 * Return the label to be used for sorting and matching elements.
	 * 
	 * @return the sort label
	 */
	public String getSortLabel() {
		return getLabel();
	}

	/**
	 * @return Returns the provider.
	 */
	public QuickAccessProvider getProvider() {
		return provider;
	}

	/**
	 * @param filter
	 * @return
	 */
	public QuickAccessEntry match(String filter) {
		String sortLabel = getLabel();
		int index = sortLabel.toLowerCase().indexOf(filter);
		if (index != -1) {
			return new QuickAccessEntry(this, new int[][] { { index, index + filter.length() - 1 } });
		}
		String camelCase = CamelUtil.getCamelCase(sortLabel);
		index = camelCase.indexOf(filter);
		if (index != -1) {
			int[][] indices = CamelUtil.getCamelCaseIndices(sortLabel, index, filter.length());
			return new QuickAccessEntry(this, indices);
		}
		return null;
	}

	/**
	 * Copied from internal class org.eclipse.ui.internal.quickaccess.CamelUtil.
	 */
	private static class CamelUtil {

		/**
		 * Returns a lowercase string consisting of all initials of the words in the given String. Words are separated
		 * by whitespace and other special characters, or by uppercase letters in a word like CamelCase.
		 * 
		 * @param s
		 *            the string
		 * @return a lowercase string containing the first character of every wordin the given string.
		 */
		public static String getCamelCase(String s) {
			StringBuffer result = new StringBuffer();
			if (s.length() > 0) {
				int index = 0;
				while (index != -1) {
					result.append(s.charAt(index));
					index = getNextCamelIndex(s, index + 1);
				}
			}
			return result.toString().toLowerCase();
		}

		/**
		 * Return an array with start/end indices for the characters used for camel case matching, ignoring the first
		 * (start) many camel case characters. For example, getCamelCaseIndices("some CamelCase", 1, 2) will return
		 * {{5,5},{10,10}}.
		 * 
		 * @param s
		 *            the source string
		 * @param start
		 *            how many characters of getCamelCase(s) should be ignored
		 * @param length
		 *            for how many characters should indices be returned
		 * @return an array of length start
		 */
		public static int[][] getCamelCaseIndices(String s, int start, int length) {
			List<int[]> result = new ArrayList<int[]>();
			int index = 0;
			while (start > 0) {
				index = getNextCamelIndex(s, index + 1);
				start--;
			}
			while (length > 0) {
				result.add(new int[] { index, index });
				index = getNextCamelIndex(s, index + 1);
				length--;
			}
			return result.toArray(new int[result.size()][]);
		}

		/**
		 * Returns the next index to be used for camel case matching.
		 * 
		 * @param s
		 *            the string
		 * @param index
		 *            the index
		 * @return the next index, or -1 if not found
		 */
		public static int getNextCamelIndex(String s, int index) {
			char c;
			while (index < s.length() && !(isSeparatorForCamelCase(c = s.charAt(index))) && Character.isLowerCase(c)) {
				index++;
			}
			while (index < s.length() && isSeparatorForCamelCase(c = s.charAt(index))) {
				index++;
			}
			if (index >= s.length()) {
				index = -1;
			}
			return index;
		}

		/**
		 * Returns true if the given character is to be considered a separator for camel case matching purposes.
		 * 
		 * @param c
		 *            the character
		 * @return true if the character is a separator
		 */
		public static boolean isSeparatorForCamelCase(char c) {
			return !Character.isLetterOrDigit(c);
		}

	}

}