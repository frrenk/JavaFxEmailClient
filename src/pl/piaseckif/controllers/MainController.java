package pl.piaseckif.controllers;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import pl.piaseckif.controllers.services.CreateAndRegisterEmailAccountService;
import pl.piaseckif.controllers.services.FolderUpdateService;
import pl.piaseckif.controllers.services.MessageRendererService;
import pl.piaseckif.controllers.services.SaveAttachmentService;
import pl.piaseckif.models.EmailAccountBean;
import pl.piaseckif.models.EmailMessageBean;
import pl.piaseckif.models.folders.EmailFolderBean;
import pl.piaseckif.models.table.BoldRowFactory;
import pl.piaseckif.views.ViewFactory;

import javax.mail.MessagingException;
import javax.swing.*;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.Date;
import java.util.ResourceBundle;

public class MainController extends AbstractController implements Initializable{


    private MenuItem showDetails = new MenuItem(("show details"));
    private ViewFactory factory = ViewFactory.defaultFactory;
    private MessageRendererService messageRendererService;

    // JavaFX elements created with SceneBuilder
    @FXML
    private TreeView<String> emailFolders;
    @FXML
    private Button newMessageButton;
    @FXML
    private Button readButton;
    @FXML
    private TableView<EmailMessageBean> emailTableView;
    @FXML
    private WebView messageRenderer;
    @FXML
    private TableColumn<EmailMessageBean, String> subjectColumn;
    @FXML
    private TableColumn<EmailMessageBean, String> senderColumn;
    @FXML
    private TableColumn<EmailMessageBean, String> sizeColumn;
    @FXML
    private TableColumn<EmailMessageBean, Date> dateColumn;
    @FXML
    private Label downloadAttachmentLabel;
    @FXML
    private ProgressBar downloadAttachmentProgress;
    @FXML
    private Button downloadAttachmentButton;

    private SaveAttachmentService saveAttachmentService;



    public MainController(ModelAccess modelAccess) {
        super(modelAccess);
    }


    @FXML
    void newMessageButtonAction(ActionEvent event) {
        Scene scene = ViewFactory.defaultFactory.getComposeMessageScene();
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.show();

    }


    @FXML
    void downloadAttachmentAction(ActionEvent event) {
        EmailMessageBean messageBean = emailTableView.getSelectionModel().getSelectedItem();
        if (messageBean != null && messageBean.hasAttachments()) {
            saveAttachmentService.setMessageBean(messageBean);
            saveAttachmentService.restart();
        }

    }


    @FXML
    void changeReadAction() {

        EmailMessageBean messageBean = getModelAccess().getSelectedMessage();
        if (messageBean !=null) {
            boolean read = messageBean.isRead();
            messageBean.setRead(!read);
            EmailFolderBean<String> selectedFolder = getModelAccess().getSelectedFolder();
            if (selectedFolder!= null) {
                if (read) {
                    selectedFolder.incrementUnreadMessageCount(1);
                }
                else {
                    selectedFolder.decrementUnreadMessageCount();
                }
            }
        }

    }




    @Override
    public void initialize(URL location, ResourceBundle resources) {
        downloadAttachmentLabel.setVisible(false);
        downloadAttachmentProgress.setVisible(false);
        saveAttachmentService = new SaveAttachmentService(downloadAttachmentProgress, downloadAttachmentLabel);
        downloadAttachmentProgress.progressProperty().bind(saveAttachmentService.progressProperty());



        FolderUpdateService folderUpdateService = new FolderUpdateService(getModelAccess().getFolderList());
        folderUpdateService.start();

        emailTableView.setRowFactory(e-> new BoldRowFactory<>());

        //Setting Cell values for every column
        setCellValues();

        // Custom comparator to compare size of the messages - using original size in bytes, as opposed to formatted String
        setSizeComparator();

        setEmailFolders();



        setEvents();
    }

    private void setEvents() {
        emailFolders.setOnMouseClicked(e ->  {
            EmailFolderBean<String> item = (EmailFolderBean<String>)emailFolders.getSelectionModel().getSelectedItem();
            if (item !=null && !item.isTopElement()) {
                emailTableView.setItems(item.getData());
                getModelAccess().setSelectedFolder(item);
                getModelAccess().setSelectedMessage(null);
            }
        });
        emailTableView.setOnMouseClicked(e -> {
            EmailMessageBean messageBean = emailTableView.getSelectionModel().getSelectedItem();
            if (messageBean!=null) {
                getModelAccess().setSelectedMessage(messageBean);
                messageRendererService = new MessageRendererService(messageRenderer.getEngine());
                messageRendererService.setMessageToRender(messageBean);
                messageRendererService.restart();

            }
        });
        showDetails.setOnAction(e -> {
            Stage stage = new Stage();
            Scene scene = factory.getEmailScene();
            stage.setScene(scene);
            stage.show();
        });
    }

    private void setEmailFolders() {
        EmailFolderBean<String> root = new EmailFolderBean<>("");
        emailFolders.setRoot(root);
        emailFolders.setShowRoot(false);

        CreateAndRegisterEmailAccountService createAndRegisterEmailAccountService =
                new CreateAndRegisterEmailAccountService(
                        "email@domain.com",
                        "xxxxxxxx",
                        root,
                        getModelAccess());
        createAndRegisterEmailAccountService.start();

        emailTableView.setContextMenu(new ContextMenu(showDetails));
    }

    private void setSizeComparator() {
        sizeColumn.setComparator(new Comparator<String>() {
            Integer int1;
            Integer int2;
            @Override
            public int compare(String o1, String o2) {
                int1 = EmailMessageBean.intSize.get(o1);
                int2 = EmailMessageBean.intSize.get(o2);
                return int1 - int2;
            }
        });
    }

    private void setCellValues() {
        subjectColumn.setCellValueFactory(new PropertyValueFactory<EmailMessageBean, String>("subject"));
        senderColumn.setCellValueFactory(new PropertyValueFactory<EmailMessageBean, String>("sender"));
        sizeColumn.setCellValueFactory(new PropertyValueFactory<EmailMessageBean, String>("size"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<EmailMessageBean, Date>("timestamp"));
    }



}
