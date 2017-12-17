package org.curvedrawer;

import edu.wpi.first.wpilibj.networktables.NetworkTable;
import javafx.application.Application;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TabPane.TabClosingPolicy;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    public static final int NUMBER_OF_STEPS = 50;
    public static final String IP_ADDRESS = "10.0.0.24";
    public static final boolean IS_CLIENT = false;
    public static final String NETWORK_TABLE_TABLE_KEY = "SmartDashboard";
    public static final SimpleDoubleProperty SCALE_FACTOR = new SimpleDoubleProperty(100); // 10 px == 1 m
    public static final SimpleDoubleProperty ZOOM_FACTOR = new SimpleDoubleProperty(.1);
    private static final int TEAM_NUMBER = 2974;
    public static NetworkTable networkTable;
    private TabPane tabPane;

    public static void main(String[] args) {
        launch(args);
    }

    private static void initNetworkTable() {
        if (IS_CLIENT) {
            NetworkTable.setClientMode();
            NetworkTable.setTeam(TEAM_NUMBER);
        } else {
            NetworkTable.setServerMode();
            NetworkTable.setIPAddress(IP_ADDRESS);
        }

        networkTable = NetworkTable.getTable(NETWORK_TABLE_TABLE_KEY);
    }

    @Override
    public final void start(Stage primaryStage) throws IOException {
        initNetworkTable();

        tabPane = new TabPane();
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
