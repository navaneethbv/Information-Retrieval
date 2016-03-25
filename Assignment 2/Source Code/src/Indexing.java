import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.lang3.StringUtils;
/**
 * Class to gather information about tokens in the Cranfield database
 *
 * @author Navaneeth.Rao
 */
public class Indexing {
	private static final TextCharacter	characteristics	= new TextCharacter();
	public static void main(final String args[]) throws IOException {
		final long start = System.currentTimeMillis();
		final CommandLine cmd = validateArguments(args);
		final File folder = new File(cmd.getOptionValue("path"));
		final File stopwords = new File(cmd.getOptionValue("stop"));
		final Parser parser = new Parser(stopwords);
		parser.parse(folder);
		String fileName = "Index_Version1.uncompressed";
		Map<String, Properties> dictionary = parser.getDictionary().getLemmaDictionary();
		System.out.println("#################################################");
		displayIndexResults(fileName, dictionary);
		// Display Index 1 compressed results
		File file = new File("Index_Version1.compressed");
		File file2 = new File("Index_Version1.compressedDictionary");
		long startCompressTime = System.currentTimeMillis();
		Compresser.blockCompress(dictionary, file, file2);
		long endcompressedTime = System.currentTimeMillis();
		long elapsedTime = endcompressedTime - startCompressTime;
		displayCompressionResults(elapsedTime, file, file2);
		// Display Index 2 uncompressed results
		fileName = "Index_Version2.uncompressed";
		dictionary = parser.getDictionary().getStemsDictionary();
		displayIndexResults(fileName, dictionary);
		// Display Index 2 compressed results
		file = new File("Index_Version2.compressed");
		file2 = new File("Index_Version2.compressedDictionary");
		startCompressTime = System.currentTimeMillis();
		Compresser.frontCodingCompress(dictionary, file, file2);
		endcompressedTime = System.currentTimeMillis();
		elapsedTime = endcompressedTime - startCompressTime;
		displayCompressionResults(elapsedTime, file, file2);
		System.out.println("#################################################");
		displayTermCharacteristics(parser);
		displayResultforNasa(parser);
		dictionary = parser.getDictionary().getLemmaDictionary();
		System.out.println("#################################################");
		System.out.println("INDEX 1");
		displayPeakTerms(dictionary);
		dictionary = parser.getDictionary().getStemsDictionary();
		System.out.println("INDEX 2");
		displayPeakTerms(dictionary);
		System.out.println("\nDocuments with largest max_tf: " + StringUtils.join(characteristics.getDocsWithLargestMaxTF(), " "));
		System.out.println("Documents with largest document length: " + StringUtils.join(characteristics.getDocsWithLargestDoclen(), " "));
		System.out.println("#################################################");
		System.out.println("\nTotal running time : " + (System.currentTimeMillis() - start) + " milliseconds");
	}
	static void displayPeakTerms(final Map<String, Properties> dictionary) {
		List<String> terms = characteristics.getTermsWithLargestDf(dictionary);
		Formatter formatter = new Formatter();
		formatter.addRow("Term with Largest DF", "DF");
		for (final String string : terms) {
			formatter.addRow(string, String.valueOf(dictionary.get(string).getDocFreq()));
		}
		System.out.println(formatter);
		terms = characteristics.getTermsWithSmallestDf(dictionary);
		formatter = new Formatter();
		formatter.addRow("Terms with Smallest DF", "DF");
		for (final String string : terms) {
			formatter.addRow(string, String.valueOf(dictionary.get(string).getDocFreq()));
		}
		System.out.println(formatter);
	}
	private static void displayResultforNasa(final Parser parser) {
		final Properties properties = parser.getDictionary().getLemmaDictionary().get("nasa");
		final int df = properties.getDocFreq();
		final Formatter formatter = characteristics.getFirstThree(properties);
		System.out.println("NASA: DF = " + df + "\n");
		System.out.println(formatter);
	}
	private static void displayCompressionResults(final long time, final File file, final File file2)
			throws FileNotFoundException,
			IOException {
		// Print the results
		final Formatter formatter = new Formatter();
		formatter.addRow(file.getName(), String.valueOf(file.length() + file2.length()));
		formatter.addRow("Creation time for " + file.getName(), time + " ms");
		System.out.println(formatter);
	}
	static void displayTermCharacteristics(final Parser parser) throws NumberFormatException, UnsupportedEncodingException {
		final Set<String> terms = new HashSet<>();
		terms.add("reynolds");
		terms.add("nasa");
		terms.add("prandtl");
		terms.add("flow");
		terms.add("pressure");
		terms.add("boundary");
		terms.add("shock");
		final StanfordLemmatizer lemmatizer = Tokenizer.lemmatizer;
		final Set<String> lemmaSet = new HashSet<>();
		for (final String string : terms) {
			lemmaSet.addAll(lemmatizer.lemmatize(string));
		}
		final Set<String> stemSet = new HashSet<>();
		for (final String string : terms) {
			stemSet.add(Stem.stem(string));
		}
		System.out.println("#################################################");
		System.out.println("LEMMATIZATION THE TOKENS\n");
		Formatter formatter = characteristics.getTermCharacteristics(lemmaSet, parser.getDictionary().getLemmaDictionary());
		System.out.println(formatter);
		System.out.println("STEMMING THE TOKENS\n");
		formatter = characteristics.getTermCharacteristics(stemSet, parser.getDictionary().getStemsDictionary());
		System.out.println(formatter);
		System.out.println("#################################################");
	}
	private static void displayIndexResults(final String fileName, final Map<String, Properties> dictionary)
			throws FileNotFoundException,
			UnsupportedEncodingException {
		final Writer writer = new Writer();
		final long startTime = System.currentTimeMillis();
		final File index1Uncompressed = writer.write(dictionary, fileName);
		final long endTime = System.currentTimeMillis();
		final Formatter formatter = new Formatter();
		formatter.addRow(fileName, index1Uncompressed.length() + " bytes");
		formatter.addRow("Creation time for " + fileName, endTime - startTime + " ms");
		formatter.addRow("Number of inverted list in " + fileName, String.valueOf(dictionary.size()));
		System.out.println(formatter);
	}
	private static CommandLine validateArguments(final String[] args) {
		final Options options = new Options();
		options.addOption("path", "dataPath", true, "Absolute or relative path to the Cranfield database is required");
		options.addOption("stop", "stopWords", true, "Absolute or relative path to the Stop Words file is required");
		final CommandLineParser commandLineParser = new DefaultParser();
		CommandLine cmd = null;
		try {
			cmd = commandLineParser.parse(options, args, false);
		} catch (final ParseException e1) {
			System.out.println("Invalid arguments are provided");
			final HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp("Tokenization", options);
			System.exit(1);
		}
		if (!cmd.hasOption("path") || !cmd.hasOption("stop")) {
			final HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp("Tokenization", options);
			System.exit(2);
		}
		return cmd;
	}
}