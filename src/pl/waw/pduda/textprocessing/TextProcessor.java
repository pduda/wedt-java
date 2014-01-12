package pl.waw.pduda.textprocessing;

import java.io.IOException;

import org.apache.lucene.analysis.Analyzer;

public abstract class TextProcessor 
{
	public static final String TEXT= "text";
	
	Analyzer analyzer;
	abstract public String stem(String input,boolean removeStopWords) throws IOException;
	//abstract public String removeStopWords(String input) throws IOException;
}
