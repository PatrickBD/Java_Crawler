package Main_Crawler;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Spider
	{
	// Fields
	private static final int MAX_PAGES_TO_SEARCH = 1000;
	private Set<String> pagesVisited = new HashSet<String>();
	private List<String> pagesToVist = new LinkedList<String>();
	private List<String> masterJokeLists = new LinkedList<String>();
	private int wordCount = 0;
	
	public void search(String url, String coreUrl) throws IOException
	{	
		String currentUrl = url;
		this.pagesVisited.add(url);
		SpiderLeg leg = new SpiderLeg();
		leg.crawl(currentUrl);
		
//		boolean success = leg.searchForWord(searchWord);
//		this.wordCount += leg.countWords(searchWord);
		this.pagesToVist.addAll(leg.getLinks(coreUrl));
		this.masterJokeLists.addAll(leg.getJokes(currentUrl));
		while(this.pagesVisited.size() < MAX_PAGES_TO_SEARCH)
		{
				currentUrl = this.nextUrl();
			if(currentUrl == "end")
			{
//				System.out.println(String.format("**Success** Word %s found at %s", searchWord, currentUrl));
//				System.out.println(String.format("**Success** Words on found page = %s", leg.countWords(searchWord)));
//				System.out.println(String.format("**Success** Words on pages before this one = %s", this.wordCount));
				System.out.println(String.format("**Success** Jokes found = %s", masterJokeLists.size()));
				
				StringBuilder sb = new StringBuilder("Joke,Category,Likes,Dislikes,Like%\n");
				for (int i = 0; i < masterJokeLists.size(); i++) {
				    sb.append(masterJokeLists.get(i));
				}
				Files.write(Paths.get("c:/Users/Valkling/Dropbox/my_personal_programs/Punny_Bot/Joke_Dataset.csv"), sb.toString().getBytes());

				break;
			}
			leg.crawl(currentUrl);
			
//			this.wordCount += leg.countWords(searchWord);
			this.pagesToVist.addAll(leg.getLinks(coreUrl));
			this.masterJokeLists.addAll(leg.getJokes(currentUrl));
		}
		System.out.println("\n**Done** Visited " + this.pagesVisited.size() + " web page(s)");
	}
	
	private String nextUrl()
	{
		String nextUrl;
		do
		{
			if(!this.pagesToVist.isEmpty()) {
				nextUrl = this.pagesToVist.remove(0);
			} else {
				nextUrl = "end";
			}
		} while(this.pagesVisited.contains(nextUrl));
		this.pagesVisited.add(nextUrl);
		return nextUrl;
	}
	
}
