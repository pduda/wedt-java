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

import pl.waw.pduda.classification.AttributeInterface;
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
		
		this.mode=mode;
		
	}
	public void train() throws Exception
	{
		
		this.logger.info("Start training");
		
		Database db = new Database();
		
		//ResultSet urls = db.select("select * from urls limit 1");
		ResultSet urls = db.select("select * from urls");
		WebPage page=null;
		while(urls.next())
    	{
			this.logger.info("Train from url - "+ urls.getString("url"));
			int url_id = urls.getInt("id");
			page = new WebPage(urls.getString("page"),true);
			
			Database db2 = new Database();
			ResultSet rs = db2.select("select * from www_blocks_parser where url_id="+url_id);
			
			AttributeInterface tempBlock=null;
			while(rs.next())
	    	{
				String c = rs.getString("class");
	    		
	    		tempBlock = this.generateBlockRepresentation(rs.getString("content"),page);

	    		//i tutaj uczenie klasyfikatora
	    		
	    		this.classifier.train(c, tempBlock.getAttributes());
	    	}
			db2.close();
	
    	}
		db.close();
		
		this.logger.info("Finished training");
	}
	private AttributeInterface generateBlockRepresentation(String html,WebPage page) throws IOException
	{
		org.jsoup.nodes.Document temp_block = Jsoup.parse(html);
		
		AttributeInterface tempBlock=null;
		
		switch(this.mode)
		{
			case 1://analiza tekstu
				tempBlock = new TextBlock(temp_block.text(),page.getNumberWords());
				break;
			case 2: //analiza stemow itp
				tempBlock = new StemBlock(temp_block.text(),page.getNumberWords(),page.getNumberStems());
				break;
			case 3: //analiza htmla
				tempBlock = new HtmlBlock(temp_block);
				break;
			case 4: //analiza htmla + stem
				DummyBlock temp0 = new DummyBlock();
				HtmlBlock temp1 = new HtmlBlock(temp_block);
				StemBlock temp2 = new StemBlock(temp_block.text(),page.getNumberWords(),page.getNumberStems());
				temp0.addAttrs(temp1.getAttributes());
				temp0.addAttrs(temp2.getAttributes());
				tempBlock =temp0;
				
				break;
			default:
				tempBlock = new TextBlock(temp_block.text(),page.getNumberWords());
				break;
		}
		
		return tempBlock;
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
		WebPage page=null;
		AttributeInterface tempBlock=null;
		String tempContent="";
		
		org.jsoup.nodes.Document doc = Jsoup.connect(link).get();
		page = new WebPage(doc.outerHtml(),true);
		
    	Elements blocks = doc.select(Parser.BLOCKELEMENTS);
    	for(Element temp: blocks)
    	{
    		tempContent = temp.html();
    		org.jsoup.nodes.Document temp_doc = Jsoup.parse(tempContent);
    		temp_doc.select(Parser.BLOCKELEMENTS).remove();
    		//tempContent ="<"+temp.tagName()+">"+ temp_doc.body().html() +"</"+temp.tagName()+">";
    	
    		tempBlock = this.generateBlockRepresentation(temp_doc.outerHtml(),page);
    		
    		tempClass = this.classifier.classify(tempBlock.getAttributes());
    		temp.attr(CLASSIFICATION_ATTRIBUTE, tempClass);
    	}
    	
    	Database db = new Database();

    	db.insertClassifierResult(doc.outerHtml(),this.mode);
	}
}
