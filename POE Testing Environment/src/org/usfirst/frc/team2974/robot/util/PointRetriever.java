package org.usfirst.frc.team2974.robot.util;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;

public class PointRetriever {

    private static NetworkTable networkTable = NetworkTableInstance.getDefault().getTable("SmartDashboard");

    
    public static Pose[] retieveSmartDashboardPoses(String smartDashboardKey)
    {
      return retrievePoses(networkTable.getEntry(smartDashboardKey).getString(""));
    }
    
    public static Pose[] retrievePoses(String string) {
        String[] stringPoses = string.trim().split(" ");
      
//      System.out.println(smartDashboardKey + "\t" + networkTable.getString(smartDashboardKey, ""));
      
//        if (stringPoses.length % 3 != 0) {
//            throw new RobotRuntimeException(
//                "The key " + smartDashboardKey + " does not contain valid pose: " + Arrays
//                    .toString(stringPoses));
//        }

        Pose[] pose = new Pose[stringPoses.length / 3];

        for (int i = 0; i < stringPoses.length; i += 3) {
            double x = Double.parseDouble(stringPoses[i]);
            double y = Double.parseDouble(stringPoses[i + 1]);
            double angle = Double.parseDouble(stringPoses[i + 2]);

            pose[i / 3] = new Pose(new Point2D(x, y), angle);
        }

        return pose;
    }
}
