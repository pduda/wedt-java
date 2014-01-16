package pl.waw.pduda.wedt;

import java.util.ArrayList;

import pl.waw.pduda.classification.AttributeInterface;

public class DummyBlock implements AttributeInterface{

	private ArrayList<String> attrs;
	public DummyBlock()
	{
		this.attrs = new ArrayList<String>();
	}
	public void addAttrs(String [] attrs)
	{
		for(String attr:attrs)
		{
			this.attrs.add(attr);
		}
		
	}
	@Override
	public String[] getAttributes() {
		// TODO Auto-generated method stub
		return this.attrs.toArray(new String[this.attrs.size()]);
	}

}
