package org.usfirst.frc.team2974.robot.exception;

/**
 * Use when there is a major exception in a subsystem.
 */
public class SubsystemException extends Exception {

    private static final long serialVersionUID = -6544008222983180372L;

    public SubsystemException(String message) {
        super(message);
    }
}
