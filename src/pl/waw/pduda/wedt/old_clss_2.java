/*import Stemmer;

import java.sql.Timestamp;

import pl.waw.pduda.classification.NaiveBayesClassifier;
import pl.waw.pduda.files.Helper;

classifier.train();
    	
    	classifier.classify(link);
    	
    	String [] positives = {"testuje testujemy will will will will will",
    						"not not not not not not test2"};
    	String [] negatives = {"will will will will will will will",
								"not not not not not not test2 dsfasd"};
    	
    	TextBlock block= null;
    	
    	NaiveBayesClassifier bayes = new NaiveBayesClassifier();
    	bayes.init(CLASSES);
    	
    	for(String temp: positives)
    	{
    		block = new TextBlock(temp);
    		bayes.train("positive", block.getAttributes());
    	}
    	
    	for(String temp: negatives)
    	{
    		block = new TextBlock(temp);
    		bayes.train("negative", block.getAttributes());
    	}

    	// Here are two unknown sentences to classify.
    	String[] unknownText1 = "this is a day ".split(" ");
    	String[] unknownText2 = "test".split(" ");

    	System.out.println( // will output "positive"
    	    bayes.classify(unknownText1));
    	System.out.println( // will output "negative"
    	    bayes.classify(unknownText2));
    	
    	Stemmer.saveClassifier(bayes);
    	
    	System.out.println(bayes.toString());
    	
    	System.out.println("done");
    	String[] unknownText2 = "test".split(" ");
    	
    	NaiveBayesClassifier bayes =(NaiveBayesClassifier) Helper.readObject("data/serialization_test.txt") ;
    	
    	java.util.Date date= new java.util.Date();
   	    System.out.println(new Timestamp(date.getTime()));
    	System.out.println(bayes.toString());
    	
    	System.out.println(bayes.classify(unknownText2));
    	
    	date= new java.util.Date();
   	    System.out.println(new Timestamp(date.getTime()));*/