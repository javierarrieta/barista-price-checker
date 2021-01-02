package techdelivery.price.barista.scrapers;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;

import java.math.BigDecimal;

public class NorthxsouthScrapper implements PriceScraper {

    @Override
    public BigDecimal findPrice(String url) {
        try {
            Document doc = Jsoup.connect(url).get();

            Element div = doc.selectFirst("div.price--main");
            TextNode moneyNode = (TextNode) div.selectFirst("span.money").childNodes().get(0);
            String moneyString = moneyNode.getWholeText().trim().replaceAll("[^\\d.]+", "");
            return new BigDecimal(moneyString);
        } catch (Throwable t) {
            t.printStackTrace();
            return null;
        }
    }
}
