import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.commons.lang3.StringUtils;
/**
 * Write the contents of a dictionary to a file
 *
 * @author Navaneeth.Rao
 */
public class Writer 
{
	public File write(final Map<String, Properties> dictionary, final String fileName) throws FileNotFoundException, UnsupportedEncodingException {
		final File file = new File(fileName);
		try (final PrintWriter writer = new PrintWriter(file, "UTF-8")) {
			for (final Entry<String, Properties> entry : dictionary.entrySet()) {
				writer.print(entry.getKey() + ":" + entry.getValue().getDocFreq() + " [");
				final List<String> pairs = new ArrayList<>();
				for (final Entry<String, Property> list : entry.getValue().getPostingFile().entrySet()) {
					pairs.add(list.getKey() + ":" + entry.getValue().getTermFreq().get(list.getKey()) + " " + list.getValue().getMaxFreq() + " " +
							list.getValue().getDoclen());
				}
				writer.print(StringUtils.join(pairs, " "));
				writer.println("]");
			}
		}
		return file;
	}
}
