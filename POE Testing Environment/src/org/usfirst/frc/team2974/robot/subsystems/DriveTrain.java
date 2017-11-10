package org.usfirst.frc.team2974.robot.subsystems;

import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.command.Subsystem;
import org.usfirst.frc.team2974.robot.RobotMap;
import org.usfirst.frc.team2974.robot.commands.Drive;

public class DriveTrain extends Subsystem {

  private final Talon rightPair;
  private final Talon leftPair;

  public DriveTrain() {
    rightPair = RobotMap.rightMotor;
    leftPair = RobotMap.leftMotor;
  }

  @Override
  protected void initDefaultCommand() {
    setDefaultCommand(new Drive());
  }

  /**
   * Sets the power of the wheels, pointing forward.
   *
   * @param left left motor power
   * @param right right motor power
   */
  public synchronized void setSpeeds(double left, double right) {
    // TODO -> check what values go which direction
  }

  // TODO!! remember to check what values go which direction!

  /**
   * Gets the speed for the left motor
   *
   * @return left motor speed
   */
  public synchronized double getLeftMotorPower() {
    return leftPair.get();
  }

  /**
   * Gets the speed for the right motor
   *
   * @return right motor speed
   */
  public synchronized double getRightMotorPower() {
    return rightPair.get();
  }

}
