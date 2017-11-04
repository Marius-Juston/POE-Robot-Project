package org.usfirst.frc.team2974.robot;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Talon;

public class RobotMap {

	public static final Talon motorLeft;
	public static final Talon motorRight;
	
	public static final Encoder encoderLeft;
	public static final Encoder encoderRight;
	
	
	static {
		motorLeft = new Talon(0);
		motorRight = new Talon(1);
		
		encoderLeft = new Encoder(new DigitalInput(0), new DigitalInput(1));
		encoderRight = new Encoder(new DigitalInput(2), new DigitalInput(3));
	}
}
