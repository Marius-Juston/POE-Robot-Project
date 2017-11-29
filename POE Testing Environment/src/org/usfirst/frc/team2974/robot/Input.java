package org.usfirst.frc.team2974.robot;

import edu.wpi.first.wpilibj.Joystick;
import org.usfirst.frc.team2974.robot.io.logitech.Gamepad;

/**
 * Input Manager
 */
public class Input {

  public static final Joystick leftJoystick;
  public static final Joystick rightJoystick;
  public static final Gamepad gamepad;

  static {
    leftJoystick = new Joystick(0);
    rightJoystick = new Joystick(1);
    gamepad = new Gamepad(2);
  }
}
