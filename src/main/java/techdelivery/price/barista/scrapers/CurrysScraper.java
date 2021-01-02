package techdelivery.price.barista.scrapers;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;

import java.math.BigDecimal;
import java.util.Optional;

public class CurrysScraper implements PriceScraper {
    @Override
    public BigDecimal findPrice(String url) {
        //  <meta property="og:price:amount" content="579.99">
        try {
            Document doc = Jsoup.connect(url).get();

            Element meta = doc.select("meta").stream().filter(el -> "og:price:amount".equals(el.attr("property"))).findFirst().get();
            String moneyString = meta.attr("content").trim().replaceAll("[^\\d.]+", "");
            return new BigDecimal(moneyString);
        } catch (Throwable t) {
            t.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean handlesHost(String host) {
        return host.equalsIgnoreCase("currys.ie");
    }
}
