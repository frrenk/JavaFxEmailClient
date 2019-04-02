package pl.piaseckif.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.web.WebView;
import pl.piaseckif.models.EmailMessageBean;
import pl.piaseckif.views.ViewFactory;

import javax.mail.MessagingException;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class EmailDetailsController extends AbstractController implements Initializable {


    @FXML
    private WebView webView;

    @FXML
    private Label subjectLabel;

    @FXML
    private Label senderLabel;

    private ViewFactory factory = ViewFactory.defaultFactory;

    public EmailDetailsController(ModelAccess modelAccess) {
        super(modelAccess);
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        EmailMessageBean message = getModelAccess().getSelectedMessage();

        subjectLabel.setText("Subject: "+message.getSubject());

        senderLabel.setText("Sender: "+message.getSender());

        try {
            webView.getEngine().loadContent(message.getMessageReference().getContent().toString());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        }

    }
}
