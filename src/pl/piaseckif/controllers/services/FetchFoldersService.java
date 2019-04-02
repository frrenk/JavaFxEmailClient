package pl.piaseckif.controllers.services;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import pl.piaseckif.controllers.ModelAccess;
import pl.piaseckif.models.EmailAccountBean;
import pl.piaseckif.models.folders.EmailFolderBean;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.event.MessageCountAdapter;
import javax.mail.event.MessageCountEvent;

public class FetchFoldersService extends Service<Void> {

    private EmailFolderBean<String> foldersRoot;
    private EmailAccountBean emailAccountBean;
    private ModelAccess modelAccess;

    private static int numberOfFetchServicesActive = 0;

    public FetchFoldersService(EmailFolderBean<String> foldersRoot, EmailAccountBean emailAccountBean, ModelAccess modelAccess) {
        this.foldersRoot = foldersRoot;
        this.emailAccountBean = emailAccountBean;
        this.modelAccess = modelAccess;

        this.setOnSucceeded(e -> numberOfFetchServicesActive--);

    }

    @Override
    protected Task<Void> createTask() {
        return new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                numberOfFetchServicesActive++;
                if(emailAccountBean!=null) {
                    Folder[] folders = emailAccountBean.getStore().getDefaultFolder().list();
                    for (Folder folder :
                            folders) {
                        modelAccess.addFolder(folder);
                        EmailFolderBean<String> item = new EmailFolderBean<>(folder.getName(), folder.getFullName());
                        foldersRoot.getChildren().add(item);
                        item.setExpanded(true);
                        addMessageListenerToFolder(folder, item);
                        FetchFolderMessagesService fetchFolderMessagesService = new FetchFolderMessagesService(item, folder);
                        fetchFolderMessagesService.start();
                        System.out.println("added Folder "+ folder.getName());

                        Folder[] subFolders = folder.list();

                        for (Folder subFolder :
                                subFolders) {
                            modelAccess.addFolder(subFolder);
                            EmailFolderBean<String> subItem = new EmailFolderBean<>(subFolder.getName(), subFolder.getFullName());
                            item.getChildren().add(subItem);
                            addMessageListenerToFolder(subFolder, subItem);
                            FetchFolderMessagesService fetchSubFolderMessagesService = new FetchFolderMessagesService(subItem, subFolder);
                            fetchSubFolderMessagesService.start();
                            System.out.println("added subFolder "+subFolder.getName());
                        }
                    }
                }
                return null;
            }
        };
    }


    private void addMessageListenerToFolder(Folder folder, EmailFolderBean<String> item) {
        folder.addMessageCountListener(new MessageCountAdapter() {
            @Override
            public void messagesAdded(MessageCountEvent e) {
                for(int i = 0; i<e.getMessages().length; i++) {
                    try {
                        Message currentMessage = folder.getMessage(folder.getMessageCount());
                        item.addEmail(0, currentMessage);
                    } catch (MessagingException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        });

    }

    public static boolean noServicesActive() {
        return numberOfFetchServicesActive==0;
    }
}
