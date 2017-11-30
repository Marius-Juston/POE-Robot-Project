package org.usfirst.frc.team2974.robot.command.auton;

import edu.wpi.first.wpilibj.command.Command;
import org.usfirst.frc.team2974.robot.manager.SubsystemManager;
import org.usfirst.frc.team2974.robot.subsystem.DriveTrain;

public class DriveForwardCommand extends Command {

  // in meters
  private final double distance;
  private final DriveTrain driveTrain;
  private final double currentDistance;

  public DriveForwardCommand(double distance) {
    super("Drive Forward Auton");

    this.distance = distance;
    this.currentDistance = 0;

    this.driveTrain = SubsystemManager.getSubsystem(DriveTrain.class);

    this.requires(this.driveTrain);
  }

  @Override
  protected void initialize() {

  }

  @Override
  protected void execute() {

  }

  @Override
  protected boolean isFinished() {
    return Math.abs(this.distance - this.currentDistance) < 1e-3;
  }

  @Override
  protected void interrupted() {
    this.end();
  }

  @Override
  protected void end() {
    this.driveTrain.setPowers(0, 0);
  }
}
