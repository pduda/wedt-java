package pl.waw.pduda.wedt;
import java.io.IOException;
import java.util.ArrayList;

import pl.waw.pduda.classification.AttributeInterface;
import pl.waw.pduda.textprocessing.PolishTextProcessor;


public class TextBlock implements AttributeInterface
{
	public static final String SENTENCES = "CUSTOM_FIELD_SENTENECES_";
	public static final String WORD_PERCENTAGE = "CUSTOM_FIELD_WORD_PERCENTAGE_";
	public static final String STEM_PERCENTAGE = "CUSTOM_FIELD_STEM_PERCENTAGE_";
	public static final String STOPWORDS_PERCENTAGE = "CUSTOM_FIELD_STOPWORDS_PERCENTAGE_";
	
	private int sentences = 0;
	private int wordsNumber=0;
	private String text ="";
	private int totalWords=1;
		
	public TextBlock(String text,int totalWords)
	{
		this.text=text;
		this.totalWords=totalWords;
		this.countWords();

		this.checkSentences();
	}

	private void checkSentences()
	{
		this.sentences = this.text.split(".").length;
	}
	private void countWords()
	{
		this.wordsNumber = this.text.split(" ").length;
	}
	
	public int getSentencesRange()
	{
		return this.getRange(this.sentences);
	}
	
	public int getWordsPercentageRange()
	{
		int temp = 100*this.wordsNumber / this.totalWords;
		return this.getRange(temp);
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

		result.add(WORD_PERCENTAGE+Integer.toString(this.getWordsPercentageRange()));
			
		return result.toArray(new String[result.size()]);
	}
	
}
