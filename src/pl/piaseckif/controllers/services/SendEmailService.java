package pl.piaseckif.controllers.services;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import pl.piaseckif.models.EmailAccountBean;
import pl.piaseckif.models.EmailConstants;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class SendEmailService extends Service<Integer> {

    private int result;
    private EmailAccountBean emailAccountBean;

    private String subject;
    private String recipient;
    private String content;
    private List<File> attachments = new ArrayList<>();

    public SendEmailService(EmailAccountBean emailAccountBean, String subject, String recipient, String content, List<File> attachments) {
        this.emailAccountBean = emailAccountBean;
        this.subject = subject;
        this.recipient = recipient;
        this.content = content;
        this.attachments = attachments;
    }

    @Override
    protected Task<Integer> createTask() {
        return new Task<Integer>() {
            @Override
            protected Integer call() throws Exception {
                try {
                    Session session = emailAccountBean.getSession();
                    MimeMessage mimeMessage = new MimeMessage(session);
                    mimeMessage.setFrom(emailAccountBean.getEmailAddress());
                    mimeMessage.addRecipients(Message.RecipientType.TO, recipient);
                    mimeMessage.setSubject(subject);

                    Multipart multipart = new MimeMultipart();
                    BodyPart messageBodyPart = new MimeBodyPart();
                    messageBodyPart.setContent(content, "text/html");
                    multipart.addBodyPart(messageBodyPart);

                    if (attachments.size() > 0) {
                        for (File file :
                                attachments) {
                            MimeBodyPart attachmentBodyPart = new MimeBodyPart();
                            DataSource source = new FileDataSource(file.getAbsolutePath());
                            attachmentBodyPart.setDataHandler(new DataHandler(source));
                            attachmentBodyPart.setFileName(file.getName());
                            multipart.addBodyPart(attachmentBodyPart);
                        }
                    }

                    mimeMessage.setContent(multipart);

                    Transport transport = session.getTransport();
                    transport.connect(emailAccountBean.getProps().getProperty("outgoingHost"),
                            emailAccountBean.getEmailAddress(),
                            emailAccountBean.getPassword());
                    transport.sendMessage(mimeMessage, mimeMessage.getAllRecipients());
                    transport.close();

                    result = EmailConstants.MESSAGE_SENT_SUCCESFULLY;


                } catch (Exception e) {
                    e.printStackTrace();
                    result = EmailConstants.MESSAGE_SENT_UNSUCCESFULLY;
                }

                return result;
            }
        };
    }
}
