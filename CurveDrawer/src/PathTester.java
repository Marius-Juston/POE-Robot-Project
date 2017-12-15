import edu.wpi.first.wpilibj.networktables.NetworkTable;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.ReadOnlyDoubleWrapper;
import javafx.beans.property.SimpleDoubleProperty;
import org.curvedrawer.Main;

public class PathTester {
    public static void main(String[] args) {
        NetworkTable.setClientMode();
        NetworkTable.setIPAddress(Main.IP_ADDRESS);


        NetworkTable networkTable = NetworkTable.getTable(Main.NETWORK_TABLE_TABLE_KEY);

        networkTable.addTableListener("Curve", (iTable, s, o, b) -> System.out.println(s + '\t' + o), true);

        SimpleDoubleProperty simpleDoubleProperty = new SimpleDoubleProperty();

        DoubleBinding readOnlyProperty = (simpleDoubleProperty.divide(Main.SCALE_FACTOR));
        ReadOnlyDoubleWrapper.doubleExpression(readOnlyProperty);

        System.out.println("Started");
        for (; ; ) ;
    }
}
