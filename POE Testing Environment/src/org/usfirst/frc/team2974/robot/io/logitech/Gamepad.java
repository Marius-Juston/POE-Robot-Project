package org.usfirst.frc.team2974.robot.io.logitech;

import edu.wpi.first.wpilibj.Joystick;

/**
 * Gamepad class for the logitech
 */
public class Gamepad extends Joystick {

	public Gamepad(int port) {
		super(port);
	}

	// TODO add comments to magic numbers, or add them as final variables

	/**
	 * Left is 1, Right is -1 FIXME? CHECK
	 *
	 * @return the left thumb stick x value in range [-1, 1]
	 */
	public final double getLeftStickX() {
		return getRawAxis(0);
	}

	/**
	 * Left is 1, Right is -1 FIXME? CHECK
	 *
	 * @return the left thumb stick y value in range [-1, 1]
	 */
	public final double getLeftStickY() {
		return getRawAxis(1);
	}

	/**
	 * Fully pressed is 1, not pressed is 0
	 *
	 * @return the left trigger value in range [0, 1]
	 */
	public final double getLeftTrigger() {
		return getRawAxis(2);
	}

	/**
	 * Left is 1, Right is -1 FIXME? CHECK
	 *
	 * @return the right thumb stick x value in range [-1, 1]
	 */
	public final double getRightStickX() {
		return getRawAxis(4);
	}

	/**
	 * Left is 1, Right is -1 FIXME? CHECK
	 *
	 * @return the right thumb stick y value in range [-1, 1]
	 */
	public final double getRightStickY() {
		return getRawAxis(3);
	}

	/**
	 * Fully pressed is 1, not pressed is 0
	 *
	 * @return the right trigger value in range [0, 1]
	 */
	public final double getRightTrigger() {
		return getRawAxis(5);
	}

	/**
	 * @param b the button to check on this gamepad
	 * @return <b>true</b> if the button b is pressed, <b>false</b> otherwise
	 */
	public final boolean buttonPressed(GamepadButton b) {
		return b.isPressed(this);
	}

	/**
	 * @param p the POV to get based on compass directions N,S,E,W,NE,NW,SE,SW, or CENTER
	 * @return <b>true</b> if the pov button p is pressed, <b>false</b> otherwise
	 */
	public final boolean povButtonPressed(POVButton p) {
		return p.getPressed(this);
	}
}
