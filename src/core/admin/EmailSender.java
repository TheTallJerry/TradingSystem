package core.admin;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

/**
 * Sends email to a user, represented with a valid email address.
 * The email address used as a sender, is a gmail address with username and password specified below.
 */
class EmailSender {
    private final String toEmailAddress, messageContent;

    /**
     * Constructs an EmailSender by the <code>toEmailAddress</code> and <code>messageContent</code>.
     *
     * @param toEmailAddress the email address to send to.
     * @param messageContent the content send to <code>toEmailAddress</code>.
     */
    EmailSender(String toEmailAddress, String messageContent) {
        this.toEmailAddress = toEmailAddress;
        this.messageContent = messageContent;
    }

    /**
     * Sends email to <code>toEmailAddress</code> with <code>messageContent</code>.
     */
    void sendEmail() {
        //Google's Simple Mail Transfer Protocol
        //https://www.siteground.com/kb/google_free_smtp_server/#:~:text=Google's%20Gmail%20SMTP%20server%20is,%2C%20newsletter%20blasts%2C%20or%20notifications.
        String host = "smtp.gmail.com";

        Properties props = System.getProperties();

        //https://www.oracle.com/technetwork/java/sslnotes-150073.txt#:~:text=enable%20or%20mail.-,smtp.,before%20sending%20any%20login%20information.
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", host);

        String senderUsername = "CSC207Project2020";
        props.put("mail.smtp.user", senderUsername);

        String senderPassword = "MakeWorldABetterPlace";
        props.put("mail.smtp.password", senderPassword);

        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");

        props.setProperty("mail.smtp.host", host);

        Session session = Session.getDefaultInstance(props);

        MimeMessage message = new MimeMessage(session);

        try {
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(toEmailAddress));
            message.setFrom(new InternetAddress(senderUsername));

            message.setSubject("Your admin creation request status");
            message.setText(messageContent);
            Transport transport = session.getTransport("smtp");
            transport.connect(host, senderUsername, senderPassword);
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}

