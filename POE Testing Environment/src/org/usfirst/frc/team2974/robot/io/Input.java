package org.usfirst.frc.team2974.robot.io;

import static org.usfirst.frc.team2974.robot.RobotConfiguration.GAMEPAD_PORT;
import static org.usfirst.frc.team2974.robot.RobotConfiguration.LEFT_JOYSTICK_PORT;
import static org.usfirst.frc.team2974.robot.RobotConfiguration.RIGHT_JOYSTICK_PORT;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.Button;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import java.util.HashMap;
import java.util.Map;
import org.usfirst.frc.team2974.robot.io.logitech.Gamepad;

/**
 * Input Manager
 */
public final class Input {

	public static final Joystick leftJoystick;
	public static final Joystick rightJoystick;
	public static final Gamepad gamepad;

	private static final Map<String, Button> binds;

	static {
		leftJoystick = new Joystick(LEFT_JOYSTICK_PORT);
		rightJoystick = new Joystick(RIGHT_JOYSTICK_PORT);
		gamepad = new Gamepad(GAMEPAD_PORT);

		binds = new HashMap<>();
	}

	private Input() {
	}

	public static void clearBinds() {
		binds.clear();
	}

	public static void removeBind(String name) {
		binds.remove(name);
	}

	public static void addBind(String name, Joystick joystick, int button) {
		binds.put(name, new JoystickButton(joystick, button));
	}

	public static boolean isButtonDown(String name) {
		return binds.get(name).get();
	}
}
