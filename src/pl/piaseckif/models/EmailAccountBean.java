package pl.piaseckif.models;

import javafx.collections.ObservableList;

import javax.mail.*;
import java.io.IOException;
import java.util.Properties;

public class EmailAccountBean {

    private String emailAddress;
    private String password;
    private Properties props;

    private Store store;
    private Session session;

    private int loginState = EmailConstants.LOGIN_STATE_NOT_READY;

    public EmailAccountBean(String emailAddress, String password) {
        this.emailAddress = emailAddress;
        this.password = password;

        props = new Properties();
        props.put("mail.store.protocol", "imaps");
        props.put("mail.transport.protocol", "smtps");
        props.put("mail.smtps.host", "smtp.gmail.com");
        props.put("mail.smtps.auth", "true");
        props.put("incomingHost", "imap.gmail.com");
        props.put("outgoingHost", "smtp.gmail.com");

        Authenticator auth = new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(emailAddress, password);
            }
        };


        session = Session.getInstance(props, auth);

        try {
            this.store = session.getStore();
            store.connect(props.getProperty("incomingHost"), emailAddress, password);
            loginState = EmailConstants.LOGIN_STATE_SUCCEDED;
        } catch (Exception e) {
            e.printStackTrace();
            loginState = EmailConstants.LOGIN_STATE_FAILED_BY_CREDENTIALS;
        }
    }


    public String getEmailAddress() {
        return emailAddress;
    }

    public Properties getProps() {
        return props;
    }

    public Store getStore() {
        return store;
    }

    public Session getSession() {
        return session;
    }

    public int getLoginState() {
        return loginState;
    }

    public String getPassword() {
        return password;
    }
}
