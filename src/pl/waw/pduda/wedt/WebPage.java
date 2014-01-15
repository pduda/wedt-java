package pl.waw.pduda.wedt;

import java.io.IOException;
import java.util.ArrayList;

import org.jsoup.Jsoup;

import pl.waw.pduda.classification.AttributeInterface;
import pl.waw.pduda.textprocessing.PolishTextProcessor;

public class WebPage implements AttributeInterface{

	public static final String SENTENCES = "CUSTOM_FIELD_SENTENECES_";
	public static final String WORD_PERCENTAGE = "CUSTOM_FIELD_WORD_PERCENTAGE_";
	public static final String STEM_PERCENTAGE = "CUSTOM_FIELD_STEM_PERCENTAGE_";
	public static final String STOPWORDS_PERCENTAGE = "CUSTOM_FIELD_STOPWORDS_PERCENTAGE_";
	
	private int sentences = 0;
	private int wordsNumber=0;
	private int stemsNumber=0;
	private String text ="";
	private String stemText ="";
	private int totalWords=1;
	private int totalStems=1;
	private boolean useStems=false;
		
	public WebPage(String html,boolean useStems) throws IOException
	{
		org.jsoup.nodes.Document temp_doc = Jsoup.parse(html);
		this.text=temp_doc.text();
		this.useStems = useStems;
		this.countWords();
		this.stemText();
		this.checkSentences();
	}
	public void setTotal(int words,int stems)
	{
		this.totalWords=words;
		this.totalStems=stems;
	}
	public int getNumberWords()
	{
		return this.wordsNumber;
	}
	public int getNumberStems()
	{
		return this.stemsNumber;
	}
	private void checkSentences()
	{
		this.sentences = this.text.split(".").length;
	}
	private void countWords()
	{
		this.wordsNumber = this.text.split(" ").length;
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

		if(this.useStems)
		{
			result.add(STEM_PERCENTAGE+Integer.toString(this.getStemsPercentageRange()));
			
			result.add(STOPWORDS_PERCENTAGE+this.getSWPercentageRange());
			
			/*String [] temp_words = this.stemText.split(" ");
			for(String temp: temp_words)
				result.add(temp);*/
		}
		else
		{
			result.add(WORD_PERCENTAGE+Integer.toString(this.getWordsPercentageRange()));
			
			/*String [] temp_words = this.text.split(" ");		
			for(String temp: temp_words)
				result.add(temp);*/
		}
		
		return result.toArray(new String[result.size()]);
	}

}
