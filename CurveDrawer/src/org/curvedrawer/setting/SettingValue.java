package org.curvedrawer.setting;

import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;

public abstract class SettingValue<T>  {
    private final String name;
    private final SimpleObjectProperty<T> value;

    public SettingValue(String name, SimpleObjectProperty<T> value) {
        this.name = name;
        this.value = value;
    }

    public SettingValue(String name, T value) {
        this(name, new SimpleObjectProperty<>(value));
    }

    public String getName() {
        return name;
    }

    public T getValue() {
        return value.getValue();
    }

    public SimpleObjectProperty<T> valueProperty() {
        return value;
    }

    public void setValue(T value) {
        this.value.set(value);
    }

    public abstract Node createSettingNode();

    @Override
    public String toString() {
        return "SettingValue{" +
                "name='" + name + '\'' +
                ", value=" + value +
                '}';
    }
}