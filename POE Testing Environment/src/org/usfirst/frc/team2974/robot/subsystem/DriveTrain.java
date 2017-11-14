package org.usfirst.frc.team2974.robot.subsystem;

import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.command.Subsystem;
import org.usfirst.frc.team2974.robot.RobotMap;
import org.usfirst.frc.team2974.robot.command.Drive;

public class DriveTrain extends Subsystem {

  public static final double WHEEL_DIAMETER = 1; // in meters
  public static final double GEAR_RATIO = 1; // gear ratio from gear train to wheel axle

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
  public synchronized void setPowers(double left, double right) {
    leftPair.set(left);
    rightPair.set(right);
  }

  // TODO!! remember to check what values go which direction!

  /**
   * Gets the speed for the left motor
   *
   * @return left motor speed
   */
  public double getLeftMotorPower() {
    return leftPair.get();
  }

  /**
   * Gets the speed for the right motor
   *
   * @return right motor speed
   */
  public double getRightMotorPower() {
    return rightPair.get();}


  /**
   * Uses the encoder paired with the left motor to get the velocity of the left wheels.
   * @return left wheel velocity in m/s
   */
  public double getLeftWheelVelocity() {
    return 0;
  }

  /**
   * Uses the encoder paired with the right motor to get the velocity of the right wheels.
   * @return right wheel velocity in m/s
   */
  public double getRightWheelVelocity() {
    return 0;
  }

}
