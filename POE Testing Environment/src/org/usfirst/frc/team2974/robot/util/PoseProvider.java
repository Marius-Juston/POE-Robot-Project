package org.usfirst.frc.team2974.robot.util;

public interface PoseProvider {

    /**
     * Returns a {@link Pose} with an x,y and an angle coordinate. This is where the robot should
     * be trying to reach and how it should face
     *
     * @return current robot pose
     */
    Pose getPose();

    /**
     * Returns a {@link RobotPair} with the left and right parameters as the distances the left
     * and right wheels have moved by.
     *
     * @return current robot wheel positions
     */
    RobotPair getWheelPositions();
}
