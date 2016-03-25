import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class TextCharacter {
	public Formatter getTermCharacteristics(final Set<String> terms, final Map<String, Properties> dictionary)
			throws NumberFormatException,
			UnsupportedEncodingException {
		final Formatter formatter = new Formatter();
		formatter.addRow("TERM", "TF", "DF", "Size of inverted list");
		for (final String term : terms) {
			if (dictionary.containsKey(term)) {
				final Map<String, Property> postingFile = dictionary.get(term).getPostingFile();
				int tf = 0, lists = 0;
				for (final Entry<String, Property> entry : postingFile.entrySet()) {
					tf += dictionary.get(term).getTermFreq().get(entry.getKey());
					lists += Integer.SIZE / 8 * 4;
				}
				formatter.addRow(term, String.valueOf(tf), String.valueOf(dictionary.get(term).getDocFreq()), lists + " bytes");
			}
		}
		return formatter;
	}
	public Formatter getFirstThree(final Properties properties) {
		List<String> docs = new ArrayList<>();
		docs.addAll(properties.getTermFreq().keySet());
		Collections.sort(docs);
		docs = docs.subList(0, 3);
		final Formatter formatter = new Formatter();
		formatter.addRow("DOC-ID", "TF", "MAX_TF", "DOCLEN");
		for (final String string : docs) {
			final int tf = properties.getTermFreq().get(string);
			final int max_tf = properties.getPostingFile().get(string).getMaxFreq();
			final int doclen = properties.getPostingFile().get(string).getDoclen();
			formatter.addRow(string, String.valueOf(tf), String.valueOf(max_tf), String.valueOf(doclen));
		}
		return formatter;
	}
	public List<String> getTermsWithLargestDf(final Map<String, Properties> dictionary) {
		final List<String> terms = new ArrayList<>();
		int max = 0;
		for (final Entry<String, Properties> entry : dictionary.entrySet()) {
			if (max < entry.getValue().getDocFreq()) {
				terms.clear();
				max = entry.getValue().getDocFreq();
				terms.add(entry.getKey());
			} else if (max == entry.getValue().getDocFreq()) {
				terms.add(entry.getKey());
			}
		}
		return terms;
	}
	public List<String> getDocsWithLargestMaxTF() {
		final List<String> docs = new ArrayList<>();
		int max = 0;
		for (final Entry<String, Property> entry : Manager.getDocProperties().entrySet()) {
			if (max < entry.getValue().getMaxFreq()) {
				docs.clear();
				max = entry.getValue().getMaxFreq();
				docs.add(entry.getKey());
			} else if (max == entry.getValue().getMaxFreq()) {
				docs.add(entry.getKey());
			}
		}
		return docs;
	}
	public List<String> getDocsWithLargestDoclen() {
		final List<String> docs = new ArrayList<>();
		int max = 0;
		for (final Entry<String, Property> entry : Manager.getDocProperties().entrySet()) {
			if (max < entry.getValue().getDoclen()) {
				docs.clear();
				max = entry.getValue().getDoclen();
				docs.add(entry.getKey());
			} else if (max == entry.getValue().getDoclen()) {
				docs.add(entry.getKey());
			}
		}
		return docs;
	}
	public List<String> getTermsWithSmallestDf(final Map<String, Properties> dictionary) {
		final List<String> terms = new ArrayList<>();
		int min = Integer.MAX_VALUE;
		for (final Entry<String, Properties> entry : dictionary.entrySet()) {
			if (min > entry.getValue().getDocFreq()) {
				terms.clear();
				min = entry.getValue().getDocFreq();
				terms.add(entry.getKey());
			} else if (min == entry.getValue().getDocFreq()) {
				terms.add(entry.getKey());
			}
		}
		return terms;
	}
}