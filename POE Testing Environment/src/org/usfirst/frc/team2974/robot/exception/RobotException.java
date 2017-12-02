package org.usfirst.frc.team2974.robot.exception;

/**
 * Use when there is a major exception in the robot, such as non-existent subsystem.
 */
public class RobotException extends Exception {

  private static final long serialVersionUID = -6607692947811100039L;

  public RobotException(final String message) {
    super(message);
  }
}
