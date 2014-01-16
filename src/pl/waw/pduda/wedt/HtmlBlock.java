package pl.waw.pduda.wedt;

import java.util.ArrayList;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import pl.waw.pduda.classification.AttributeInterface;

public class HtmlBlock implements AttributeInterface
{
	public static final String PARAGRAPHS = "CUSTOM_FIELD_PARAGRAPHS_";
	public static final String LIST_ITEMS = "CUSTOM_FIELD_LIST_ITEMS_";
	public static final String TAG_TYPE = "CUSTOM_FIELD_TAG_TYPE_";
	public static final String TAGS_NUMBER = "CUSTOM_FIELD_TAGS_NUMBER_";
	public static final String ANCHORS = "CUSTOM_FIELD_ANCHORS_";
	
	private int paragraphs = 0;
	private int anchors = 0;
	private int listitems = 0;
	private int tagNumber =0;//liczba tagow p li a 
	private String tagName ="";
	private Document block = null;
	private Document page = null;
	
	public HtmlBlock(Document block)
	{
		this.block=block;
		this.countParagraphs();
		this.countListItems();
		this.countAnchors();
	}
	private void mainTagName()
	{
		Elements blocks =this.block.select(Parser.BLOCKELEMENTS);
    	for(Element temp: blocks)
    	{
    		this.tagName=temp.tagName();
    	}
	}
	private void countParagraphs()
	{
    	this.paragraphs=countElements("p");
	}
	private void countListItems()
	{
    	this.listitems=countElements("li");
	}
	private void countAnchors()
	{
    	this.anchors=countElements("a");
	}
	private int countElements(String element)
	{
		int i=0;
		Elements blocks =this.block.select(element);
    	for(Element temp: blocks)
    	{
    		i++;
    	}
    	this.tagNumber+=i;
    	return i;
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
		
		result.add(PARAGRAPHS+Integer.toString(this.getRange(this.paragraphs)));
		
		result.add(LIST_ITEMS+Integer.toString(this.getRange(this.listitems)));
		
		result.add(ANCHORS+Integer.toString(this.getRange(this.anchors)));
		
		result.add(TAG_TYPE+this.tagName);
		
		result.add(TAGS_NUMBER+Integer.toString(this.getRange(this.tagNumber)));

		return result.toArray(new String[result.size()]);
	}
	
}
