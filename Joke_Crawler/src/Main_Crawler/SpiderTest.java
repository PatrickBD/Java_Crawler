package Main_Crawler;
import java.io.IOException;
public class SpiderTest 
{
    public static void main(String[] args) throws IOException
    {
        Spider spider = new Spider();
        spider.search("http://www.laughfactory.com/jokes/clean-jokes", "http://www.laughfactory.com/jokes/");
        
    }
}
