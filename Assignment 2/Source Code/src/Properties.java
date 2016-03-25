import java.util.HashMap;
import java.util.Map;
public class Properties {
	private int	docuFreq;
	private Map<String, Property>	postingFile	= new HashMap<>();
	private Map<String, Integer>	termFreq	= new HashMap<>();

	public final Map<String, Integer> getTermFreq() {
		return this.termFreq;
	}
	public final void setTermFreq(final Map<String, Integer> termFreq) {
		this.termFreq = termFreq;
	}
	public final int getDocFreq() {
		return this.docuFreq;
	}
	public final void setDocFreq(final int docuFreq) {
		this.docuFreq = docuFreq;
	}
	public final Map<String, Property> getPostingFile() {
		return this.postingFile;
	}
	public final void setPostingFile(final Map<String, Property> postingFile) {
		this.postingFile = postingFile;
	}

}