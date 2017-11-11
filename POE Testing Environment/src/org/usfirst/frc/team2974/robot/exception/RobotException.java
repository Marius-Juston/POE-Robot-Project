package org.usfirst.frc.team2974.robot.exception;

/**
 * Use when there is a major exception in the robot, such as non-existent subsystem.
 */
public class RobotException extends Exception {

  public RobotException(String message) {
    super(message);
  }
}
