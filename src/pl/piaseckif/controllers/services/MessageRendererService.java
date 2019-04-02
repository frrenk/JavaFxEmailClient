package pl.piaseckif.controllers.services;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.scene.web.WebEngine;
import pl.piaseckif.models.EmailMessageBean;

import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.internet.MimeBodyPart;

public class MessageRendererService extends Service<Void>{

    private EmailMessageBean messageToRender;
    private WebEngine mesageRendererEngine;
    private StringBuffer sb = new StringBuffer();

    public MessageRendererService(WebEngine webEngine) {
        this.mesageRendererEngine = webEngine;
        this.setOnSucceeded(e->showMessage());
    }

    public void setMessageToRender(EmailMessageBean messageToRender) {
        this.messageToRender = messageToRender;
    }

    @Override
    protected Task<Void> createTask() {
        return new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                renderMessage();

                return null;
            }
        };
    }


    private void renderMessage() {
        sb.setLength(0);
        messageToRender.clearAttachment();
        Message message = messageToRender.getMessageReference();
        try {
            String messageType = message.getContentType();
            if (messageType.contains("TEXT/HTML") || messageType.contains("TEXT/PLAIN") || messageType.contains("text")) {
                sb.append(message.getContent().toString());
            }
            else if (messageType.contains("multipart")) {
                Multipart multipart = (Multipart)message.getContent();
                for (int i = multipart.getCount()-1; i >=0; i--) {
                    BodyPart bodyPart = multipart.getBodyPart(i);
                    String contentType = bodyPart.getContentType();
                    if (contentType.contains("TEXT/HTML") || contentType.contains("TEXT/PLAIN") || contentType.contains("mixed") || contentType.contains("text")) {
                        if (sb.length()==0) {
                            sb.append(bodyPart.getContent().toString());
                        }

                    }
                    // attachment handlig
                    else if (contentType.toLowerCase().contains("application") || contentType.toLowerCase().contains("image") || contentType.toLowerCase().contains("audio")  || contentType.toLowerCase().contains("video")) {
                        MimeBodyPart mimeBodyPart = (MimeBodyPart)bodyPart;
                        messageToRender.addAttachment(mimeBodyPart);
                    }
                    else if (bodyPart.getContentType().contains("multipart")) {
                        Multipart multipart1 = (Multipart)bodyPart.getContent();
                        for (int j = multipart1.getCount()-1; j>=0; j--) {
                            BodyPart bodyPart1 = multipart1.getBodyPart(i);
                            if (bodyPart1.getContentType().contains("TEXT/HTML") || bodyPart1.getContentType().contains("TEXT/PLAIN")) {
                                sb.append(bodyPart1.getContent().toString());
                            }
                        }
                    }
                }
            }

        } catch (Exception e) {
            System.out.println("This here");
            e.printStackTrace();
        }

    }

    private void showMessage() {
        mesageRendererEngine.loadContent(sb.toString());
    }


}
