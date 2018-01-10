package org.usfirst.frc.team2974.robot.util;

import edu.wpi.first.wpilibj.networktables.NetworkTable;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import java.util.Arrays;
import org.usfirst.frc.team2974.robot.RobotConfiguration;
import org.usfirst.frc.team2974.robot.exception.RobotRuntimeException;

public class PointRetriever {

    private static NetworkTable networkTable = NetworkTable
        .getTable(RobotConfiguration.PATH_NETWORKTABLE);

    public static Pose[] retrievePoses(String smartDashboardKey) {
        String[] stringPoses = SmartDashboard.getString(smartDashboardKey, "").trim().split(" ");
        
        Pose[] pose = new Pose[stringPoses.length / 3];
        
       // System.out.println(Arrays.toString(stringPoses));
        
        
        for (int i = 0; i < stringPoses.length; i += 3) {
            double x = Double.parseDouble(stringPoses[i]);
            double y = Double.parseDouble(stringPoses[i + 1]);
            double angle = Double.parseDouble(stringPoses[i + 2]);

            pose[i / 3] = new Pose(new Point2D(x, y), angle);
        }
        
        //System.out.println(Arrays.toString(pose));

        return pose;
    }
}
