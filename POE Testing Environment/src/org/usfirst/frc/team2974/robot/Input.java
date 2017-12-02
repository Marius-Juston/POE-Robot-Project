package org.usfirst.frc.team2974.robot;

import static org.usfirst.frc.team2974.robot.RobotConfiguration.GAMEPAD_PORT;
import static org.usfirst.frc.team2974.robot.RobotConfiguration.LEFT_JOYSTICK_PORT;
import static org.usfirst.frc.team2974.robot.RobotConfiguration.RIGHT_JOYSTICK_PORT;

import edu.wpi.first.wpilibj.Joystick;
import org.usfirst.frc.team2974.robot.io.logitech.Gamepad;

/**
 * Input Manager
 */
public final class Input {

  public static final Joystick leftJoystick;
  public static final Joystick rightJoystick;
  public static final Gamepad gamepad;

  static {
    leftJoystick = new Joystick(LEFT_JOYSTICK_PORT);
    rightJoystick = new Joystick(RIGHT_JOYSTICK_PORT);
    gamepad = new Gamepad(GAMEPAD_PORT);
  }

  private Input() {
  }
}
