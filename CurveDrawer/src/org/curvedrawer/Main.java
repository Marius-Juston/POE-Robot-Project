package org.curvedrawer;

import edu.wpi.first.wpilibj.networktables.NetworkTable;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    public static final int NUMBER_OF_STEPS = 50;
    public static final int TEAM_NUMBER = 2974;
    public static final String NETWORK_TABLE_TABLE_KEY = "SmartDashboard";
    public static NetworkTable networkTable;
    private TabPane tabPane;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        initNetworkTable();

        tabPane = new TabPane();
        tabPane.setMaxSize(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY);
        tabPane.setMinSize(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY);
        tabPane.setPrefSize(640.0, 480.0);
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        tabPane.setOnKeyPressed(event ->
        {
            if (event.isControlDown() && event.getCode() == KeyCode.S) {
                System.out.println("SAVED"); //TODO make it save the current progress
            }
        });

        addTab(createDrawingTab("Curve Drawer"));

        primaryStage.setTitle("Bezier Curve Creator");
        primaryStage.setScene(new Scene(tabPane));
        primaryStage.show();


        primaryStage.setOnCloseRequest(event -> NetworkTable.shutdown());
    }

    private void initNetworkTable() {
        NetworkTable.setClientMode();
        NetworkTable.setTeam(TEAM_NUMBER);
        networkTable = NetworkTable.getTable(NETWORK_TABLE_TABLE_KEY);
    }

    private void addTab(Tab tab) {
        tabPane.getTabs().add(tab);

        if (tabPane.getTabs().size() > 1) {
            tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.ALL_TABS);
        }
    }

    private Tab createDrawingTab(String name) throws IOException {
        Parent tabContentController = FXMLLoader.load(getClass().getResource("/assets/fxml/curve_drawing_tab.fxml")); //TODO improve upon this

        Tab tab = new Tab(name);
        tab.setContent(tabContentController);
        tab.setOnClosed(event -> {
            if (tabPane.getTabs().size() <= 1) {
                tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
            }
        });


        return tab;
    }

}
