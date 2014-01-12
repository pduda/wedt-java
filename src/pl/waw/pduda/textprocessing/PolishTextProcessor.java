package pl.waw.pduda.textprocessing;

import java.io.IOException;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.pl.PolishAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.util.Version;

public class PolishTextProcessor extends TextProcessor
{
	
	@Override
	public String stem(String input, boolean removeStopWords) throws IOException 
	{
		this.analyzer = new PolishAnalyzer(Version.LUCENE_46);
		
		StringBuilder result = new StringBuilder();
		
		TokenStream tokenStream = this.analyzer.tokenStream(TEXT, input);
    	//OffsetAttribute offsetAttribute = tokenStream.addAttribute(OffsetAttribute.class);
    	CharTermAttribute charTermAttribute = tokenStream.addAttribute(CharTermAttribute.class);

    	tokenStream.reset();
    	
    	while (tokenStream.incrementToken()) 
    	{
    	   // int startOffset = offsetAttribute.startOffset();
    	   // int endOffset = offsetAttribute.endOffset();
    	  //  String term = charTermAttribute.toString();

    	    result.append(charTermAttribute.toString());
    	    result.append(" ");

    	}
    	
    	return result.toString();
	}

	/*@Override
	public String removeStopWords(String input) {
		// TODO Auto-generated method stub
		return input;
	}*/

}
