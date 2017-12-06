package org.usfirst.frc.team2974.robot.command.auton;

import static org.usfirst.frc.team2974.robot.Robot.driveTrain;

import edu.wpi.first.wpilibj.command.Command;
import org.usfirst.frc.team2974.robot.controllers.MotionPathTurn;
import org.usfirst.frc.team2974.robot.controllers.Pose;

public class TurnCommand extends Command {

    private final double turnAngle; // angle to turn by in degrees
    private final double maxVelocity; // the velocity that the robot will try to reach and stay at.
    private final double maxAcceleration; // the acceleration that the robot will try to reach and stay at.
    private MotionPathTurn motionPathTurn;

    /**
     * Initializes the instance variables
     *
     * @param turnAngle angle to turn by, in degrees
     * @param maxVelocity the desired speed the robot should reach
     * @param maxAcceleration the desired acceleration the robot should reach
     */
    public TurnCommand(double turnAngle, double maxVelocity, double maxAcceleration) {
        this.turnAngle = turnAngle;
        this.maxVelocity = maxVelocity;
        this.maxAcceleration = maxAcceleration;

        requires(driveTrain); // tells the command that it will use the drivetrain subsystem
    }

    @Override
    protected final void initialize() {

        final Pose start = driveTrain.getPose(); // gets the position (x,y, angle) of robot

        // creates motion by setting the start position, the angle to turn to in radians, the cruise velocity and the max acceleration
        motionPathTurn = new MotionPathTurn(start, Math.toRadians(turnAngle),
            maxVelocity, maxAcceleration);

        driveTrain
            .addControllerMotion(motionPathTurn); // adds motion  to the motion profile controller
        driveTrain.startMotion(); // starts the motion profile controller
    }

    @Override
    protected final boolean isFinished() { //TODO check that the isFinished no longer returns an error
        return driveTrain.isControllerFinished() || (
            driveTrain.getCurrentMotion().equals(motionPathTurn) && driveTrain
                .isCurrentMotionFinished());
    }
}
