package org.usfirst.frc.team2974.robot;

import edu.wpi.first.wpilibj.AnalogGyro;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.interfaces.Gyro;

/**
 * This class is where you initialize every motor, talon, solenoid, digital inputs, analog inputs
 * etc... There variables should all be static final
 */
public class HardwareMap {

  public static final Talon leftMotor;
  public static final Talon rightMotor;

  public static final Encoder leftEncoder;
  public static final Encoder rightEncoder;

  public static final Solenoid gearIntakeSolenoid;
  public static final Talon climberMotor;

  public static final Compressor compressor;

  public static final Gyro gyroscope;

  static {
    // the front is where the camera is now. deal with it.
    rightMotor = new Talon(0);
    leftMotor = new Talon(1);

    leftMotor.setInverted(true);

    leftEncoder = new Encoder(new DigitalInput(0), new DigitalInput(1));
    rightEncoder = new Encoder(new DigitalInput(2), new DigitalInput(3));

    gearIntakeSolenoid = new Solenoid(2);

    climberMotor = new Talon(4);

    compressor = new Compressor();

    gyroscope = new AnalogGyro(0);
  }
}
