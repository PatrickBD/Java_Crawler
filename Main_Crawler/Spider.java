package Main_Crawler;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class Spider 
{
	// Fields
	private static final int MAX_PAGES_TO_SEARCH = 1000;
	private Set<String> pagesVisited = new HashSet<String>();
	private List<String> pagesToVist = new LinkedList<String>();
	private int wordCount = 0;
	
	public void search(String url, String searchWord, String coreUrl)
	{
		while(this.pagesVisited.size() < MAX_PAGES_TO_SEARCH)
		{
			String currentUrl;
			SpiderLeg leg = new SpiderLeg();
			if(this.pagesToVist.isEmpty())
			{
				currentUrl = url;
				this.pagesVisited.add(url);
			}
			else
			{
				currentUrl = this.nextUrl();
			}
			leg.crawl(currentUrl);
			
			boolean success = leg.searchForWord(searchWord);
			if(success)
			{
				System.out.println(leg.getHeader());
				System.out.println(String.format("**Success** Word %s found at %s", searchWord, currentUrl));
				System.out.println(String.format("**Success** Words on found page = %s", leg.countWords(searchWord)));
				System.out.println(String.format("**Success** Words on pages before this one = %s", this.wordCount));
				break;
			}
			this.wordCount += leg.countWords(searchWord);
			this.pagesToVist.addAll(leg.getLinks(coreUrl));
		}
		System.out.println("\n**Done** Visited " + this.pagesVisited.size() + " web page(s)");
	}
	
	private String nextUrl()
	{
		String nextUrl;
		do
		{
			nextUrl = this.pagesToVist.remove(0);
		} while(this.pagesVisited.contains(nextUrl));
		this.pagesVisited.add(nextUrl);
		return nextUrl;
	}
	
}
