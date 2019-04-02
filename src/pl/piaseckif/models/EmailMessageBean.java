package pl.piaseckif.models;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import pl.piaseckif.models.table.AbstractTableItem;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.MimeBodyPart;
import java.time.LocalDateTime;
import java.util.*;

public class EmailMessageBean extends AbstractTableItem {

    public static Map<String, Integer> intSize = new HashMap<>();

    private SimpleStringProperty sender;
    private SimpleStringProperty subject;
    private SimpleStringProperty size;
    private SimpleObjectProperty<Date> timestamp;
    private Message messageReference;

    private List<MimeBodyPart> attachmentList = new ArrayList<>();
    private StringBuffer attachmentNames = new StringBuffer();

    public EmailMessageBean(String subject, String sender, int size, boolean isRead, Date timestamp, Message messageReference) {
        super(isRead);
        this.sender = new SimpleStringProperty(sender);
        this.subject = new SimpleStringProperty(subject);
        this.size = new SimpleStringProperty(formatSize(size));
        this.timestamp = new SimpleObjectProperty<>();
        this.messageReference = messageReference;
    }

    public List<MimeBodyPart> getAttachmentList() {
        return attachmentList;
    }

    public void addAttachment(MimeBodyPart mimeBodyPart) {
        attachmentList.add(mimeBodyPart);
        try {
            attachmentNames.append(mimeBodyPart.getFileName()+"; ");
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    public boolean hasAttachments() {
        return attachmentList.size()>0;
    }

    public void clearAttachment() {
        attachmentList.clear();
        attachmentNames.setLength(0);
    }


    public Date getTimestamp() {
        return timestamp.get();
    }

    public SimpleObjectProperty<Date> timestampProperty() {
        return timestamp;
    }

    public String getAttachmentNames() {
        return attachmentNames.toString();
    }

    public String getSender() {
        return sender.get();
    }

    public SimpleStringProperty senderProperty() {
        return sender;
    }

    public String getSubject() {
        return subject.get();
    }

    public SimpleStringProperty subjectProperty() {
        return subject;
    }

    public String getSize() {
        return size.get();
    }

    public SimpleStringProperty sizeProperty() {
        return size;
    }

    public Message getMessageReference() {
        return messageReference;
    }

    public void setMessageReference(Message messageReference) {
        this.messageReference = messageReference;
    }

    private String formatSize(int size) {
        String returnValue;
        if (size<=0) {
            returnValue="0";
        }
        else if (size<1024) {
            returnValue=size+" B";
        }
        else if (size < 1048576) {
            returnValue=size/1024+" kB";
        }
        else {
            returnValue= size/1048576 +" MB";
        }
        intSize.put(returnValue, size);
        return returnValue;
    }

    @Override
    public String toString() {
        return "EmailMessageBean{" +
                "sender=" + sender +
                ", subject=" + subject +
                ", size=" + size +
                '}';
    }
}
