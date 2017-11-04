package org.usfirst.frc.team2974.robot;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Talon;

public class RobotMap {

  public static final Talon leftMotor;
  public static final Talon rightMotor;

  public static final Encoder leftEncoder;
  public static final Encoder rightEncoder;


  static {
    leftMotor = new Talon(0);
    rightMotor = new Talon(1);

    leftEncoder = new Encoder(new DigitalInput(0), new DigitalInput(1));
    rightEncoder = new Encoder(new DigitalInput(2), new DigitalInput(3));
  }
}
