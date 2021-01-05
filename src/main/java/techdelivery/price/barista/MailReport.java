package techdelivery.price.barista;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;

public class MailReport {
    public static final String LINK = "Link";

    public final String subject;
    public final String body;

    public MailReport(PriceProcessor.PriceReport report) {
        this.subject = "%s lowest price: %.2f at %s".formatted(report.productName, report.lowestPrice.price, siteName(report.lowestPrice.url));
        this.body = renderHtmlBody(report);
    }

    private String renderHtmlBody(PriceProcessor.PriceReport report) {
        return """
                <html>
                  <title>%s Price Report</title>
                  <body style="font-family: sans-serif">
                    <p>Lowest price found: <a href="%s">%.2f&euro;</a></p>
                    <p>Full report</p>
                    <table>
                      <th>
                        <td>Price</td>
                        <td>Url</td>
                      </th>
                      %s
                    </table>
                  </body>
                </html>""".formatted(report.productName, report.lowestPrice.url, report.lowestPrice.price, htmlRowsFor(report.urlAndPrices));
    }

    private String htmlRowsFor(UrlAndPrice[] urlAndPrices) {
        return Arrays.stream(urlAndPrices).map( urlAndPrice ->
                        """
                              <tr>
                                <td>%.2f&euro;<td>
                                <td><a href="%s">%s</a></td>
                              </tr>""".formatted(urlAndPrice.price, urlAndPrice.url, siteName(urlAndPrice.url))
                ).reduce("", (s1, s2) -> s1 + s2);
    }

    private String siteName(String url) {
        try {
            return new URL(url).getHost().replace("www.", "");
        } catch (MalformedURLException malformedURLException) {
            return LINK;
        }
    }

}
