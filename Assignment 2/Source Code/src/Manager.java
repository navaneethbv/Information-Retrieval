import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
/**
 * Class to store all the characteristics of the indexes
 *
 * @author Navaneeth.Rao
 */
public class Manager {
	private final Map<String, Integer>		stemMap;
	private final Map<String, Integer>		lemmaMap;
	private static Map<String, Property>	docProperties	= new HashMap<>();;
	private static Set<String>				stopword;
	public Manager(final Set<String> stopword) {
		this.stemMap = new HashMap<>();
		this.lemmaMap = new HashMap<>();
		Manager.stopword = stopword;
	}
	public void store(final String word, final List<String> lemma, final String stem, final File file) {
		// Create document properties
		final String doc = file.getName().replaceAll("[^\\d]", "");
		if (!Manager.docProperties.containsKey(doc)) {
			docProperties.put(doc, new Property());
		}if (!Manager.stopword.contains(word)) {			
			int count = this.stemMap.containsKey(stem) ? this.stemMap.get(stem) : 0;
			this.stemMap.put(stem, count + 1);
			for (final String string : lemma) {
				count = this.lemmaMap.containsKey(string) ? this.lemmaMap.get(string) : 0;
				this.lemmaMap.put(string, count + 1);
			}
			if (Manager.docProperties.get(doc).getMaxFreq() < count + 1) {
				Manager.docProperties.get(doc).setMaxFreq(count + 1);
			}
		}
		final int len = Manager.docProperties.get(doc).getDoclen();
		Manager.docProperties.get(doc).setDoclen(len + 1);
	}
	public final Map<String, Integer> getStemsMap() {
		return this.stemMap;
	}
	public final Map<String, Integer> getLemmaMap() {
		return this.lemmaMap;
	}
	public static final Map<String, Property> getDocProperties() {
		return Manager.docProperties;
	}
}