package pl.piaseckif.models.table;

import javafx.beans.property.SimpleBooleanProperty;

public abstract class AbstractTableItem {

    private final SimpleBooleanProperty read = new SimpleBooleanProperty();

    public AbstractTableItem(boolean isRead) {
        this.setRead(isRead);
    }

    public boolean isRead() {
        return read.get();
    }

    public SimpleBooleanProperty readProperty() {
        return read;
    }

    public void setRead(boolean read) {
        this.read.set(read);
    }
}
