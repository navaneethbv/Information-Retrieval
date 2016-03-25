import java.io.File;
import java.util.List;

/**
 * Class that implements generating tokens and calling storage manager on them
 *
 * @author Navaneeth.Rao
 */
public class Tokenizer {
	public static StanfordLemmatizer lemmatizer = new StanfordLemmatizer();

	public void tokenize(final File file, String line, final Manager storageManager) {
		line = this.transformText(line);
final String[] words = line.split(" ");
		for (final String word : words) {
			if (word == null || word.length() < 1) {
				continue;
			}
	final String stem = Stem.stem(word);
			final List<String> lemma = lemmatizer.lemmatize(word);
			storageManager.store(word, lemma, stem, file);
		}
	}
	private String transformText(String text) {
		// Replacing the SGML tags with space.
		text = text.replaceAll("\\<.*?>", " ");

		// Remove digits
		text = text.replaceAll("[\\d+]", "");

		// Remove the special characters
		text = text.replaceAll("[+^:,?;=%#&~`$!@*_)/(}{\\.]", "");

		// Remove possessives
		text = text.replaceAll("\\'s", "");

		// Replace "'" with a space
		text = text.replaceAll("\\'", " ");

		// Replace - with space to count two words
		text = text.replaceAll("-", " ");

		// Remove multiple white spaces
		text = text.replaceAll("\\s+", " ");

		// Trim and set text to lower case
		text = text.trim().toLowerCase();
		return text;
	}
}