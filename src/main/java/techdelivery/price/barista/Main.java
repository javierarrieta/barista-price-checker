package techdelivery.price.barista;

import techdelivery.price.barista.scrapers.*;

import java.util.Optional;

public class Main {

    private static final ProductData baristaExpress = new ProductData(
            "Sage The Barista Express",
            new String[]{
                    "https://northxsouth.ie/collections/bean-to-cup-coffee-machines/products/sage-by-heston-blumenthal-barista-express-bean-to-cup-coffee-machine-bes875uk",
                    "https://northxsouth.ie/collections/bean-to-cup-coffee-machines/products/sage-barista-express-bean-to-cup-coffee-machine-bes875-black",
                    "https://www.did.ie/sage-the-barista-express-been-to-cup-coffee-machine-black-sesame-ses875bks2guk-ses875bks2guk-prd",
                    "https://www.joyces.ie/product/sage-the-barista-express-coffee-machine-back-ses875bks2guk1/",
//                "https://www.harveynorman.ie/small-appliances/coffee-machines/bean-2-cup/sage-barista-express-espresso-coffee-machine-bes875uk-stainless-steel.html",
                    "https://www.currys.ie/ieen/household-appliances/small-kitchen-appliances/coffee-machines-and-accessories/coffee-machines/sage-barista-express-bes875uk-bean-to-cup-coffee-machine-silver-10174955-pdt.html",
                    "https://www.argos.ie/static/Product/partNumber/7985692.htm"
            }
    );

    public static void main(String[] args) throws Exception {

        PriceScraper[] scrapers = {new NorthxsouthScrapper(), new DidScraper(), new JoycesScraper(),
                new HarveyNormanScraper(), new CurrysScraper(), new ArgosScraper()};

        Optional<MailSender> sender = createMailSender();

        PriceProcessor priceProcessor = new PriceProcessor(scrapers);

        String mailFrom = System.getenv("MAIL_FROM");
        String mailRecipient = System.getenv("MAIL_RECIPIENT");

        PriceProcessor.PriceReport report = priceProcessor.process(baristaExpress);

        MailReport mailReport = new MailReport(report);

        if (sender.isPresent()) {
            sender.get().sendMail(mailFrom, mailRecipient, mailReport.subject, mailReport.body);
        } else {
            System.out.printf("Subject:\n%s\n\n%n", mailReport.subject);
            System.out.printf("Body:\n%s%n", mailReport.body);
        }

    }

    private static Optional<MailSender> createMailSender() {
        String smtpHost = System.getenv("SMTP_HOSTNAME");
        if (smtpHost == null || smtpHost.isEmpty())
            return Optional.empty();
        int smtpPort = Integer.parseInt(System.getenv("SMTP_PORT"));
        String smtpUsername = System.getenv("SMTP_USERNAME");
        String smtpPassword = System.getenv("SMTP_PASSWORD");
        boolean starttls = Optional.ofNullable(System.getenv("SMTP_STARTTLS")).map(Boolean::parseBoolean).orElse(true);

        return Optional.of(new MailSender(smtpHost, smtpPort, starttls, smtpUsername, smtpPassword));
    }

}
