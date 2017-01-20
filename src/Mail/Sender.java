package Mail;

import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;

/**
 *
 */
class Sender {

    private static String HOST = "smtp.helsinki.fi";
    private static String PORT = "587";
    private static String DOMAIN = "@helsinki.fi";

    private static Properties properties = new Properties();
    private static Session session;
    private static String mailAddress = "";
    private static String domain;

    /**
     * This method will use default settings for connection.
     *
     * @param userName Username of Helsinki university account
     * @param mailAddress Mail address (first part before @)
     * @param password Account password
     */
    public void initializeConnection(final String userName, final String mailAddress, final String password) {
        initializeConnection(HOST, PORT, userName, mailAddress, DOMAIN, password);
    }



    /**
     * This method is used when more specified connection is required.
     *
     * @param host Connection host
     * @param port Port to use
     * @param userName Username
     * @param mailAddress Mail address (first part before @)
     * @param domain Domain of the mail address (@example.com)
     * @param password Account password
     */
    public void initializeConnection(String host, String port, final String userName,
                                  final String mailAddress, final String domain, final String password) {

        this.domain = domain;
        this.mailAddress = mailAddress;

        //SMTP server properties
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", port);
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.user", userName);
        properties.put("mail.password", password);
        //properties.put("mail.debug", "true");

        // creates a new session with an authenticator
        Authenticator auth = new Authenticator() {
            public PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(userName, password);
            }
        };
        this.session = Session.getInstance(properties, auth);

    }

    /**
     * Method for sending the message to target destination.
     *
     * @param toAddress Address where the message should be sent.
     * @param subject Subject of the message.
     * @param message Message of the message, duh.
     * @return 0 = success, -1 = null session, -2 = AddressException, -3 = MessagingException.
     */
    public int sendEmail(String toAddress, String subject, String message) {

        if(session == null) return -1;

        // creates a new e-mail message
        Message msg = new MimeMessage(session);
        try {
            try {
                msg.setFrom(new InternetAddress(this.mailAddress + DOMAIN));
            } catch(javax.mail.internet.AddressException e) {
                System.out.println(e);
                return -2;
            }

            InternetAddress[] toAddresses = {new InternetAddress(toAddress)};
            msg.setRecipients(Message.RecipientType.TO, toAddresses);
            msg.setSubject(subject);
            msg.setSentDate(new Date());

            // creates message part
            MimeBodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setContent(message, "text/plain; charset=UTF-8");

            // creates multi-part
            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(messageBodyPart);

            // sets the multi-part as e-mail's content
            msg.setContent(multipart);

            // sends the e-mail
            Transport.send(msg);
        } catch(javax.mail.MessagingException e) {
            System.out.println(e);
            return -3;
        }
        return 0;
    }
}

