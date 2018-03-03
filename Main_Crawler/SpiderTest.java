package Main_Crawler;

public class SpiderTest 
{
    public static void main(String[] args)
    {
        Spider spider = new Spider();
        spider.search("https://en.wikipedia.org/wiki/Main_Page", "pickle");
    }
}
