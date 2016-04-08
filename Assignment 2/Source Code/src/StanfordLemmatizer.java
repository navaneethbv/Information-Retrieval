import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import edu.stanford.nlp.ling.CoreAnnotations.LemmaAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;
public class StanfordLemmatizer
{
	protected StanfordCoreNLP	pipeline;
	public StanfordLemmatizer() {
		Properties props;
		props = new Properties();
		props.put("annotators", "tokenize, ssplit, pos, lemma");
		this.pipeline = new StanfordCoreNLP(props);
	}
	public List<String> lemmatize(final String documentText) {
		final List<String> lemmas = new LinkedList<String>();
		final Annotation document = new Annotation(documentText);
		this.pipeline.annotate(document);
		final List<CoreMap> sentences = document.get(SentencesAnnotation.class);
		for (final CoreMap sentence : sentences) {
			for (final CoreLabel token : sentence.get(TokensAnnotation.class)) {
				lemmas.add(token.get(LemmaAnnotation.class));
			}
		}
		return lemmas;
	}
}
