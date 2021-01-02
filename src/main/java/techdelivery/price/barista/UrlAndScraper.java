package techdelivery.price.barista;

import techdelivery.price.barista.scrapers.PriceScraper;

public class UrlAndScraper {
    public final String url;
    public final PriceScraper scraper;

    public UrlAndScraper(String url, PriceScraper scraper) {
        this.url = url;
        this.scraper = scraper;
    }
}
