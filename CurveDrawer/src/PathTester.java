import edu.wpi.first.wpilibj.networktables.NetworkTable;
import edu.wpi.first.wpilibj.tables.ITable;
import edu.wpi.first.wpilibj.tables.ITableListener;
import org.curvedrawer.Main;

public class PathTester {
    public static void main(String[] args) {
        NetworkTable.setClientMode();
        NetworkTable.setIPAddress(Main.IP_ADDRESS);


        NetworkTable networkTable = NetworkTable.getTable(Main.NETWORK_TABLE_TABLE_KEY);

        networkTable.addTableListener("Curve", (iTable, s, o, b) -> System.out.println(s + '\t' + o), true);

        for (;;);
    }
}
