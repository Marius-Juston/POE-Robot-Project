package org.usfirst.frc.team2974.robot.subsystems;

import org.usfirst.frc.team2974.robot.commands.Drive;

import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.command.Subsystem;

public class DriveTrain extends Subsystem {
	
	private Talon motorRight;
	private Talon motorLeft;
	
	@Override
	protected void initDefaultCommand() {
		setDefaultCommand(new Drive());
	}

	/**
	 * Sets the power of the wheels, pointing forward.
	 * @param left left motor power
	 * @param right right motor power
	 */
	public synchronized void setSpeeds(double left, double right) {
		// TODO -> check what values go which direction
	}
}
