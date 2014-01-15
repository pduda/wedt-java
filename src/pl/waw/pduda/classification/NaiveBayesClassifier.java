package pl.waw.pduda.classification;

import java.io.Serializable;
import java.util.Hashtable;
import java.util.Map;

public class NaiveBayesClassifier implements ClassifierInterface,Serializable
{

	private static final long serialVersionUID = 1L;
	private static final double MIN_PROB = 0.000000000000000001;
	
	private Map<String,Integer> classes; //id klasy=> ilosc klas
	private Map<String,Double> classProb; //id klasy => pstwo
	private boolean isTrained = false;
	
	private Map<String,Integer> attributes; //id atrybutu => ilosc
	//private Map<String,Double> attrProb; //id atrybutu => pstwo
	
	private Map<String, Map<String, Integer>> attributesNumberPerClass; //id atrybutu => (id klasy => ilosc)
	
	private void attributeOccured(String element,String c)
	{
		Map<String, Integer> tempE = this.attributesNumberPerClass.get(c);

        if (tempE == null) // pierwszy raz element w klasie
        {
            this.attributesNumberPerClass.put(c,new Hashtable<String, Integer>());       
            tempE = this.attributesNumberPerClass.get(c);
        }
        
        Integer count = tempE.get(element);
        
        if (count == null) 
        {
        	tempE.put(element, 0);
            count = tempE.get(element);
        }
        
        tempE.put(element, ++count);
        Integer totalCount = this.attributes.get(element);
        
        if (totalCount == null) // pierwszy raz element
        {
            this.attributes.put(element, 0);
            totalCount = this.attributes.get(element);
        }
        
        this.attributes.put(element, ++totalCount);
	}
	
	private void classOccured(String c)
	{
		Integer count = this.classes.get(c);
        if (count == null) 
        {
            this.classes.put(c, 0);
            count = this.classes.get(c);
        }
       this.classes.put(c, ++count);
	}
	private double countAttributesProbability(String [] attrs,String c)
	{
		double prob = 1.0;
		
		for(String temp: attrs)
		{
			prob *= this.getAttributeProbability(temp, c);
		}
		
		return prob;
	}
	private void countClassProbabilities(String [] attrs) 
	{
		double tempProb =0.0;
		
		for(String temp: this.classes.keySet())
		{
			tempProb = this.countAttributesProbability(attrs,temp) * ((double)this.getClassOccurences(temp)/(double)this.getClassesTotalOccurences());
			this.classProb.remove(temp);
			this.classProb.put(temp,tempProb);
		}
    }
	public int getAttributeOccurences(String attr, String c) 
	{
        Map<String, Integer> attrs = this.attributesNumberPerClass.get(c);
        
        if (attrs == null)
            return 0;
        
        Integer count = attrs.get(attr);
        
        if(count == null)
        	return 0;
        else
        	return count.intValue();
    }
	public int getClassOccurences(String c) 
	{
        Integer count = this.classes.get(c);
        
        if(count == null)
        	return 0;
        else
        	return count.intValue();
    }
	public double getAttributeProbability(String attr, String c) // P(A|B) = P(A wsp B ) / P(B)
	{
		if (this.getClassOccurences(c) == 0)
            return 0;
		
		double a_wsp_b= (double) this.getAttributeOccurences(attr, c) / (double) this.getAttributeTotalOccurences(attr);
		a_wsp_b += MIN_PROB; // zeby nie zerowalo
		
		return a_wsp_b / (double) this.getClassOccurences(c);
	}
	public int getAttributeTotalOccurences(String attr)
	{
		Integer count = this.attributes.get(attr);
        
        if(count == null)
        	return 1;//wtedy 1 zeby nie bylo bledow przy dzieleniu
        else
        	return count.intValue();
	}
	public int getClassesTotalOccurences() 
	{
        int toReturn = 0;
        
        for(Integer temp : this.classes.values())
        {
        	toReturn += temp;
        }
        
        return toReturn;
    }
	
	@Override
	public void init(String[] classes) 
	{
		this.reset();
		
		for(String temp:classes)
			this.classes.put(temp,0);
		
	}

	@Override
	public void reset() 
	{
        this.attributesNumberPerClass =new Hashtable<String, Map<String,Integer>>();
        this.attributes =new Hashtable<String, Integer>();
        this.classes = new Hashtable<String, Integer>();
        this.classProb = new Hashtable<String, Double>();
    }

	@Override
	public void train(String c, String [] attrs) 
	{
		for (String temp: attrs)
            this.attributeOccured(temp, c);
		
        this.classOccured(c);
	}

	@Override
	public String classify(String [] data) 
	{
		if(!this.isTrained)
			this.countClassProbabilities(data);
		
		String resultClass = "";
		double maxProb=0.0;
		
		for(String temp : this.classes.keySet())
		{
			if(maxProb<=this.classProb.get(temp))
			{
				maxProb=this.classProb.get(temp);
				resultClass= temp;
			}
			//System.out.println(temp+" "+this.classProb.get(temp));
		}
		
		return resultClass;
	}
	
	@Override
	public String toString()
	{
		StringBuilder rs = new StringBuilder();
		
		rs.append("\nNaiveBayesClassifier\n");
		
		rs.append("Classes Number - " + this.classes.size()+"\n");
		
		int analyzedBlocks= 0;
		for(int temp: this.classes.values())
		{
			analyzedBlocks +=temp;
		}
		rs.append("Analyzed Blocks - " + analyzedBlocks+"\n");
		
		rs.append("Attributes Number - " +this.attributes.size()+"\n");
		
		rs.append("\n");
		
		return rs.toString();
	}

}
