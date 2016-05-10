import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
/**
 * Class to store stems and lemma dictionary
 *
 * @author Navaneeth.Rao
 */
public class Dictionary {
	private final Map<String, Properties>	stemDictionary;
	private final Map<String, Properties>	lemmaDictionary;
	public Dictionary() {
		this.stemDictionary = new HashMap<>();
		this.lemmaDictionary = new HashMap<>();
	}
	public void append(final Manager manager, final File file) {
		final String doc = file.getName().replaceAll("[^\\d]", "");
		this.appendToDictionary(this.stemDictionary, manager.getStemsMap(), Manager.getDocProperties(), doc);
		this.appendToDictionary(this.lemmaDictionary, manager.getLemmaMap(), Manager.getDocProperties(), doc);
	}
	private void appendToDictionary(final Map<String, Properties> appendTo,
			final Map<String, Integer> appendFrom,
			final Map<String, Property> docProperties,
			final String file) {
		for (final Entry<String, Integer> entry : appendFrom.entrySet()) 
		{
			Properties temp;
			if (appendTo.containsKey(entry.getKey())) {				
				temp = appendTo.get(entry.getKey());
				temp.setDocFreq(temp.getDocFreq() + 1);
				final Map<String, Integer> freq = temp.getTermFreq();
				freq.put(file, entry.getValue());
				temp.setTermFreq(freq);
			} else {
				temp = new Properties();
				temp.setDocFreq(1);
				temp.getTermFreq().put(file, entry.getValue());
			}
			final Property property = docProperties.get(file);
			temp.getPostingFile().put(file, property);
			appendTo.put(entry.getKey(), temp);
		}
	}
	public final Map<String, Properties> getStemsDictionary() {
		return this.stemDictionary;
	}
	public final Map<String, Properties> getLemmaDictionary() {
		return this.lemmaDictionary;
	}
}
