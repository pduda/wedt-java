package pl.waw.pduda.wedt;

import java.io.IOException;
import java.sql.ResultSet;
import java.util.Arrays;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

import pl.waw.pduda.classification.NaiveBayesClassifier;
import pl.waw.pduda.database.Database;
import pl.waw.pduda.files.Helper;

public class WedtClassifier 
{
	public static final String CLASSIFICATION_ATTRIBUTE = "classification";

	public static final String [] CLASSES ={"article","title","tags","author","other"};
	public static final String DEFUALT_CLASS = "other";
	public static final int MODE_TEXT = 1;
	public static final int MODE_STEM = 2;
	
	NaiveBayesClassifier classifier;
	ClassifierConfig config;
	int mode;
	private Logger logger;
	private FileHandler fh;   

	public WedtClassifier(int mode) throws SecurityException, IOException
	{
		this.classifier = new NaiveBayesClassifier();
		this.classifier.init(CLASSES);
		this.mode= mode;
		
		this.logger = Logger.getLogger("Classifier");
		this.fh = new FileHandler("logs/classifier.txt");  
		this.logger.addHandler(fh); 
		
		switch(mode)
		{
			case MODE_TEXT:
				this.config = new ClassifierConfig(false, false, "Text");
				break;
			case MODE_STEM:
				this.config = new ClassifierConfig(true, false, "Text");
				break;				
			default:
				this.config = new ClassifierConfig(false, false, "Text");
				break;
		}
		
	}
	public void train() throws Exception
	{
		
		this.logger.info("Start training");
		
		Database db = new Database();
		
		//ResultSet urls = db.select("select * from urls limit 1");
		ResultSet urls = db.select("select * from urls");
		TextBlock page=null;
		while(urls.next())
    	{
			this.logger.info("Train from url - "+ urls.getString("url"));
			int url_id = urls.getInt("id");
			org.jsoup.nodes.Document temp_doc = Jsoup.parse(urls.getString("page"));
			page = this.generateBlockRepresentation(temp_doc.text());
			
			Database db2 = new Database();
			ResultSet rs = db2.select("select * from www_blocks_parser where url_id="+url_id);
			
			TextBlock tempBlock=null;
			while(rs.next())
	    	{
				String c = rs.getString("class");
	    		org.jsoup.nodes.Document temp_block = Jsoup.parse(rs.getString("content"));
	    		
	    		tempBlock = this.generateBlockRepresentation(temp_block.text());
	    		tempBlock.setTotal(page.getNumberWords(), page.getNumberStems());
	    		
	    		//i tutaj uczenie klasyfikatora
	    		
	    		this.classifier.train(c, tempBlock.getAttributes());
	    	}
			db2.close();
	
    	}
		db.close();
		
		this.logger.info("Finished training");
	}
	private TextBlock generateBlockRepresentation(String text)
	{
		/*switch(mode)
		{
			case MODE_TEXT:
				this.config = new ClassifierConfig(false, false, "Text");
				break;
			case MODE_STEM:
				this.config = new ClassifierConfig(true, false, "Text");
				break;				
			default:
				this.config = new ClassifierConfig(false, false, "Text");
				break;
		}*/
		return new TextBlock(text,false);
	}
	public void saveClassifier()
	{
		Helper.saveObject("data/classifier_"+Integer.toString(this.mode),this.classifier) ;
	}
	public void saveClassifier(String path)
	{
		Helper.saveObject(path,this.classifier) ;
	}
	public void readClassifier()
	{
		this.classifier = (NaiveBayesClassifier) Helper.readObject("data/classifier_"+Integer.toString(this.mode)) ;
	}
	public void readClassifier(String path)
	{
		this.classifier = (NaiveBayesClassifier) Helper.readObject(path) ;
	}
	public void classify(String link) throws Exception
	{
		String tempClass = DEFUALT_CLASS;
		TextBlock page=null;
		TextBlock tempBlock=null;
		String tempContent="";
		
		org.jsoup.nodes.Document doc = Jsoup.connect(link).get();
		page = this.generateBlockRepresentation(doc.text());
		
    	Elements blocks = doc.select(Parser.BLOCKELEMENTS);
    	for(Element temp: blocks)
    	{
    		tempContent = temp.html();
    		org.jsoup.nodes.Document temp_doc = Jsoup.parse(tempContent);
    		temp_doc.select(Parser.BLOCKELEMENTS).remove();
    		//tempContent ="<"+temp.tagName()+">"+ temp_doc.body().html() +"</"+temp.tagName()+">";
    	
    		tempBlock = this.generateBlockRepresentation(temp_doc.text());
    		tempBlock.setTotal(page.getNumberWords(), page.getNumberStems());
    		
    		tempClass = this.classifier.classify(tempBlock.getAttributes());
    		temp.attr(CLASSIFICATION_ATTRIBUTE, tempClass);
    	}
    	
    	Database db = new Database();

    	db.insertClassifierResult(doc.outerHtml(),this.mode);
	}
}
