package org.usfirst.frc.team2974.robot.controllers;

public interface PoseProvider {

  /** FIXME? should I have java docs here??
   * @return current robot pose
   */
  Pose getPose();

  /** FIXME? should I have java docs here??
   * @return current robot wheel positions
   */
  RobotPair getWheelPositions();
}
