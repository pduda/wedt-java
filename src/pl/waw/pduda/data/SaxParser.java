package pl.waw.pduda.data;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

public class SaxParser extends DefaultHandler
{
	ArrayList<String> AtributesElements=new ArrayList<String>();
	
	ArrayList<String> ValueElements=new ArrayList<String>();
	
	StringBuffer Value = new StringBuffer();
	
	private ArrayList<XmlTagEventListener> Listeners = new ArrayList<XmlTagEventListener>();
	
	public SaxParser()
	{
	
	}
	public void parse(String xmlText) throws IOException, SAXException,ParserConfigurationException
	{
		SAXParserFactory spf = SAXParserFactory.newInstance(); 
		
	    SAXParser sp = spf.newSAXParser();
	    
		XMLReader parser= sp.getXMLReader(); 
		
		parser.setContentHandler(this);
		
		org.xml.sax.InputSource input = new InputSource();
		
		input.setCharacterStream(new StringReader(xmlText));
		
		parser.parse(input);
		
	//	parser.parse("test.xml");
	   
	}
	public void parse(File f) throws ParserConfigurationException, SAXException, IOException
	{
		SAXParserFactory spf = SAXParserFactory.newInstance(); 
		
	    SAXParser sp = spf.newSAXParser();
	    
		XMLReader parser= sp.getXMLReader(); 
		
		parser.setContentHandler(this);
		
		org.xml.sax.InputSource input = new InputSource();
		
		input.setCharacterStream(new FileReader(f));
		
		parser.parse(input);
	}
	public synchronized void addEventListener(XmlTagEventListener listener)  
	{
		Listeners.add(listener);
	}
	public synchronized void removeEventListener(XmlTagEventListener listener)   
	{
		Listeners.remove(listener);
	}
	public void addAttributesElement(String value)
	{
		this.AtributesElements.add(value);
	}
	public void addValueElement(String value)
	{
		this.ValueElements.add(value);
	}
	private synchronized void fireXmlTagEvent(XmlTagEvent event)
	{		
		for(int i=0;i<Listeners.size();i++)
		{
			Listeners.get(i).handleXmlTagEvent(event);
		}
		
	}
	public void characters(char[] buffer, int start, int length) 
	{
		Value.append(buffer, start, length);
	}

	public void startElement(String uri, String name, String qname,Attributes attributes) 
	{
	    Value.setLength(0); 
	    
	    XmlTagEvent event=new XmlTagEvent(this);
	    
	    for(int i=0; i<this.AtributesElements.size();i++)
	    {
	    	if(qname.equals(this.AtributesElements.get(i)))
	    	{
	    		event.setAttributes(attributes);
	    		
	    		event.setType(XmlTagEvent.ATTRIBUTE_ELEMENT);
	    		
	    		event.setTagName(qname);
	    		
	    		this.fireXmlTagEvent(event);
	    	}
	    }

	}
	
	public void endElement(String uri, String name, String qname) 
	{
		XmlTagEvent event=new XmlTagEvent(this);
		
		for(int i=0; i<this.ValueElements.size();i++)
		{
			if(name.equals(this.ValueElements.get(i)))
		    {
				event.setValue(this.Value.toString());
				
	    		event.setType(XmlTagEvent.VALUE_ELEMENT);
	    		
	    		event.setTagName(name);
	    		
	    		this.fireXmlTagEvent(event);
		    }
		}
	}

}
