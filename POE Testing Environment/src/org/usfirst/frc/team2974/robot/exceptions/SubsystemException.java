package org.usfirst.frc.team2974.robot.exceptions;

/**
 * Use when there is a major exception in a subsystem.
 */
public class SubsystemException extends Exception {

  public SubsystemException(String message) {
    super(message);
  }
}
