package techdelivery.price.barista.scrapers;

import java.math.BigDecimal;

public interface PriceScraper {

    BigDecimal findPrice(String url);

    boolean handlesHost(String host);

}
