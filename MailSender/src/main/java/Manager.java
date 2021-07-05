import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.*;
import java.util.Properties;

public class Manager {
    private Message message = null;
    protected static String SMTP_SERVER = null;
    protected static String SMTP_PORT = null;
    protected static String SMTP_AUTH_USER = null;
    protected static String SMTP_AUTH_PWD = null;
    protected static String EMAIL_FROM = null;
    protected static String FILE_PATH = null;
    protected static String REPLY_TO = null;

    public Manager(final String mailTo, final String theme) {
        Properties properties = new Properties();
        properties.put("mail.smtp.host", SMTP_SERVER);
        properties.put("mail.smtp.port", SMTP_PORT);
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.ssl.enable", "true");
        properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");

        try {
            Authenticator auth = new Authenticator(SMTP_AUTH_USER, SMTP_AUTH_PWD);
            Session session = Session.getDefaultInstance(properties, auth);
            session.setDebug(false);

            InternetAddress emailFrom = new InternetAddress(EMAIL_FROM);
            InternetAddress emailTo = new InternetAddress(mailTo);
            InternetAddress replyTo = (REPLY_TO != null) ? new InternetAddress(REPLY_TO) : null;

            message = new MimeMessage(session);
            message.setFrom(emailFrom);
            message.setRecipient(Message.RecipientType.TO, emailTo);
            message.setSubject(theme);
            if (replyTo != null) {
                message.setReplyTo(new Address[] {replyTo});
            }

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    private MimeBodyPart createFileAttachment(String filepath) throws MessagingException {
        MimeBodyPart mbp = new MimeBodyPart();
        FileDataSource dataSource = new FileDataSource(filepath);
        mbp.setDataHandler(new DataHandler(dataSource));
        mbp.setFileName(dataSource.getName());
        return mbp;
    }

    public boolean sendMessage (final String text) {
        boolean messageSent = false;

        try {
            Multipart mmp = new MimeMultipart();
            MimeBodyPart bodyPart = new MimeBodyPart();
            bodyPart.setContent(text, "text/plain; charset=utf-8");
            mmp.addBodyPart(bodyPart);
            if (FILE_PATH != null) {
                MimeBodyPart mbr = createFileAttachment(FILE_PATH);
                mmp.addBodyPart(mbr);
            }
            message.setContent(mmp);

            Transport.send(message);
            messageSent = true;
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        return messageSent;
    }
}
