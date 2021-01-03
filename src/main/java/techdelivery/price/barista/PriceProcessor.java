package techdelivery.price.barista;

import techdelivery.price.barista.scrapers.PriceScraper;

import java.net.URL;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Stream;

public class PriceProcessor {

    public static class PriceReport {
        public final UrlAndPrice[] urlAndPrices;
        public final UrlAndPrice lowestPrice;

        public PriceReport(UrlAndPrice[] urlAndPrices, UrlAndPrice lowestPrice) {
            this.urlAndPrices = urlAndPrices;
            this.lowestPrice = lowestPrice;
        }
    }

    private final PriceScraper[] scrapers;

    public PriceProcessor(PriceScraper[] scrapers) {
        this.scrapers = scrapers;
    }

    public PriceReport process(String[] pages) {

        Stream<Optional<UrlAndScraper>> urlAndScrapers =
                Arrays.stream(pages).map(url -> scraperForUrl(url).map(scraper -> new UrlAndScraper(url, scraper)));
        UrlAndPrice[] urlAndPrice = urlAndScrapers.filter(Optional::isPresent).map(Optional::get)
                .map(PriceProcessor::scrape).sorted().toArray(UrlAndPrice[]::new);
        UrlAndPrice lowestPrice = urlAndPrice[0];

        return new PriceReport(urlAndPrice, lowestPrice);
    }


    private Optional<PriceScraper> scraperForUrl(String urlString) {
        try {
            String host = new URL(urlString).getHost().replace("www.", "");
            return Arrays.stream(scrapers).filter(s -> s.handlesHost(host)).findFirst();
        } catch (Throwable t) {
            t.printStackTrace();
            return Optional.empty();
        }
    }

    private static UrlAndPrice scrape(UrlAndScraper urlAndScraper) {
        return new UrlAndPrice(urlAndScraper.url, urlAndScraper.scraper.findPrice(urlAndScraper.url));
    }

}
