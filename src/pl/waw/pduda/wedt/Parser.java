package pl.waw.pduda.wedt;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Hashtable;
import java.util.Map;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import pl.waw.pduda.database.Database;

public class Parser 
{
	public static final String BLOCKELEMENTS = "div,h1, h2, h3, article";
	private static Logger logger = Logger.getLogger("Parser");
	private static FileHandler fh;   
	private static int startId=0;
	
	public static void setStartId(int id)
	{
		startId=id;
	}
	public static void parseTest(String link) throws Exception
	{
		
		org.jsoup.nodes.Document doc = Jsoup.connect(link).get();
    	Elements newsHeadlines = doc.select(BLOCKELEMENTS);
    	int i=0;
    	String temp_text="";
    	for(Element temp: newsHeadlines)
    	{
    		//temp_text = temp.html();
    		
    		//org.jsoup.nodes.Document temp_doc = Jsoup.parse(temp_text);
    		
    		//temp_doc.select(BLOCKELEMENTS).remove();
    		//System.out.println(temp_doc.text());
    		
    		i++;
    	}
    	
    	System.out.println(i);
	}
	public static void parseAntyweb(String link,int link_id) throws Exception
	{
		Map<String,String> selectors = new Hashtable<String, String>();
		
		selectors.put("article", "article div.news-content");
		selectors.put("title", "article h1.entry-title");
		selectors.put("author", "article div.entry-meta div.author");
		selectors.put("tags", "article div.tags");
		
		Parser.parseSite(link, link_id, selectors);
	}
	public static void parseGazeta(String link,int link_id) throws Exception
	{
		Map<String,String> selectors = new Hashtable<String, String>();
		
		selectors.put("article", "#gazeta_article #article #article_body div");
		selectors.put("title", "#top_wrap h1");
		selectors.put("author", "#gazeta_article #gazeta_article_author");
		selectors.put("tags", "#gazeta_article #gazeta_article_tags");
		
		Parser.parseSite(link, link_id, selectors);
	}
	public static void parseTechcrunch(String link,int link_id) throws Exception
	{
		Map<String,String> selectors = new Hashtable<String, String>();
		
		selectors.put("article", "div.l-main div.article-entry");
		selectors.put("title", "div.l-main header h1");
		selectors.put("author", "div.l-main header div.title-left div.byline");
		selectors.put("tags", "");
		
		Parser.parseSite(link, link_id, selectors);
	}
	
	
	public static void parseSite(String link,int link_id,Map<String,String> selectors) throws Exception
	{
		logger.info("Parsing - " + link);
		
		org.jsoup.nodes.Document doc = Jsoup.connect(link).get();
		String temp_content="";
		Database db = new Database();
		
		for(String sel : selectors.keySet())
		{
			if(selectors.get(sel)!="")
			{
				Elements content = doc.select(selectors.get(sel));
				for(Element temp: content)
		    	{
					temp.attr("analyzed", "1");
					temp_content = temp.html();
		    		
		    		org.jsoup.nodes.Document temp_doc = Jsoup.parse(temp_content);
		    		
		    		temp_doc.select(BLOCKELEMENTS).remove();
		    		//tcontent = temp_doc.html();
		    		temp_content ="<"+temp.tagName()+">"+ temp_doc.body().html() +"</"+temp.tagName()+">";
		    		
		    		db.insertBlockText(temp_content, sel,link_id);
		    	}
			}
		}
		
		String temp_other="";
		Elements others = doc.select(BLOCKELEMENTS);
		for(Element temp: others)
    	{

			if(temp.hasAttr("analyzed"))
				continue;
			temp_other = temp.html();
    		
    		org.jsoup.nodes.Document temp_doc = Jsoup.parse(temp_other);
    		
    		temp_doc.select(BLOCKELEMENTS).remove();
    		//ttags = temp_doc.html();
    		temp_other ="<"+temp.tagName()+">"+ temp_doc.body().html() +"</"+temp.tagName()+">";

    		db.insertBlockText(temp_other, "other",link_id);
    	}
		db.close();
		
		//System.out.println(j);
	}
	public static void parse() throws Exception
	{
		fh = new FileHandler("logs/parser.txt");   
		logger.addHandler(fh); 
		
		logger.info("Parser started");
		
		String link ="";
    	int link_id =0;
    	int domain_id=0;
    	Database db = new Database();
    	//ResultSet rs = db.select("select id,url,domain_id from urls"); 
    	ResultSet rs = db.select("select id,url,domain_id from urls where (domain_id=2 or domain_id=3) and id >="+Integer.toString(startId));
    	//int i=0;
    	while(rs.next())
    	{
    		link = rs.getString("url");
    		link_id = rs.getInt("id");
    		domain_id = rs.getInt("domain_id");
    		switch(domain_id)
    		{
    			case 1:
    				Parser.parseTechcrunch(link,link_id);
    				break;
    			case 2:
    				Parser.parseAntyweb(link, link_id);
    				break;
    			case 3:
    				Parser.parseGazeta(link,link_id);
    				break;
    			default:
    				break;
    		}
    		//Parser.parseAntyWeb(link,link_id);
    		//i++;
    		//System.out.println(link);
    		
    	}

    	db.close();
    	logger.info("Parser finished");
	}
}
