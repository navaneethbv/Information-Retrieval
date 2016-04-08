import java.util.LinkedList;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
public class Formatter 
{
	private final List<String[]>	rows	= new LinkedList<String[]>();
	public void addRow(final String... columns) {
		this.rows.add(columns);
	}
	private int[] colWidths() {
		int columns = -1;
		for (final String[] row : this.rows) {
			columns = Math.max(columns, row.length);
		}
		final int[] widths = new int[columns];
		for (final String[] row : this.rows) {
			for (int colNum = 0; colNum < row.length; colNum++) {
				widths[colNum] = Math.max(widths[colNum], StringUtils.length(row[colNum]));
			}
		}
		return widths;
	}
	public String toString() {
		final StringBuilder buf = new StringBuilder();
		final int[] colWidths = this.colWidths();
		for (final String[] row : this.rows) {
			for (int colNum = 0; colNum < row.length; colNum++) {
				buf.append(StringUtils.rightPad(StringUtils.defaultString(row[colNum]), colWidths[colNum]));
				buf.append('\t');
			}
			buf.append('\n');
		}
		return buf.toString();
	}
}
