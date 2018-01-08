package org.curvedrawer.controller;

import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.curvedrawer.setting.SettingValue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;


public final class SettingController {
    private static final Collection<SettingValue> SETTING_VALUES = new ArrayList<>(15);

    private static final Parent PARENT;
    private static final Stage STAGE = new Stage();
    private static final VBox V_BOX;

    static {
        VBox V_BOX1;
        Parent parent1;
        try {
            parent1 = FXMLLoader
                    .load(PathSelectorController.class.getResource("/assets/fxml/settings.fxml"));

            V_BOX1 = ((VBox) ((ScrollPane) parent1.getChildrenUnmodifiable().get(0)).getContent());
        } catch (IOException e) {
            e.printStackTrace();
            parent1 = null;
            V_BOX1 = null;
        }

        V_BOX = V_BOX1;
        PARENT = parent1;

        if (PARENT != null) {
            STAGE.setScene(new Scene(PARENT));
        }
        STAGE.initModality(Modality.APPLICATION_MODAL);
    }


    public static void showSettings() {
        STAGE.showAndWait();

        for (SettingValue settingValue : SETTING_VALUES) {
            System.out.println(settingValue);
        }
    }

//    public static void addNumber(String key, Number value) {
//        addSpecialSetting(new DoubleValue(key, (Double) value));
//    }

    public static <T> void addSpecialSetting(SettingValue<T> settingValue) {
        SETTING_VALUES.add(settingValue);

        addSettingNode(settingValue.createSettingNode());
    }

    public static <T> SimpleObjectProperty<T> getValue(String key) {
        for (SettingValue settingValue : SETTING_VALUES) {
            if (settingValue.getName().equals(key)) {
                return (SimpleObjectProperty<T>) settingValue.valueProperty();
            }
        }

        return null;
    }

    private static void addSettingNode(Node node) {
        VBox.setVgrow(node, Priority.NEVER);
        V_BOX.setAlignment(Pos.CENTER);
        V_BOX.getChildren().add(node);
    }
}
