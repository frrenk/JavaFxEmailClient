package pl.piaseckif.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import pl.piaseckif.models.EmailAccountBean;
import pl.piaseckif.models.EmailMessageBean;
import pl.piaseckif.models.folders.EmailFolderBean;

import javax.mail.Folder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ModelAccess {

    private EmailMessageBean selectedMessage;
    private EmailFolderBean<String> selectedFolder;
    private List<Folder> folderList = new ArrayList<>();
    private Map<String, EmailAccountBean> emailAccounts = new HashMap<>();
    private ObservableList<String> emailAccountsNames = FXCollections.observableArrayList();

    public List<Folder> getFolderList() {
        return folderList;
    }

    public void addFolder(Folder folder) {
        folderList.add(folder);
    }

    public void setFolderList(List<Folder> folderList) {
        this.folderList = folderList;
    }

    public EmailMessageBean getSelectedMessage() {
        return selectedMessage;
    }

    public void setSelectedMessage(EmailMessageBean selectedMessage) {
        this.selectedMessage = selectedMessage;
    }

    public EmailFolderBean<String> getSelectedFolder() {
        return selectedFolder;
    }

    public void setSelectedFolder(EmailFolderBean<String> selectedFolder) {
        this.selectedFolder = selectedFolder;
    }

    public Map<String, EmailAccountBean> getEmailAccounts() {
        return emailAccounts;
    }

    public void setEmailAccounts(Map<String, EmailAccountBean> emailAccounts) {
        this.emailAccounts = emailAccounts;
    }

    public ObservableList<String> getEmailAccountsNames() {
        return emailAccountsNames;
    }

    public void setEmailAccountsNames(ObservableList<String> emailAccountsNames) {
        this.emailAccountsNames = emailAccountsNames;
    }

    public EmailAccountBean getEmailAccountByName(String name) {
        return emailAccounts.get(name);
    }

    public void addAccount(EmailAccountBean emailAccountBean) {
        emailAccounts.put(emailAccountBean.getEmailAddress(), emailAccountBean);
        emailAccountsNames.add(emailAccountBean.getEmailAddress());
    }
}
