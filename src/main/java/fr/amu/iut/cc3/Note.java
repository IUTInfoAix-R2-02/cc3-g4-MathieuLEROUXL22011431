package fr.amu.iut.cc3;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class Note {

    public Note(int val) {
        this.value = new SimpleIntegerProperty(val);
    }

    public int getValue() {
        return value.get();
    }

    public IntegerProperty valueProperty() {
        return value;
    }

    public void setValue(int value) {
        this.value.set(value);
    }

    private IntegerProperty value;

}
