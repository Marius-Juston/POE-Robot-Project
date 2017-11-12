import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();


      root.setOnKeyPressed(event ->
      {
        String character = event.getText();

        if (event.isControlDown() && character.equals("s")) {
          System.out.println("SAVED");
        }
      });



      ((TabPane)root).getTabs().add(new BezierTab("test Tab"));
    }


    public static void main(String[] args) {
        launch(args);
    }
}
