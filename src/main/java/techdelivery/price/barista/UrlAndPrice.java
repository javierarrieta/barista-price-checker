package techdelivery.price.barista;

import java.math.BigDecimal;

public record UrlAndPrice (String url, BigDecimal price) implements Comparable<UrlAndPrice> {
    @Override
    public int compareTo(UrlAndPrice o) {
        return this.price.compareTo(o.price);
    }
}