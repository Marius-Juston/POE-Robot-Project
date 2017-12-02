package org.usfirst.frc.team2974.robot.command;

import edu.wpi.first.wpilibj.command.Command;
import org.usfirst.frc.team2974.robot.controllers.MotionPathTurn;
import org.usfirst.frc.team2974.robot.controllers.Pose;
import org.usfirst.frc.team2974.robot.manager.SubsystemManager;
import org.usfirst.frc.team2974.robot.subsystem.DriveTrain;

public class TurnCommand extends Command {

  private final double turnAngle; // angle to turn by in degrees
  private double maxVelocity;
  private double maxAcceleration;

  public TurnCommand(double turnAngle, double maxVelocity, double maxAcceleration) {
    super();
    this.turnAngle = turnAngle;
    this.maxVelocity = maxVelocity;
    this.maxAcceleration = maxAcceleration;
  }

  @Override
  protected void initialize() {
    DriveTrain driveTrain = SubsystemManager.getSubsystem(DriveTrain.class);

    requires(driveTrain);

    Pose start = driveTrain.getPose();

    MotionPathTurn motionPathTurn = new MotionPathTurn(start, Math.toRadians(turnAngle),
        maxVelocity, maxAcceleration);

    driveTrain.addControllerMotion(motionPathTurn);
    driveTrain.startMotion();
  }

  @Override
  protected boolean isFinished() {
    return false;
  }
}
