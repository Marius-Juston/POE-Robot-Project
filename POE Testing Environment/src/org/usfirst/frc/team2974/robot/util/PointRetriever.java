package org.usfirst.frc.team2974.robot.util;

import edu.wpi.first.wpilibj.networktables.NetworkTable;
import java.util.Arrays;
import org.usfirst.frc.team2974.robot.RobotConfiguration;
import org.usfirst.frc.team2974.robot.exception.RobotRuntimeException;

public class PointRetriever {

    private static NetworkTable networkTable = NetworkTable
        .getTable(RobotConfiguration.PATH_NETWORKTABLE);

    public static Pose[] retrievePoses(String smartDashboardKey) {
        
      System.out.println(smartDashboardKey + "\t" + networkTable.getString(smartDashboardKey, ""));
      String[] stringPoses = networkTable.getString(smartDashboardKey, "").split(" ");

//        if (stringPoses.length % 3 != 0) {
//            throw new RobotRuntimeException(
//                "The key " + smartDashboardKey + " does not contain valid pose: " + Arrays
//                    .toString(stringPoses));
//        }

        Pose[] pose = new Pose[stringPoses.length / 3];

        for (int i = 0; i < stringPoses.length; i += 3) {
            double x = Double.parseDouble(stringPoses[i]);
            double y = Double.parseDouble(stringPoses[i + 1]);
            double angle = Double.parseDouble(stringPoses[i = 2]);

            pose[i / 3] = new Pose(new Point2D(x, y), angle);
        }

        return pose;
    }
}
