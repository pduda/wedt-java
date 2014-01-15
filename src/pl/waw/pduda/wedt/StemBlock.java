package pl.waw.pduda.wedt;
import java.io.IOException;
import java.util.ArrayList;

import pl.waw.pduda.classification.AttributeInterface;
import pl.waw.pduda.textprocessing.PolishTextProcessor;


public class StemBlock implements AttributeInterface
{
	public static final String SENTENCES = "CUSTOM_FIELD_SENTENECES_";
	public static final String WORD_PERCENTAGE = "CUSTOM_FIELD_WORD_PERCENTAGE_";
	public static final String STEM_PERCENTAGE = "CUSTOM_FIELD_STEM_PERCENTAGE_";
	public static final String STOPWORDS_PERCENTAGE = "CUSTOM_FIELD_STOPWORDS_PERCENTAGE_";
	
	private int sentences = 0;
	private int stemsNumber=0;
	private int wordsNumber=0;
	private String stemText ="";
	private int totalStems=1;
	private int totalWords=1;
	
	private String text;
		
	public StemBlock(String text,int totalWords,int totalStems) throws IOException
	{
		this.text = text;
		this.wordsNumber = this.text.split(" ").length;
		this.stemText();
		this.checkSentences();
		this.totalStems = totalStems;
		
		
	}


	private void checkSentences()
	{
		this.sentences = this.text.split(".").length;
	}

	private void stemText() throws IOException
	{
		PolishTextProcessor proc = new PolishTextProcessor();
		
		this.stemText = proc.stem(this.text, true);
		
		this.stemsNumber = this.stemText.split(" ").length;
	}
	public int getSentencesRange()
	{
		return this.getRange(this.sentences);
	}
	public int getSWPercentageRange()
	{
		int temp = 100*(this.wordsNumber-this.stemsNumber)/(this.totalWords-this.totalStems);
		return this.getRange(temp);
	}
	public int getWordsPercentageRange()
	{
		int temp = 100*this.wordsNumber / this.totalWords;
		return this.getRange(temp);
	}
	public int getStemsPercentageRange()
	{
		int temp = 100*this.stemsNumber /this.totalStems;
		return this.getRange(temp);
	}
	public String getText()
	{
		return this.text;
	}
	public String getStemmedText()
	{
		return this.stemText;
	}
	public int getSentencesNumber()
	{
		return this.sentences;
	}
	private int getRange(int value)
	{
		int i=0; //zakres to bedzie potega 2
		while(value!=0)
		{
			value/=2;
			i++;
		}
		return i;
	}
	@Override
	public String[] getAttributes() 
	{
		ArrayList<String> result = new ArrayList<String> ();
		
		result.add(SENTENCES+Integer.toString(this.getSentencesRange()));

		result.add(STEM_PERCENTAGE+Integer.toString(this.getStemsPercentageRange()));
			
		result.add(STOPWORDS_PERCENTAGE+this.getSWPercentageRange());
			
		result.add(WORD_PERCENTAGE+Integer.toString(this.getWordsPercentageRange()));
		
		return result.toArray(new String[result.size()]);
	}
	
}
