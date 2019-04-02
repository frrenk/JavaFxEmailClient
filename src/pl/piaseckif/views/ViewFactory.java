package pl.piaseckif.views;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import pl.piaseckif.controllers.*;

import javax.naming.OperationNotSupportedException;
import java.io.IOException;

public class ViewFactory {

    public static ViewFactory defaultFactory = new ViewFactory();
    private static boolean mainViewInitialized;

    private ModelAccess modelAccess = new ModelAccess();

    private MainController mainController;

    private EmailDetailsController emailDetailsController;

    private ComposeMessageController composeMessageController;



    public Scene getMainScene() throws OperationNotSupportedException {
        if (!mainViewInitialized) {
            mainController = new MainController(modelAccess);
            Scene scene = initializeScene("/pl/piaseckif/views/MainLayout.fxml", mainController);
            mainViewInitialized = true;
            return scene;
        } else {
            throw new OperationNotSupportedException("Main Scene already initialized.");
        }
    }

    public Scene getEmailScene() {
        emailDetailsController = new EmailDetailsController(modelAccess);
        Scene scene = initializeScene("/pl/piaseckif/views/EmailDetailsLayout.fxml", emailDetailsController);
        return scene;
    }

    public Scene getComposeMessageScene() {
        composeMessageController = new ComposeMessageController(modelAccess);
        Scene scene = initializeScene("/pl/piaseckif/views/ComposeMessageLayout.fxml", composeMessageController);
        return scene;
    }


    public Node resolveIcon(String treeItemValue) {
        String lowercaseTreeItemValue = treeItemValue.toLowerCase();
        ImageView returnIcon = null;

        try {
            if(lowercaseTreeItemValue.contains("inbox")) {
                returnIcon = new ImageView(new Image(getClass().getResourceAsStream("/pl/piaseckif/views/images/inbox.png")));
            }
            else if(lowercaseTreeItemValue.contains("sent")) {
                returnIcon = new ImageView(new Image(getClass().getResourceAsStream("/pl/piaseckif/views/images/sent2.png")));
            }
            else if(lowercaseTreeItemValue.contains("spam")) {
                returnIcon = new ImageView(new Image(getClass().getResourceAsStream("/pl/piaseckif/views/images/spam.png")));
            }
            else if(lowercaseTreeItemValue.contains("@")) {
                returnIcon = new ImageView(new Image(getClass().getResourceAsStream("/pl/piaseckif/views/images/email.png")));
            }
            else {
                returnIcon = new ImageView(new Image(getClass().getResourceAsStream("/pl/piaseckif/views/images/folder.png")));
            }
        } catch (Exception e) {
            System.out.println("Invalid image location!");
            e.printStackTrace();
        }
        returnIcon.setFitHeight(16);
        returnIcon.setFitWidth(16);
        return returnIcon;
    }


    private Scene initializeScene(String fxmlPath, AbstractController controller) {
        FXMLLoader loader;
        Parent parent;
        Scene scene;

        try {
            loader = new FXMLLoader(getClass().getResource(fxmlPath));
            loader.setController(controller);
            parent = loader.load();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        scene = new Scene(parent);
        scene.getStylesheets().add(getClass().getResource("/pl/piaseckif/views/style.css").toExternalForm());
        return scene;
    }
}
