package Main_Crawler;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class SpiderLeg
{

    private static final String USER_AGENT =
            "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/535.1 (KHTML, like Gecko) Chrome/13.0.782.112 Safari/535.1";
    private List<String> links = new LinkedList<String>();
    private Document htmlDocument;

    public boolean crawl(String url)
    {
        try
        {
            Connection connection = Jsoup.connect(url).userAgent(USER_AGENT);
            Document htmlDocument = connection.get();
            this.htmlDocument = htmlDocument;
            if(connection.response().statusCode() == 200) 
            {
                System.out.println("\n**Visiting** Received web page at " + url);
            }
            if(!connection.response().contentType().contains("text/html"))
            {
                System.out.println("**Failure** Retrieved something other than HTML");
                return false;
            }
            Elements linksOnPage = htmlDocument.select("a[href]");
            System.out.println("Found (" + linksOnPage.size() + ") links");
            for(Element link : linksOnPage)
            {
                this.links.add(link.absUrl("href"));
            }
            return true;
        }
        catch(IOException ioe)
        {
            return false;
        }
    }
    
    public String getDoc()
    {
    	return this.htmlDocument.body().text();
    }

    public String getHeader()
    {
    	return this.htmlDocument.head().text();
    }

    public boolean searchForWord(String searchWord)
    {
        if(this.htmlDocument == null)
        {
            System.out.println("ERROR! Call crawl() before performing analysis on the document");
            return false;
        }
        System.out.println("Searching for the word " + searchWord + "...");
        String bodyText = this.htmlDocument.body().text();
        return bodyText.toLowerCase().contains(searchWord.toLowerCase());
    }
    
    public int countWords(String st){
    	
        int wordCount = 0;
        if (this.htmlDocument == null)
        {
        	return wordCount;
        }
        String s = this.htmlDocument.body().text();
        boolean word = false;
        int endOfLine = s.length() - 1;

        for (int i = 0; i < s.length(); i++) {
            // if the char is a letter, word = true.
            if (Character.isLetter(s.charAt(i)) && i != endOfLine) {
                word = true;
                // if char isn't a letter and there have been letters before,
                // counter goes up.
            } else if (!Character.isLetter(s.charAt(i)) && word) {
                wordCount++;
                word = false;
                // last word of String; if it doesn't end with a non letter, it
                // wouldn't count without this.
            } else if (Character.isLetter(s.charAt(i)) && i == endOfLine) {
                wordCount++;
            }
        }
        return wordCount;
    }


    public List<String> getLinks(String str)
    {
    	List<String> newlinklist = new LinkedList<String>();
    	for(int i = 0; i < this.links.size(); i++) {
    		String line = this.links.get(i);

    		if (line.contains(str) && !line.contains("#")
    				&& !line.contains("all-time") && !line.contains("past-week")
    				&& !line.contains("past-month") && !line.contains("latest-jokes")
    				&& !line.contains("joke-of-the-day") && !line.contains("popular-jokes"))
    		{
    			newlinklist.add(line);
    		}
    	}
        return newlinklist;
    }
    
    public List<String> getJokes(String curl)
    {
    	Elements newjokelist = this.htmlDocument.select("p[id~=joke_]");
    	Elements newlikelist = this.htmlDocument.select("span[id~=thumbs_up_number]");
    	Elements newdislikelist = this.htmlDocument.select("span[id~=thumbs_down_number]");
    	
    	curl += "/unknown";
    	String category = curl.split("/")[4];
    	
    	List<String> newcombinedlist = new LinkedList<String>();
    	for(int i = 0; i < newjokelist.size(); i++) {
    		String line = newjokelist.get(i).text().replaceAll(",", "^");;
    		String jokeid = newjokelist.get(i).id();
    		
    		String likeN = newlikelist.get(i).text();
    		String likeid = newlikelist.get(i).id();
    		
    		String dislikeN = newdislikelist.get(i).text();
    		String dislikeid = newdislikelist.get(i).id();
    		
    		line += ","+category;
    		line += ","+likeN;
    		line += ","+dislikeN;
    		line += ","+(Float.parseFloat(likeN)/(Float.parseFloat(likeN)+Float.parseFloat(dislikeN)));
    		line += '\n';
    		newcombinedlist.add(line);
    	}
    	
        return newcombinedlist;
    }

}
