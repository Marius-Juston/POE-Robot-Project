package org.usfirst.frc.team2974.robot.util;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.usfirst.frc.team2974.robot.RobotConfiguration;

public class PointRetriever {

	private static NetworkTable networkTable = NetworkTableInstance.getDefault()
		.getTable(RobotConfiguration.PATH_NETWORKTABLE);


	public static Pose[] retrieveSmartDashboardPoses(String smartDashboardKey) {
		return retrievePoses(SmartDashboard.getString(smartDashboardKey, ""));
	}

	public static Pose[] retrievePoses(String poseString) {
		String[] stringPoses = poseString.trim().split(" ");

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
