import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.RandomAccessFile; 
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.commons.lang3.StringUtils;

/**
 * Implements methods needed for index compression
 *
 * @author Navaneeth.Rao
 */
public class Compresser {
	public static void blockCompress(final Map<String, Properties> dictionary, final File file, final File file2) throws FileNotFoundException, IOException {
		try (final RandomAccessFile accessFile = new RandomAccessFile(file, "rw"); PrintWriter writer = new PrintWriter(file2)) {
			List<String> words = new ArrayList<String>();
			int counter = 0;
			for (final Entry<String, Properties> entry : dictionary.entrySet()) {
				if (counter == 0) {
					final String compressed = StringUtils.join(words.toArray());
					words = new ArrayList<String>();
					writer.write(compressed + compressed.length());
				}
				if (counter < 8) {
					words.add(entry.getKey());
					final byte[] df = gamma(entry.getValue().getDocFreq());
					accessFile.write(df);
					int previous = 0;
					final Map<String, Property> tempPostingFile = entry.getValue().getPostingFile();
					for (final Entry<String, Property> list : tempPostingFile.entrySet()) {
						final byte[] gap = gamma(Integer.parseInt(list.getKey()) - previous);
						accessFile.write(gap);
						previous = Integer.parseInt(list.getKey());
						final byte[] tf = gamma(entry.getValue().getTermFreq().get(list.getKey()));
						accessFile.write(tf);
						final byte[] doclen = gamma(list.getValue().getDoclen());
						accessFile.write(doclen);
						final byte[] max = gamma(list.getValue().getMaxFreq());
						accessFile.write(max);
					}
					counter++;
				}
				if (counter == 8) {				
					counter = 0;
				}
			}
		}
	}

	public static void frontCodingCompress(final Map<String, Properties> dictionary, final File file, final File file2)
			throws FileNotFoundException,
			IOException {

		final List<String> sortedList = new ArrayList<>();
		sortedList.addAll(dictionary.keySet());
		Collections.sort(sortedList);

		int minimumLength = Integer.MAX_VALUE;
		for (final String string : sortedList) {
			minimumLength = Math.min(minimumLength, string.length());
		}

		int prefixLength = 0;
		boolean breakFlag = false;
		final StringBuilder frontCodeString = new StringBuilder();
		try (final RandomAccessFile accessFile = new RandomAccessFile(file, "rw"); PrintWriter writer = new PrintWriter(file2)) {

			while (prefixLength < minimumLength) {
				final char cur = sortedList.get(0).charAt(prefixLength);
				for (int i = 1; i < sortedList.size(); i++) {

					if (!(sortedList.get(i).charAt(prefixLength) == cur)) {
						breakFlag = true;
						break;
					}
				}

				if (breakFlag) {
					break;
				}

				prefixLength++;	}

			if (prefixLength >= 1) {
				frontCodeString.append(Integer.toString(sortedList.get(0).length())
						+ sortedList.get(0).substring(0, prefixLength) + "*"
						+ sortedList.get(0).substring(prefixLength));
				for (int i = 1; i < sortedList.size(); i++) {
					frontCodeString.append(Integer.toString(sortedList.get(i).length()
							- prefixLength)
							+ "|" + sortedList.get(i).substring(prefixLength));
				}
			} else {				
				for (int i = 0; i < sortedList.size(); i++) {
					frontCodeString.append(Integer.toString(sortedList.get(i).length())
							+ sortedList.get(i));
				}
			}for (final String string : sortedList) {
				int previous = 0;
				final Map<String, Property> tempPostingFile = dictionary.get(string).getPostingFile();
				for (final Entry<String, Property> list : tempPostingFile.entrySet()) {
					final byte[] gap = delta(Integer.parseInt(list.getKey()) - previous);
					accessFile.write(gap);
					previous = Integer.parseInt(list.getKey());

					final byte[] tf = delta(dictionary.get(string).getTermFreq().get(list.getKey()));
					accessFile.write(tf);

					final byte[] doclen = delta(list.getValue().getDoclen());
					accessFile.write(doclen);

					final byte[] max = delta(list.getValue().getMaxFreq());
					accessFile.write(max);
				}
			}
			writer.write(frontCodeString.toString());
		}
	}
	public static byte[] StringtoBytes(final String string) throws UnsupportedEncodingException {
		final BitSet bitSet = new BitSet(string.length());
		int index = 0;
		while (index < string.length()) {
			if (string.charAt(index) == '1') {
				bitSet.set(index);
			}
			index++;
		}
		final byte[] btob = new byte[(bitSet.length() + 7) / 8];
		int i = 0;
		while (i < bitSet.length()) {
			if (bitSet.get(i)) {
				btob[btob.length - i / 8 - 1] |= 1 << i % 8;
			}
			i++;
		}
		return btob;
	}
	public static byte[] gamma(final int number) throws UnsupportedEncodingException {
		final String unary = Integer.toBinaryString(number);
		String compressed = new String();
		int i = 1;
		while (i < unary.length()) {
			compressed = compressed + "1";
			i++;
		}

		compressed = compressed + "0" + unary.substring(1);
		final byte[] bytes = StringtoBytes(compressed);
		return bytes;
	}
	public static byte[] delta(final int number) throws UnsupportedEncodingException {
		final String unary = Integer.toBinaryString(number);
		final int len = unary.length();
		final String lenUnary = Integer.toBinaryString(len);
		String compressed = new String();
		int i = 1;
		while (i < lenUnary.length()) {
			compressed = compressed + "1";
			i++;
		}
		compressed = compressed + "0" + lenUnary.substring(1);// Gamma Changing
		compressed = compressed + unary.substring(1);// Delta Change
		final byte[] bytes = StringtoBytes(compressed);
		return bytes;
	}
	public static int minimum(final int w, final int x, final int y, final int z) {
		return Math.min(w, Math.min(x, Math.min(y, z)));
	}
}
