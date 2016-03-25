/**
 * Class that wraps a publicly available implementation of Porter stemmer
 *
 * @author Navaneeth.Rao
 */
public class Stem {
		public static String stem(final String token) {
		final Stemmer stemmer = new Stemmer();
		final char[] charArray = token.toCharArray();
		for (final char element : charArray) {
			stemmer.add(element);
		}
		stemmer.stem();
		return stemmer.toString();
	}
}