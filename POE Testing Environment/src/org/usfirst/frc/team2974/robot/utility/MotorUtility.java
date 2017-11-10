package org.usfirst.frc.team2974.robot.utility;

import edu.wpi.first.wpilibj.Talon;

public final class MotorUtility {

  private MotorUtility() {
  }

  /**
   * Sets the motor speed so it goes x m/s.
   *
   * @param motor the motor to configure
   * @param mps m/s the motor should go
   */
  public void setMotorSpeed(Talon motor, double mps) {
    // use set because it automatically inverts if it is
    // motor.set();
  }
}
