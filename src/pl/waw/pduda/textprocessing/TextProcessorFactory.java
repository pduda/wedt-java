package pl.waw.pduda.textprocessing;

public class TextProcessorFactory 
{
	private static final int ENGLISH =0;
	private static final int POLISH =1;
	
	private int language;
	private boolean useMorfo;
	
	/*public TextProcessor(int lang, boolean useMorfo)
	{
		this.language=lang;
		this.useMorfo=useMorfo;
	}*/

	public String englishRemoveStopWords(String input)
	{
		return input;
	}
	public String polishRemoveStopWords(String input)
	{
		return input;
	}
	public String englishStem(String input, boolean removeSW)
	{
		return input;
	}
	public String polishStem(String input, boolean removeSW)
	{
		return input;
	}
}
