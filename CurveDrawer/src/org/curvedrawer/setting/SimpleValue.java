package org.curvedrawer.setting;

import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;

import java.awt.event.KeyEvent;

public abstract class SimpleValue<T> extends SettingValue<T> {


    private final TextField textField = new TextField();

    protected SimpleValue(String name, SimpleObjectProperty<T> value) {
        super(name, value);

//        textField.setOnAction(this::handleTextChange);
//        textField.setOnKeyPressed(this::handleTextChange);
    }

    protected SimpleValue(String name, T value) {
        this(name, new SimpleObjectProperty<>(value));
    }

    @Override
    public final Node createSettingNode() {
        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER);

        Label label = new Label(getName());

        Region region = new Region();
        HBox.setHgrow(region, Priority.ALWAYS);

        textField.setText(String.valueOf(getValue()));

        hBox.getChildren().addAll(label, region, textField);

        return hBox;
    }

    protected abstract T handleTextChange(KeyEvent keyEvent);

    public final TextField getTextField() {
        return textField;
    }

    @Override
    public String toString() {
        return "SimpleValue{" +
                "textField=" + textField +
                "} " + super.toString();
    }
}
