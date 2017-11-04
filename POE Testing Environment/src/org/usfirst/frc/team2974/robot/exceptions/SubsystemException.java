package org.usfirst.frc.team2974.robot.exceptions;

/**
 * @author Evan Tichenor (evan.tichenor@gmail.com)
 * @version 1.0, 11/3/2017
 *
 * Use when there is a major exception in a subsystem.
 */
public class SubsystemException extends Exception {

    public SubsystemException(String message) {
        super(message);
    }
}
