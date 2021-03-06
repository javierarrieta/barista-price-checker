package techdelivery.price.barista.scrapers;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.TextNode;

import java.math.BigDecimal;

public class ElectrocityScraper implements PriceScraper {
    @Override
    public BigDecimal findPrice(String url) {
        try {
            Document doc = Jsoup.connect(url).get();

            TextNode moneyNode = (TextNode) doc.select("span.woocommerce-Price-amount").get(1).child(0).childNodes().get(1);
            String moneyString = moneyNode.getWholeText().trim().replaceAll("[^\\d.]+", "");
            return new BigDecimal(moneyString);
        } catch (Throwable t) {
            t.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean handlesHost(String host) {
        return host.equals("electrocity.ie");
    }
}
