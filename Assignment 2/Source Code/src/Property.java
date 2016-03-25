/**
 * Stores the length and maximum frequency properties for a document
 *
 * @author Navaneeth.Rao
 */
public class Property {
	private int	doclen;
	private int	maxFreq;
	public final int getDoclen() {
		return this.doclen;
	}
	public final void setDoclen(final int doclen) {
		this.doclen = doclen;
	}
	public final int getMaxFreq() {
		return this.maxFreq;
	}
	public final void setMaxFreq(final int maxFreq) {
		this.maxFreq = maxFreq;
	}
}