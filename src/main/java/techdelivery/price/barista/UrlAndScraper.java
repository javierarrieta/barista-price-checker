package techdelivery.price.barista;

import techdelivery.price.barista.scrapers.PriceScraper;

public record UrlAndScraper(String url, PriceScraper scraper) {
}
