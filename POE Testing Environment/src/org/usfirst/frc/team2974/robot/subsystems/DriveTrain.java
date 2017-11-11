package org.usfirst.frc.team2974.robot.subsystems;

import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.command.Subsystem;
import org.usfirst.frc.team2974.robot.Driver;
import org.usfirst.frc.team2974.robot.JoystickButtonKey;
import org.usfirst.frc.team2974.robot.RobotMap;
import org.usfirst.frc.team2974.robot.commands.Drive;

public class DriveTrain extends Subsystem {

  private final Talon rightMotor;
  private final Talon leftMotor;

  public DriveTrain() {
    rightMotor = RobotMap.rightMotor;
    leftMotor = RobotMap.leftMotor;
  }

  @Override
  protected void initDefaultCommand() {
    Driver doNothingDriver = new Driver("Do Nothing Driver") {
      @Override
      public void initButtons() { 
        addJoystick("Left Joystick", 0);
        createAndAddJoystickButton("Left Joystick", "Kicker Button", JoystickButtonKey.TRIGGER);
      }
    };
     
    setDefaultCommand(new Drive(doNothingDriver));
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
    return leftMotor.get();
  }

  /**
   * Gets the speed for the right motor
   *
   * @return right motor speed
   */
  public synchronized double getRightMotorPower() {
    return rightMotor.get();
  }

}
