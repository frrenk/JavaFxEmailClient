package pl.piaseckif.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.web.HTMLEditor;
import javafx.stage.FileChooser;
import pl.piaseckif.controllers.services.SendEmailService;
import pl.piaseckif.models.EmailConstants;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ComposeMessageController extends AbstractController implements Initializable {

    private List<File> attachments = new ArrayList<>();

    @FXML
    private Label fromLabel;
    @FXML
    private Label toLabel;
    @FXML
    private Label subjectLabel;
    @FXML
    private Button attachButton;
    @FXML
    private Label attachLabel;
    @FXML
    private ChoiceBox<String> fromChoiceBox;
    @FXML
    private TextField toTextField;
    @FXML
    private TextField subjectTextField;
    @FXML
    private HTMLEditor messageHtmlEditor;
    @FXML
    private Button sendButton;
    @FXML
    private Label errorLabel;


    public ComposeMessageController(ModelAccess modelAccess) {
        super(modelAccess);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        fromChoiceBox.setItems(getModelAccess().getEmailAccountsNames());
        fromChoiceBox.setValue(getModelAccess().getEmailAccountsNames().get(0));

    }

    @FXML
    void attachButtonAction(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            attachments.add(selectedFile);
            attachLabel.setText(attachLabel.getText() + selectedFile.getName() + "; ");
        }

    }

    @FXML
    void sendButtonAction(ActionEvent event) {
        errorLabel.setText("");
        SendEmailService sendEmailService = new SendEmailService(getModelAccess().getEmailAccountByName(fromChoiceBox.getValue()), subjectTextField.getText(), toTextField.getText(), messageHtmlEditor.getHtmlText(), attachments);
        sendEmailService.restart();
        sendEmailService.setOnSucceeded(e-> {
            if (sendEmailService.getValue() != EmailConstants.MESSAGE_SENT_SUCCESFULLY) {
                errorLabel.setText("Message sent");
            } else {
                errorLabel.setText("An error occured. Message not sent.");
            }
        });

    }
}
