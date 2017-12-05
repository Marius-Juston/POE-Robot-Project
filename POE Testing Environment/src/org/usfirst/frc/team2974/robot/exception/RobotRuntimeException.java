package org.usfirst.frc.team2974.robot.exception;

/**
 * Exception meant to be used for problems with logic
 */
public class RobotRuntimeException extends RuntimeException {

    private static final long serialVersionUID = -9035041957129158861L;

    public RobotRuntimeException(String message) {
        super(message);
    }
}
