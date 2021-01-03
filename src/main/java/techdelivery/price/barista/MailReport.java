package techdelivery.price.barista;

import java.util.Arrays;

public class MailReport {
    public final String subject;
    public final String body;

    public MailReport(PriceProcessor.PriceReport report) {
        this.subject = "Sage The Barista Express lower price: %.2f".formatted(report.lowestPrice.price);
        this.body = renderHtmlBody(report);
    }

    private String renderHtmlBody(PriceProcessor.PriceReport report) {
        return """
                <html>
                  <title>Sage The Barista Express Price Report</title>
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
                </html>""".formatted(report.lowestPrice.url, report.lowestPrice.price, htmlRowsFor(report.urlAndPrices));
    }

    private String htmlRowsFor(UrlAndPrice[] urlAndPrices) {
        return Arrays.stream(urlAndPrices).map( urlAndPrice ->
                        """
                              <tr>
                                <td>%.2f&euro;<td>
                                <td><a href="%s">Link</a></td>
                              </tr>""".formatted(urlAndPrice.price, urlAndPrice.url)
                ).reduce("", (s1, s2) -> s1 + s2);
    }

}
