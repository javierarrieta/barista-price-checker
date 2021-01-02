package techdelivery.price.barista;

import techdelivery.price.barista.scrapers.DidScraper;
import techdelivery.price.barista.scrapers.JoycesScraper;
import techdelivery.price.barista.scrapers.NorthxsouthScrapper;
import techdelivery.price.barista.scrapers.PriceScraper;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {

    private static final Logger logger = Logger.getLogger(Main.class.getName());

    public static Function<UrlAndScraper, UrlAndPrice> f = urlAndScraper ->
        new UrlAndPrice(urlAndScraper.url(), urlAndScraper.scraper().findPrice(urlAndScraper.url()));

    private static PriceScraper nortxsouthScrapper = new NorthxsouthScrapper();
    private static PriceScraper didScrapper = new DidScraper();
    private static PriceScraper joycesScrapper = new JoycesScraper();

    private static Function<String, PriceScraper> scraperForUrl = urlString -> {
        try {
            String host = new URL(urlString).getHost().replace("www.", "");
            if(host.equalsIgnoreCase("northxsouth.ie"))
                return nortxsouthScrapper;
            else if(host.equalsIgnoreCase("did.ie"))
                return didScrapper;
            else if(host.equalsIgnoreCase("joyces.ie"))
                return joycesScrapper;
            return null;
        } catch (Exception e) {
            return null;
        }
    };

    public static void main(String[] args) {

        String[] pages = {
                "https://northxsouth.ie/collections/bean-to-cup-coffee-machines/products/sage-by-heston-blumenthal-barista-express-bean-to-cup-coffee-machine-bes875uk",
                "https://northxsouth.ie/collections/bean-to-cup-coffee-machines/products/sage-barista-express-bean-to-cup-coffee-machine-bes875-black",
                "https://www.did.ie/sage-the-barista-express-been-to-cup-coffee-machine-black-sesame-ses875bks2guk-ses875bks2guk-prd",
                "https://www.joyces.ie/product/sage-the-barista-express-coffee-machine-back-ses875bks2guk1/"
        };

        UrlAndPrice lower = lowerPrice(pages);

        System.out.println("Lower price found: %.2f at %s".formatted(lower.price(), lower.url()));

    }

    private static Function<UrlAndPrice, UrlAndPrice> logUrlAndPriceProcessed = urlAndPrice -> {
        logger.log(Level.INFO, "Processed price for '%s': %s".formatted(urlAndPrice.url(), urlAndPrice.price()));
        return urlAndPrice;
    };

    public static UrlAndPrice lowerPrice(String[] pages) {
        return Arrays.stream(pages).map(url -> new UrlAndScraper(url, scraperForUrl.apply(url))).map(f)
                .map(logUrlAndPriceProcessed).sorted().findFirst().get();
    }

}
