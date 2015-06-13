package de.uni_hannover.sra.minimax_simulator.gui;

import javafx.beans.property.SimpleStringProperty;

/**
 * Created by philipp on 13.06.15.
 */
public class MemoryTableModel {
    private final SimpleStringProperty address;
    private final SimpleStringProperty decimal;
    private final SimpleStringProperty hex;

    public MemoryTableModel(String address, String decimal, String hex) {
        this.address = new SimpleStringProperty(address);
        this.decimal = new SimpleStringProperty(decimal);
        this.hex = new SimpleStringProperty(hex);
    }

    public String getAddress() {
        return address.get();
    }

    public SimpleStringProperty addressProperty() {
        return address;
    }

    public void setAddress(String address) {
        this.address.set(address);
    }

    public String getDecimal() {
        return decimal.get();
    }

    public SimpleStringProperty decimalProperty() {
        return decimal;
    }

    public void setDecimal(String decimal) {
        this.decimal.set(decimal);
    }

    public String getHex() {
        return hex.get();
    }

    public SimpleStringProperty hexProperty() {
        return hex;
    }

    public void setHex(String hex) {
        this.hex.set(hex);
    }
}
