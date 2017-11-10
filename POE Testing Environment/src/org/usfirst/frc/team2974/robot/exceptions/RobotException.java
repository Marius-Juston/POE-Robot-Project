package org.usfirst.frc.team2974.robot.exceptions;

/**
 * Use when there is a major exception in the robot, such as non-existent subsystems.
 */
public class RobotException extends Exception {

  public RobotException(String message) {
    super(message);
  }
}
