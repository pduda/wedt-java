

/*package pl.waw.pduda.wedt;
import java.io.*;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.en.EnglishMinimalStemmer;
import org.apache.lucene.analysis.en.PorterStemFilter;
import org.apache.lucene.analysis.tokenattributes.*;
import org.apache.lucene.analysis.util.CharArraySet;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.analysis.pl.PolishAnalyzer;
import org.apache.lucene.analysis.snowball.SnowballAnalyzer;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.analysis.stempel.StempelStemmer;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.Query;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.*;
import org.egothor.stemmer.Trie;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.apache.lucene.analysis.morfologik.MorfologikAnalyzer;
import org.apache.lucene.analysis.morfologik.MorfologikFilter;
import org.apache.lucene.analysis.morfologik.MorphosyntacticTagsAttribute;
import org.apache.lucene.analysis.morfologik.MorphosyntacticTagsAttributeImpl;


import java.sql.*;
import org.apache.lucene.benchmark.byTask.*;
import org.apache.lucene.benchmark.byTask.feeds.DemoHTMLParser;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexableField;

public class old_Stemmer
{
	//  Database credentials
	static final String USER = "wedt";
	static final String PASS = "wedt123";
	static final String BASE = "jdbc:mysql://localhost/wedt";
	
	private static void addDoc(IndexWriter w, String title, String isbn) throws IOException {
		  Document doc = new Document();
		  doc.add(new TextField("title", title, Field.Store.YES));
		  doc.add(new StringField("isbn", isbn, Field.Store.YES));
		  w.addDocument(doc);
		}
    public static void main (String[] args) throws IOException
    {
    	String page ="";
    	//System.out.print("Sprawdzanie sterownika:");
        try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
		} catch (Exception e) {
			//System.out.println("Blad przy ladowaniu sterownika bazy!");
			//System.exit(1);
			System.out.println(e.toString());
		}
    	try
    	{
    		Connection conn = null;
        	Statement stmt = null;
        	
        	//Class.forName("com.mysql.jdbc.Driver");
        	conn = DriverManager.getConnection(BASE,USER,PASS);
        	
        	stmt = conn.createStatement();
            String sql;
            sql = "SELECT page from parser_results limit 1";
            ResultSet rs = stmt.executeQuery(sql);

        	//STEP 5: Extract data from result set
            while(rs.next())
            {
               page = rs.getString("page");
               //Display values
              // System.out.println("Page: " + page);
            }
            //STEP 6: Clean-up environment
            rs.close();
            stmt.close();
            conn.close();
    	}
    	catch(Exception e)
    	{
    		System.out.println(e.toString());
    	}
    	
    	org.jsoup.nodes.Document doc = Jsoup.connect("http://www.gemius.com/about-gemius.html").get();
    	Elements newsHeadlines = doc.select("body div");
    	int i=0;
    	String temp_text="";
    	for(Element temp: newsHeadlines)
    	{
    		temp_text = temp.html();
    		
    		org.jsoup.nodes.Document temp_doc = Jsoup.parse(temp_text);
    		
    		temp_doc.select("div").remove();
    		System.out.println(temp_doc.text());
    		
    		i++;
    	}
    	
    	System.out.println(i);
    	
    	//Trie temp2 = PolishAnalyzer.getDefaultTable();
    	//String str = "grzebałem";
    	
    	//StempelStemmer stem = new StempelStemmer(temp2);
    	
    	//stem.stem('');
    	
    	//QueryParser parser = new QueryParser(Version.LUCENE_46, "word", analizer);
    	//HtmlParser parser = new HtmlParser(Version.LUCENE_46, "word", analizer);
    	DemoHTMLParser parser = new DemoHTMLParser();
    	Query temp;
    	try 
    	{
			temp = parser.parse(str);
			System.out.println("result: " + temp.toString()); //amenit
			//temp.
		} catch (ParseException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	//EnglishAnalyzer analyzer = new EnglishAnalyzer(Version.LUCENE_46);

    	//PolishAnalyzer analyzer = new PolishAnalyzer(Version.LUCENE_46);
    	MorfologikAnalyzer analyzer = new MorfologikAnalyzer(Version.LUCENE_46);
    	
    	String input ="grzebałem sobie uchu aż tutaj nagle przyszedł psów";
    	//String input ="A sentence is a group of words that are put together to mean something";
    	
    	TokenStream tokenStream = analyzer.tokenStream("test", input);
    	OffsetAttribute offsetAttribute = tokenStream.addAttribute(OffsetAttribute.class);
    	CharTermAttribute charTermAttribute = tokenStream.addAttribute(CharTermAttribute.class);
    	
    	MorphosyntacticTagsAttribute morfo = tokenStream.addAttribute(MorphosyntacticTagsAttribute.class);
    	

    	tokenStream.reset();
    	while (tokenStream.incrementToken()) {
    	    int startOffset = offsetAttribute.startOffset();
    	    int endOffset = offsetAttribute.endOffset();
    	    String term = charTermAttribute.toString() + morfo.toString();

    	    System.out.println(term);

    	}
    	
       // System.out.println(sb.toString());
    	
        //System.out.println(tokenStream.getAttribute(CharTermAttribute.class).toString());
    }
}
public static void parseAntyWeb(String link,int link_id) throws Exception
	{
		org.jsoup.nodes.Document doc = Jsoup.connect(link).get();
		int i=0;
		String tcontent="",ttitle="",tauthor="",ttags="";
		
		Elements content = doc.select("article div.news-content");
		for(Element temp: content)
    	{
			temp.attr("analyzed", "1");
			tcontent = temp.html();
    		
    		org.jsoup.nodes.Document temp_doc = Jsoup.parse(tcontent);
    		
    		temp_doc.select(BLOCKELEMENTS).remove();
    		//tcontent = temp_doc.html();
    		tcontent ="<"+temp.tagName()+">"+ temp_doc.body().html() +"</"+temp.tagName()+">";
    		i++;
    	}
		//System.out.println(i);i=0;
		
		Elements title = doc.select("article h1.entry-title");
		for(Element temp: title)
    	{
			ttitle = temp.html();
			temp.attr("analyzed", "1");
    		
    		org.jsoup.nodes.Document temp_doc = Jsoup.parse(ttitle);
    		
    		temp_doc.select(BLOCKELEMENTS).remove();
    	//	ttitle = temp_doc.html();
    		ttitle ="<"+temp.tagName()+">"+ temp_doc.body().html() +"</"+temp.tagName()+">";
    		i++;
    	}
		//System.out.println(i);i=0;
		
		Elements author = doc.select("article div.entry-meta div.author > a");
		for(Element temp: author)
    	{
			temp.attr("analyzed", "1");
			tauthor = temp.html();
    		
    		org.jsoup.nodes.Document temp_doc = Jsoup.parse(tauthor);
    		
    		temp_doc.select(BLOCKELEMENTS).remove();
    		//tauthor = temp_doc.html();
    		tauthor ="<"+temp.tagName()+">"+ temp_doc.body().html() +"</"+temp.tagName()+">";
    		i++;
    	}
		//System.out.println(i);i=0;
		
		Elements tags = doc.select("article div.tags");
		for(Element temp: tags)
    	{
			temp.attr("analyzed", "1");
			ttags = temp.html();
    		
    		org.jsoup.nodes.Document temp_doc = Jsoup.parse(ttags);
    		
    		temp_doc.select(BLOCKELEMENTS).remove();
    		//ttags = temp_doc.html();
    		ttags ="<"+temp.tagName()+">"+ temp_doc.body().html() +"</"+temp.tagName()+">";
    		i++;
    	}
		//System.out.println(i);i=0;

		/*System.out.println(tcontent);
		System.out.println(ttitle);
		System.out.println(tauthor);
		System.out.println(ttags);*/
		
		/*Database db = new Database();
		
		db.insertBlockText(tcontent, "article",link_id);
		db.insertBlockText(ttitle, "title",link_id);
		db.insertBlockText(tauthor, "tauthor",link_id);
		db.insertBlockText(ttags, "tags",link_id);
		
		i=0;int j=0;
		String temp_other="";
		Elements others = doc.select(BLOCKELEMENTS);
		for(Element temp: others)
    	{
			j++;
			if(temp.hasAttr("analyzed"))
				continue;
			temp_other = temp.html();
    		
    		org.jsoup.nodes.Document temp_doc = Jsoup.parse(temp_other);
    		
    		temp_doc.select(BLOCKELEMENTS).remove();
    		//ttags = temp_doc.html();
    		temp_other ="<"+temp.tagName()+">"+ temp_doc.body().html() +"</"+temp.tagName()+">";
    		i++;
    		db.insertBlockText(temp_other, "other",link_id);
    	}
		//System.out.println(i);
		//System.out.println(j);
	}*/
