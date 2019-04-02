package pl.piaseckif.models.folders;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TreeItem;
import pl.piaseckif.models.EmailMessageBean;
import pl.piaseckif.views.ViewFactory;

import javax.mail.Flags;
import javax.mail.Message;
import javax.mail.MessagingException;
import java.time.ZoneId;

public class EmailFolderBean<T> extends TreeItem<String> {

    private boolean topElement;
    private int unreadMessageCount;
    private String name;
    private String completeName;
    private ObservableList<EmailMessageBean> data = FXCollections.observableArrayList();


    /*
        Contructor for top elements
        @param value
     */

    public EmailFolderBean(String value) {
        super(value, ViewFactory.defaultFactory.resolveIcon(value));
        this.name = value;
        this.completeName = value;
        data = null;
        topElement = true;
        this.setExpanded(true);
    }

    public EmailFolderBean(String value, String completeName) {
        super(value, ViewFactory.defaultFactory.resolveIcon(value));
        this.name = value;
        this.completeName = completeName;

    }

    private void updateValue() {
        if (unreadMessageCount >0) {
            this.setValue((String)(name+" ("+unreadMessageCount+")"));
        }
        else {
            this.setValue(name);
        }
    }

    public void incrementUnreadMessageCount(int newMessages) {
        unreadMessageCount += newMessages;
        updateValue();
    }

    public void decrementUnreadMessageCount() {
        unreadMessageCount--;
        updateValue();
    }


    public void addEmail(int position, Message message) {
        try {
            boolean isRead = message.getFlags().contains(Flags.Flag.SEEN);
            EmailMessageBean emailMessageBean = new EmailMessageBean(message.getSubject()
                    , message.getFrom()[0].toString()
                    , message.getSize()
                    , isRead
                    , message.getReceivedDate()
                    , message);
            if (position < 0) {
                data.add(emailMessageBean);
            } else {
                data.add(position, emailMessageBean);
            }
            if (!isRead) {
                incrementUnreadMessageCount(1);
            }
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    public boolean isTopElement() {
        return topElement;
    }

    public ObservableList<EmailMessageBean> getData() {
        return data;
    }

    public int getUnreadMessageCount() {
        return unreadMessageCount;
    }

    public String getName() {
        return name;
    }

    public String getCompleteName() {
        return completeName;
    }
}
