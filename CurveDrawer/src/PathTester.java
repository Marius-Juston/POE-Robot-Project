import edu.wpi.first.wpilibj.networktables.NetworkTable;
import org.curvedrawer.Main;
import org.curvedrawer.path.Spline;
import org.curvedrawer.util.Point;


/**
 * Test class for retrieving what would be sent to the robotRIO
 */
final class PathTester {

//    public static void main(String[] args) {
//        NetworkTable.setClientMode();
//        NetworkTable.setIPAddress(Main.IP_ADDRESS);
//
//        NetworkTable networkTable = NetworkTable.getTable(Main.NETWORK_TABLE_TABLE_KEY);
//
//        networkTable.addTableListener("Curve", (iTable, s, o, b) -> System.out.println(s + '\t' + o), true);
//
//        System.out.println("Started");
//        while (true) ;
//    }

//    public static void main(String[] args) {
//        Spline spline = new Spline(10);
//
//        for (int i = 0; i < 5;i++)
//        {
//            Point point = new Point(i, i,"");
//
//            spline.addPoints(point);
//            spline.createPathPoses();
//        }
//    }

    public static void main(String[] args) {
        for (int n = 0; n < 50; n++) {

            double decimal = 0;
            int sum = 0;


            int stepNumbers = 10;

            for (int i = 0; i < n; i++) {

                sum += stepNumbers / n + Math.round(decimal);
                decimal = (decimal - (int)decimal) + (stepNumbers / (double)n) - (stepNumbers / n) ;
                decimal = decimal - (int)decimal;
            }

            System.out.println(sum);
        }
    }
}
