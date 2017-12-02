package org.usfirst.frc.team2974.robot.command.auton;

import edu.wpi.first.wpilibj.command.Command;
import org.usfirst.frc.team2974.robot.controllers.MotionPathTurn;
import org.usfirst.frc.team2974.robot.controllers.Pose;
import org.usfirst.frc.team2974.robot.manager.SubsystemManager;
import org.usfirst.frc.team2974.robot.subsystem.DriveTrain;

//TODO Test command
public class TurnCommand extends Command {

  private final double turnAngle; // angle to turn by in degrees
  private double maxVelocity; // the velocity that the robot will try to reach and stay at.
  private double maxAcceleration; // the acceleration that the robot will try to reach and stay at.
  private DriveTrain driveTrain = SubsystemManager
      .getSubsystem(DriveTrain.class); // instance of the drivetrain instance
  private MotionPathTurn motionPathTurn;

  /**
   * Initalizes the instance variables
   *
   * @param turnAngle angle to turn by
   * @param maxVelocity the desired speed the robot should reach
   * @param maxAcceleration the desired acceleration the robot should reach
   */
  public TurnCommand(double turnAngle, double maxVelocity, double maxAcceleration) {
    super();
    this.turnAngle = turnAngle;
    this.maxVelocity = maxVelocity;
    this.maxAcceleration = maxAcceleration;
  }

  @Override
  protected void initialize() {
    // gets drivetrain instance

    requires(driveTrain); // tells the command that it will use the drivetrain subsystem

    Pose start = driveTrain.getPose(); // gets the position (x,y, angle) of robot

    // creates motion by setting the start position, the angle to turn to in radians, the cruise velocity and the max acceleration
    motionPathTurn = new MotionPathTurn(start, Math.toRadians(turnAngle),
        maxVelocity, maxAcceleration);

    driveTrain.addControllerMotion(motionPathTurn); // adds motion  to the motion profile controller
    driveTrain.startMotion(); // starts the motion profile controller
  }

  @Override
  protected boolean isFinished() {
    return driveTrain.getCurrentMotion().equals(motionPathTurn) && driveTrain
        .isCurrentMotionFinished();
  }
}
