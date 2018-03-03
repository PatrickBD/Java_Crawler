package Main_Crawler;

public class SpiderTest 
{
    public static void main(String[] args)
    {
        Spider spider = new Spider();
        spider.search("http://www.dictionary.com/", "pickle", "dictionary.com/browse/");
    }
}
