import javax.mail.PasswordAuthentication;

public class Authenticator extends javax.mail.Authenticator {
    private String login;
    private String password;

    public Authenticator(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(login, password);
    }
}
