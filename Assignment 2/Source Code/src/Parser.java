import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
/**
 * Class to parse and tokenize input data
 *
 * @author Navaneeth.Rao
 */
public class Parser {
	private final Set<String>	stop_words;
	private final Dictionary	dictionary;
	public Parser(final File file) throws FileNotFoundException, IOException {
		this.stop_words = new HashSet<>();
		this.dictionary = new Dictionary();
		try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
			for (String line; (line = reader.readLine()) != null;) {
				this.stop_words.add(line.trim());
			}
		}
	}
	public void parse(final File rootFile) throws IOException {
		for (final File file : rootFile.listFiles()) {
			if (file.isDirectory()) {
				this.parse(file);
			} else {
				this.readFile(file);
			}
		}
	}
	private void readFile(final File file) throws IOException {
		if (file == null || !file.exists() || file.isDirectory()) {
			return;
		}
		final Tokenizer tokenizer = new Tokenizer();
		final Manager storageManager = new Manager(this.stop_words);
		try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
			for (String line; (line = reader.readLine()) != null;) {
				tokenizer.tokenize(file, line, storageManager);
			}
		}
		this.dictionary.append(storageManager, file);
	}
	public final Dictionary getDictionary() {
		return this.dictionary;
	}
}