package pl.piaseckif.controllers.services;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import pl.piaseckif.controllers.ModelAccess;
import pl.piaseckif.models.EmailAccountBean;
import pl.piaseckif.models.EmailConstants;
import pl.piaseckif.models.folders.EmailFolderBean;

public class CreateAndRegisterEmailAccountService extends Service<Integer> {


    private String emailAddress;
    private String password;
    private EmailFolderBean<String> folderRoot;
    private ModelAccess modelAccess;


    public CreateAndRegisterEmailAccountService(String emailAddress, String password, EmailFolderBean<String> folderRoot, ModelAccess modelAccess) {
        this.emailAddress = emailAddress;
        this.password = password;
        this.folderRoot = folderRoot;
        this.modelAccess = modelAccess;

    }

    @Override
    protected Task<Integer> createTask() {
        return new Task<Integer>() {
            @Override
            protected Integer call() throws Exception {
                EmailAccountBean emailAccountBean = new EmailAccountBean(emailAddress, password);
                if (emailAccountBean.getLoginState() == EmailConstants.LOGIN_STATE_SUCCEDED) {
                    modelAccess.addAccount(emailAccountBean);
                    EmailFolderBean<String> emailFolderBean = new EmailFolderBean<>(emailAddress);
                    folderRoot.getChildren().add(emailFolderBean);
                    FetchFoldersService fetchFoldersService = new FetchFoldersService(emailFolderBean, emailAccountBean, modelAccess);
                    fetchFoldersService.start();
                }
                return emailAccountBean.getLoginState();
            }
        };

    }
}
