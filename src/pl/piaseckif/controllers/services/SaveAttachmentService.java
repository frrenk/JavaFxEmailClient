package pl.piaseckif.controllers.services;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import pl.piaseckif.models.EmailMessageBean;

import javax.mail.MessagingException;
import javax.mail.internet.MimeBodyPart;
import java.io.IOException;

public class SaveAttachmentService extends Service<Void> {

    private static final String DOWNLOAD_DEFAULT_LOCATION = System.getProperty("user.home") +"/Downloads/";

    private EmailMessageBean messageBean;
    private ProgressBar progressBar;
    private Label label;

    public SaveAttachmentService(ProgressBar progressBar, Label label) {
        this.progressBar = progressBar;
        this.label = label;
        
        this.setOnRunning(e-> {
            showVisuals(true);
        });
        this.setOnSucceeded(e-> {
            showVisuals(false);
        });
    }

    public EmailMessageBean getMessageBean() {
        return messageBean;
    }

    public void setMessageBean(EmailMessageBean messageBean) {
        this.messageBean = messageBean;
    }

    public ProgressBar getProgressBar() {
        return progressBar;
    }

    public void setProgressBar(ProgressBar progressBar) {
        this.progressBar = progressBar;
    }

    public Label getLabel() {
        return label;
    }

    public void setLabel(Label label) {
        this.label = label;
    }

    @Override
    protected Task<Void> createTask() {
        return new Task<Void>() {
            @Override
            protected Void call() throws Exception {

                try {
                    for (MimeBodyPart mimeBodyPart :
                            messageBean.getAttachmentList()) {
                        updateProgress(messageBean.getAttachmentList().indexOf(mimeBodyPart), messageBean.getAttachmentList().size());
                        mimeBodyPart.saveFile(DOWNLOAD_DEFAULT_LOCATION+mimeBodyPart.getFileName());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (MessagingException e) {
                    e.printStackTrace();
                }
                return null;
            }
        };
    }
    
    private void showVisuals(boolean show) {
        progressBar.setVisible(show);
        label.setVisible(show);
    }
}
