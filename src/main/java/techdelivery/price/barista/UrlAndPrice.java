package techdelivery.price.barista;

import java.math.BigDecimal;

public class UrlAndPrice implements Comparable<UrlAndPrice> {
    public final String url;
    public final BigDecimal price;

    public UrlAndPrice (String url, BigDecimal price) {
        this.url = url;
        this.price = price;
    }
    @Override
    public int compareTo(UrlAndPrice o) {
        return this.price.compareTo(o.price);
    }
}