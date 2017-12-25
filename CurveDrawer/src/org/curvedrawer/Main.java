package org.curvedrawer;

import edu.wpi.first.wpilibj.networktables.NetworkTable;
import javafx.application.Application;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ObservableDoubleValue;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TabPane.TabClosingPolicy;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import org.curvedrawer.controller.SettingController;
import org.curvedrawer.setting.SimpleValue;

import java.awt.event.KeyEvent;
import java.io.IOException;

public class Main extends Application {

    public static final String IP_ADDRESS = "10.0.0.24";
    public static final String NETWORK_TABLE_TABLE_KEY = "SmartDashboard";
    private static final boolean IS_CLIENT = false;
        public static final int NUMBER_OF_STEPS = 50;
        private static final int TEAM_NUMBER = 2974;
        public static final ObservableDoubleValue ZOOM_FACTOR = new SimpleDoubleProperty(0.1);
        public static final ObservableDoubleValue SCALE_FACTOR = new SimpleDoubleProperty(100.0); // 10 px == 1 m

    public static NetworkTable networkTable;

    static {
//        SettingController.addNumber("NUMBER_OF_STEPS", 50, NumberType.INTEGER);
//        SettingController.addString("IP_ADDRESS", 100.0);
//        SettingController.addString("NETWORK_TABLE_KEY", 100.0);
//        SettingController.addNumber("SCALE_FACTOR", 100.0, NumberType.DOUBLE);
//        SettingController.addNumber("ZOOM_FACTOR", 0.1, NumberType.DOUBLE);
//        SettingController.addBoolean("IS_CLIENT", IS_CLIENT);


        SettingController.addSpecialSetting(new SimpleValue<Integer>("Hello", 3) {
            @Override
            protected Integer handleTextChange(KeyEvent keyEvent) {
                return null;
            }

        });

    }

    private TabPane tabPane = new TabPane();

    public static void main(String[] args) {
        launch(args);
    }

    private static void initNetworkTable() {
//        SettingController.addNumber("TEAM_NUMBER", 2974, NumberType.INTEGER);
//        SettingController.<Integer>getValue("TEAM_NUMBER").addListener((observable, oldValue, newValue) -> {
//            if (!newValue.equals(oldValue))
//            {
//                NetworkTable.setTeam(newValue);
//            }
//        });

        if (IS_CLIENT) {
            NetworkTable.setClientMode();
            NetworkTable.setTeam(SettingController.<Integer>getValue("TEAM_NUMBER").get());
        } else {
            NetworkTable.setServerMode();
            NetworkTable.setIPAddress(IP_ADDRESS);
        }

        networkTable = NetworkTable.getTable(NETWORK_TABLE_TABLE_KEY);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        initNetworkTable();

        tabPane.setMaxSize(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY);
        tabPane.setMinSize(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY);
        tabPane.setPrefSize(640.0, 480.0);
        tabPane.setTabClosingPolicy(TabClosingPolicy.UNAVAILABLE);

        tabPane.setOnKeyPressed(event ->
        {
            if (event.isControlDown() && (event.getCode() == KeyCode.S)) {
                System.out.println("SAVED"); //TODO make it save the current progress
            }
        });

        ContextMenu contextMenu = new ContextMenu();

        MenuItem setting = new MenuItem("Settings");
        setting.setOnAction(e -> SettingController.showSettings());

        contextMenu.getItems().add(setting);

        tabPane.setContextMenu(contextMenu);


        addTab(createDrawingTab("Curve Drawer"));

        primaryStage.setTitle("Bezier Curve Creator");
        primaryStage.setScene(new Scene(tabPane));
        primaryStage.getScene().getStylesheets().add("/assets/css/stylesheet.css");

        primaryStage.show();

        primaryStage.setOnCloseRequest(event -> NetworkTable.shutdown());
    }

    private void addTab(Tab tab) {
        tabPane.getTabs().add(tab);

        if (tabPane.getTabs().size() > 1) {
            tabPane.setTabClosingPolicy(TabClosingPolicy.ALL_TABS);
        }
    }

    private Tab createDrawingTab(String name) throws IOException {
        Parent tabContentController = FXMLLoader.load(getClass().getResource("/assets/fxml/curve_drawing_tab.fxml")); //TODO improve upon this

        Tab tab = new Tab(name);
        tab.setContent(tabContentController);
        tab.setOnClosed(event -> {
            if (tabPane.getTabs().size() <= 1) {
                tabPane.setTabClosingPolicy(TabClosingPolicy.UNAVAILABLE);
            }
        });


        return tab;
    }

}
