package org.usfirst.frc.team2974.robot.subsystem;

import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.command.Subsystem;
import org.usfirst.frc.team2974.robot.HardwareMap;
import org.usfirst.frc.team2974.robot.io.Driver;
import org.usfirst.frc.team2974.robot.io.JoystickButtonKey;
import org.usfirst.frc.team2974.robot.command.Drive;

public class DriveTrain extends Subsystem {

<<<<<<< HEAD:POE Testing Environment/src/org/usfirst/frc/team2974/robot/subsystems/DriveTrain.java
  public static final double WHEEL_DIAMETER = 1; // in meters
  public static final double GEAR_RATIO = 1; // gear ratio from gear train to wheel axle

  private final Talon rightPair;
  private final Talon leftPair;
=======
  private final Talon rightMotor;
  private final Talon leftMotor;
>>>>>>> 02807f16f637f1622be5be9a73a9fd4aac7ea02f:POE Testing Environment/src/org/usfirst/frc/team2974/robot/subsystem/DriveTrain.java

  public DriveTrain() {
    rightMotor = HardwareMap.rightMotor;
    leftMotor = HardwareMap.leftMotor;
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
<<<<<<< HEAD:POE Testing Environment/src/org/usfirst/frc/team2974/robot/subsystems/DriveTrain.java
  public double getLeftMotorPower() {
    return leftPair.get();
=======
  public synchronized double getLeftMotorPower() {
    return leftMotor.get();
>>>>>>> 02807f16f637f1622be5be9a73a9fd4aac7ea02f:POE Testing Environment/src/org/usfirst/frc/team2974/robot/subsystem/DriveTrain.java
  }

  /**
   * Gets the speed for the right motor
   *
   * @return right motor speed
   */
<<<<<<< HEAD:POE Testing Environment/src/org/usfirst/frc/team2974/robot/subsystems/DriveTrain.java
  public double getRightMotorPower() {
    return rightPair.get();
=======
  public synchronized double getRightMotorPower() {
    return rightMotor.get();
>>>>>>> 02807f16f637f1622be5be9a73a9fd4aac7ea02f:POE Testing Environment/src/org/usfirst/frc/team2974/robot/subsystem/DriveTrain.java
  }

  /**
   * Uses the encoder paired with the left motor to get the velocity of the left wheels.
   * @return left wheel velocity in m/s
   */
  public double getLeftWheelVelocity() {

  }

  /**
   * Uses the encoder paired with the right motor to get the velocity of the right wheels.
   * @return right wheel velocity in m/s
   */
  public double getRightWheelVelocity() {

  }

}
