package techdelivery.price.barista.scrapers;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;

import java.math.BigDecimal;

public class HarveyNormanScraper implements PriceScraper {
    @Override
    public BigDecimal findPrice(String url) {

        try {
            Document doc = Jsoup.connect(url).get();

            Element span = doc.select("span").stream()
                    .filter(s -> s.attr("class").startsWith("discounted_price")).findFirst().get();

            TextNode moneyNode = (TextNode) span.childNodes().get(0);
            String moneyString = moneyNode.getWholeText().trim().replaceAll("[^\\d.]+", "");
            return new BigDecimal(moneyString);
        } catch (Throwable t) {
            t.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean handlesHost(String host) {
        return host.equalsIgnoreCase("harveynorman.ie");
    }
}
