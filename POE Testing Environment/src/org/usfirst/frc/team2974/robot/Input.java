package org.usfirst.frc.team2974.robot;

import static org.usfirst.frc.team2974.robot.RobotConfiguration.GAMEPAD_PORT;
import static org.usfirst.frc.team2974.robot.RobotConfiguration.LEFT_JOYSTICK_PORT;
import static org.usfirst.frc.team2974.robot.RobotConfiguration.RIGHT_JOYSTICK_PORT;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.Button;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import java.util.HashMap;
import java.util.Map;
import org.usfirst.frc.team2974.robot.io.logitech.Gamepad;
import org.usfirst.frc.team2974.robot.io.logitech.GamepadButton;

/**
 * Input Manager
 */
public final class Input {

  private Input() {
  }

  public static final Joystick leftJoystick;
  public static final Joystick rightJoystick;
  public static final Gamepad gamepad;

  public static final Map<String, Button> commands;

  static {
    leftJoystick = new Joystick(LEFT_JOYSTICK_PORT);
    rightJoystick = new Joystick(RIGHT_JOYSTICK_PORT);
    gamepad = new Gamepad(GAMEPAD_PORT);

    commands = new HashMap<>();
    commands.put("", new JoystickButton(gamepad, GamepadButton._1.getIndex()));
  }

  private Button newButton(Joystick controller, )

  public boolean isButtonDown(String bind) {
    return commands.get(bind).get();
  }
}
