package org.curvedrawer.setting;

import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Node;

public abstract class SettingValue<T> {
    private final String name;
    private final SimpleObjectProperty<T> value;

    protected SettingValue(String name, SimpleObjectProperty<T> value) {
        this.name = name;
        this.value = value;
    }

    protected SettingValue(String name, T value) {
        this(name, new SimpleObjectProperty<>(value));
    }

    public final String getName() {
        return name;
    }

    public final T getValue() {
        return value.getValue();
    }

    public final void setValue(T value) {
        this.value.set(value);
    }

    public final SimpleObjectProperty<T> valueProperty() {
        return value;
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