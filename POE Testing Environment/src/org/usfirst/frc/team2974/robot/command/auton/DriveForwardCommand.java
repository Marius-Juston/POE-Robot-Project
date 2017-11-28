package org.usfirst.frc.team2974.robot.command.auton;

import edu.wpi.first.wpilibj.command.Command;

import edu.wpi.first.wpilibj.command.PIDCommand;
import org.usfirst.frc.team2974.robot.RobotMap;
import org.usfirst.frc.team2974.robot.manager.SubsystemManager;
import org.usfirst.frc.team2974.robot.subsystem.DriveTrain;

/**
 * @author Evan Tichenor (evan.tichenor@gmail.com)
 * @version 1.0, 11/16/2017
 */
public class DriveForwardCommand extends Command {

  private DriveTrain driveTrain;

  // in meters
  private final double distance;

  private double currentDistance;

  public DriveForwardCommand(double distance) {
    super("Drive Forward Auton");

    this.distance = distance;
    currentDistance = 0;

    driveTrain = SubsystemManager.getSubsystem(DriveTrain.class);

    requires(driveTrain);
  }

  @Override
  protected void initialize() {

  }

  @Override
  protected void execute() {

  }

  @Override
  protected boolean isFinished() {
    return Math.abs(distance - currentDistance) < 1e-3;
  }

  @Override
  protected void interrupted() {
    end();
  }

  @Override
  protected void end() {
    driveTrain.setPowers(0, 0);
  }
}
