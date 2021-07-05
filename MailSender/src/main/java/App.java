import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

//Enter data in email.properties
public class App {
    private final static String PROPS_FILE = "src/main/resources/email.properties";

    public static void main(String[] args) {
        try (InputStream is = new FileInputStream(PROPS_FILE)){
            Reader reader = new InputStreamReader(is, StandardCharsets.UTF_8);
            Properties pr = new Properties();
            pr.load(reader);

            Manager.SMTP_SERVER = pr.getProperty("server");
            Manager.SMTP_PORT = pr.getProperty("port");
            Manager.EMAIL_FROM = pr.getProperty("from");
            Manager.SMTP_AUTH_USER = pr.getProperty("user");
            Manager.SMTP_AUTH_PWD = pr.getProperty("pass");
            Manager.REPLY_TO = pr.getProperty("replyto");
            Manager.FILE_PATH = null;

            String emailTo = pr.getProperty("to");
            String theme = "Mail sender test";
            String text = "Test message";

            Manager se = new Manager(emailTo, theme);
            se.sendMessage(text);

            System.out.println("Message successfully sent");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
