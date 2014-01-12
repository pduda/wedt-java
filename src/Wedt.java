import java.io.*;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.en.EnglishMinimalStemmer;
import org.apache.lucene.analysis.en.PorterStemFilter;
import org.apache.lucene.analysis.tokenattributes.*;
import org.apache.lucene.analysis.util.CharArraySet;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.analysis.pl.PolishAnalyzer;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.analysis.stempel.StempelStemmer;
import org.apache.lucene.search.Query;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.*;
import org.egothor.stemmer.Trie;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


import java.sql.*;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexableField;

import pl.waw.pduda.classification.NaiveBayesClassifier;
import pl.waw.pduda.database.Database;
import pl.waw.pduda.files.Helper;
import pl.waw.pduda.wedt.Parser;
import pl.waw.pduda.wedt.TextBlock;
import pl.waw.pduda.wedt.WedtClassifier;

public class Wedt
{
	//  Database credentials

    public static void main (String[] args) throws Exception
    {
    	Logger logger = Logger.getLogger("App");
    	FileHandler fh;
    	fh = new FileHandler("logs/app.txt");   
		logger.addHandler(fh); 
		/*
    	try
    	{
    		Parser.setStartId(695);
    		Parser.parse();
    	}
    	catch(Exception e)
    	{
    		logger.info("Parser Error: " + e.toString());
    		System.exit(0);
    	}*/

    	String link = "http://technologie.gazeta.pl/internet/1,104530,15255346,Kinomaniak_tv_oglasza___The_end____To_koniec_serwisu_.html";
    	
    	try
    	{
	    	WedtClassifier classifier = new WedtClassifier(WedtClassifier.MODE_STEM);
	    	
	    	//trening
	    	//classifier.train();
	    	//classifier.saveClassifier();
	    	classifier.readClassifier();
	    	
	    	classifier.classify(link);
    	}
    	catch(Exception e)
    	{
    		logger.info("Classifier Error: " + e.toString());
    		System.exit(0);
    	}
    	System.out.println("done");
   	    //zapis
    	//classifier.saveClassifier("data/1uczenie");
    	//classifier.readClassifier("data/1uczenie");
    	
    	//klasyfikacja
    	
    	
    }
}