package pl.waw.pduda.data;

import java.util.EventObject;
import org.xml.sax.Attributes;

public class XmlTagEvent extends EventObject{
	
	Attributes Attributes=null;
	
	String Value="";
	
	String TagName="";
	
	public static final int ATTRIBUTE_ELEMENT =1;
	
	public static final int VALUE_ELEMENT =2;
	
	int Type;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public XmlTagEvent(Object source)
	{
		super(source);
	}
	public void setType(int type)
	{
		this.Type=type;
	}
	public void setAttributes(Attributes attr)
	{
		this.Attributes=attr;
	}
	public void setValue(String value)
	{
		this.Value=value;
	}
	public void setTagName(String tag)
	{
		this.TagName=tag;
	}
	public int getType()
	{
		return this.Type;
	}
	public Attributes getAttributes()
	{
		return this.Attributes;
	}
	public String getTagName()
	{
		return this.TagName;
	}
	public String getValue()
	{
		return this.Value;
	}

}
