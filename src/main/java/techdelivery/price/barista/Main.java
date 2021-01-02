package techdelivery.price.barista;

import techdelivery.price.barista.scrapers.*;

import java.net.URL;
import java.util.Arrays;
import java.util.Optional;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;

public class Main {

    private static final Logger logger = Logger.getLogger(Main.class.getName());

    public static Function<UrlAndScraper, UrlAndPrice> f = urlAndScraper ->
        new UrlAndPrice(urlAndScraper.url, urlAndScraper.scraper.findPrice(urlAndScraper.url));

    private static PriceScraper[] scrapers =  { new NorthxsouthScrapper(),  new DidScraper(), new JoycesScraper(),
        new HarveyNormanScraper(), new CurrysScraper() };

    private static Function<String, Optional<PriceScraper>> scraperForUrl = urlString -> {
        try {
            String host = new URL(urlString).getHost().replace("www.", "");
            return Arrays.stream(scrapers).filter(s -> s.handlesHost(host)).findFirst();
        } catch (Throwable t) {
            t.printStackTrace();
            return Optional.empty();
        }
    };

    private static final String[] pages = {
            "https://northxsouth.ie/collections/bean-to-cup-coffee-machines/products/sage-by-heston-blumenthal-barista-express-bean-to-cup-coffee-machine-bes875uk",
            "https://northxsouth.ie/collections/bean-to-cup-coffee-machines/products/sage-barista-express-bean-to-cup-coffee-machine-bes875-black",
            "https://www.did.ie/sage-the-barista-express-been-to-cup-coffee-machine-black-sesame-ses875bks2guk-ses875bks2guk-prd",
            "https://www.joyces.ie/product/sage-the-barista-express-coffee-machine-back-ses875bks2guk1/",
//                "https://www.harveynorman.ie/small-appliances/coffee-machines/bean-2-cup/sage-barista-express-espresso-coffee-machine-bes875uk-stainless-steel.html",
            "https://www.currys.ie/ieen/household-appliances/small-kitchen-appliances/coffee-machines-and-accessories/coffee-machines/sage-barista-express-bes875uk-bean-to-cup-coffee-machine-silver-10174955-pdt.html"
    };

    public static void main(String[] args) throws Exception {

        String smtpHost = System.getenv("SMTP_HOSTNAME");
        int smtpPort = Integer.parseInt(System.getenv("SMTP_PORT"));
        String smtpUsername = System.getenv("SMTP_USERNAME");
        String smtpPassword = System.getenv("SMTP_PASSWORD");
        boolean starttls = Optional.ofNullable(System.getenv("SMTP_STARTTLS")).map(Boolean::parseBoolean).orElse(true);

        String mailFrom = System.getenv("MAIL_FROM");
        String mailRecipient = System.getenv("MAIL_RECIPIENT");

        MailSender sender = new MailSender(smtpHost, smtpPort, starttls, smtpUsername, smtpPassword);

        UrlAndPrice lower = lowerPrice(pages);

        String mailSubject = "Sage The Barista Express lower price";
        String mailBody = "Lower price found: %.2f at %s%n".formatted(lower.price, lower.url);

        sender.sendMail(mailFrom, mailRecipient, mailSubject, mailBody);

    }

    private static Function<UrlAndPrice, UrlAndPrice> logUrlAndPriceProcessed = urlAndPrice -> {
        logger.log(Level.INFO, "Processed price for '%s': %s".formatted(urlAndPrice.url, urlAndPrice.price));
        return urlAndPrice;
    };

    public static UrlAndPrice lowerPrice(String[] pages) {
        Stream<Optional<UrlAndScraper>> urlAndScrapers =
                Arrays.stream(pages).map(url -> scraperForUrl.apply(url).map(scraper -> new UrlAndScraper(url, scraper)));
        return urlAndScrapers.filter(Optional::isPresent).map(Optional::get).map(f)
                .map(logUrlAndPriceProcessed).sorted().findFirst().get();
    }

}
