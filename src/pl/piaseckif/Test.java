package pl.piaseckif;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import pl.piaseckif.models.EmailAccountBean;
import pl.piaseckif.models.EmailMessageBean;

public class Test {


    public static void main (String[] args) {
        final EmailAccountBean emailAccountBean = new EmailAccountBean("frpicipec@gmail.com", "frpici123");

        ObservableList<EmailMessageBean> data = FXCollections.observableArrayList();



    }


}
