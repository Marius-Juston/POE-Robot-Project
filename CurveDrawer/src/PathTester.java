import edu.wpi.first.wpilibj.networktables.NetworkTable;
import org.curvedrawer.Main;


/**
 * Test class for retrieving what would be sent to the robotRIO
 */
final class PathTester {

    public static void main(String[] args) {
        NetworkTable.setClientMode();
        NetworkTable.setIPAddress(Main.IP_ADDRESS);

        NetworkTable networkTable = NetworkTable.getTable(Main.NETWORK_TABLE_TABLE_KEY);

        networkTable.addTableListener("Curve", (iTable, s, o, b) -> System.out.println(s + '\t' + o), true);

        System.out.println("Started");
        while (true) ;
    }
}
