package techdelivery.price.barista;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class MailSender {

    private final String host;
    private final int port;
    private final boolean starttls;
    private final String username;
    private final String password;

    private final Session session;

    public MailSender(String host, int port, boolean starttls, String username, String password) {
        this.host = host;
        this.port = port;
        this.starttls = starttls;
        this.username = username;
        this.password = password;

        Properties prop = new Properties();
        prop.put("mail.smtp.host", host);
        prop.put("mail.smtp.port", String.valueOf(port));
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.starttls.enable", String.valueOf(starttls)); //TLS

        session = Session.getInstance(prop,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });

    }

    public void sendMail(String from, String recipient, String subject, String body) throws MessagingException {
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(from));
        message.setRecipients(
                Message.RecipientType.TO,
                InternetAddress.parse(recipient)
        );
        message.setSubject(subject);
        message.setText(body);

        Transport.send(message);
    }
}
