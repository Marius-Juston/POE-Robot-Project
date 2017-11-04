package org.usfirst.frc.team2974.robot.exceptions;

/**
 * @author Evan Tichenor (evan.tichenor@gmail.com)
 * @version 1.0, 11/3/2017
 *
 *          Use when there is a major exception in the robot, such as non-existent subsystems.
 */
public class RobotException extends Exception {

  public RobotException(String message) {
    super(message);
  }
}
