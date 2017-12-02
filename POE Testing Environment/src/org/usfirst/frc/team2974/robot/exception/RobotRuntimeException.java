package org.usfirst.frc.team2974.robot.exception;

/**
 * Exception meant to be used for problems such as dividing by zero and such
 */
public class RobotRuntimeException extends RuntimeException {

  private static final long serialVersionUID = -9035041957129158861L;

  public RobotRuntimeException(final String message) {
    super(message);
  }
}
