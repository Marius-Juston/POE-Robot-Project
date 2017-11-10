package org.usfirst.frc.team2974.robot.exceptions;

/**
 * Exception meant to be used for problems such as dividing by zero and such
 */
public class RobotRuntimeException extends RuntimeException {

  public RobotRuntimeException(String message) {
    super(message);
  }
}
