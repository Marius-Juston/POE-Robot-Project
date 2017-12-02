package org.usfirst.frc.team2974.robot.command.auton;

import static org.usfirst.frc.team2974.robot.RobotMap.leftEncoder;
import static org.usfirst.frc.team2974.robot.RobotMap.rightEncoder;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;
import org.usfirst.frc.team2974.robot.controllers.MotionPathStraight;
import org.usfirst.frc.team2974.robot.controllers.MotionProvider;
import org.usfirst.frc.team2974.robot.manager.SubsystemManager;
import org.usfirst.frc.team2974.robot.subsystem.DriveTrain;

public class DriveForwardCommand extends Command {

  private final double SETTLE_TIME = 1;

  private final DriveTrain driveTrain;

  // in meters
  private final double distance;
  private final double velocity;
  private final double acceleration;

  private double finishedTime;

  private MotionProvider motion;
  private boolean motionFinished;

  /**
   * Drives forward with the parameters:
   * @param distance the distance to move
   * @param velocity the velocity to cruise at
   * @param acceleration the max acceleration to accelerate to velocity and decelerate to 0 m/s at.
   */
  public DriveForwardCommand(double distance, double velocity, double acceleration) {
    super("Drive Forward Auton");

    this.driveTrain = SubsystemManager.getSubsystem(DriveTrain.class);

    this.distance = distance;
    this.velocity = velocity;
    this.acceleration = acceleration;

    this.requires(this.driveTrain);
  }

  @Override
  protected void initialize() {
    motionFinished = false;
    motion = new MotionPathStraight(driveTrain.getPose(), distance, velocity, acceleration);

    driveTrain.addControllerMotion(motion);
    driveTrain.startMotion();
  }

  @Override
  protected void execute() {
    if (!motionFinished && driveTrain.isControllerFinished()) {
      finishedTime = Timer.getFPGATimestamp();
      motionFinished = true;
    }
  }

  @Override
  protected boolean isFinished() {
    return motionFinished && (Timer.getFPGATimestamp() - finishedTime) > SETTLE_TIME;
  }

  @Override
  protected void interrupted() {
    this.end();
  }

  @Override
  protected void end() {
    driveTrain.cancelMotion();
  }
}
