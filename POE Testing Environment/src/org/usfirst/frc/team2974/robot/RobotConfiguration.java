package org.usfirst.frc.team2974.robot;

/**
 * DO NOT DELETE ANYTHING.
 */
public final class RobotConfiguration {

	public static final double DISTANCE_PER_PULSE = 0.0005652; //distance per pulse for encoder
	public static final int LEFT_JOYSTICK_PORT = 0;
	public static final int RIGHT_JOYSTICK_PORT = 1;
	public static final int GAMEPAD_PORT = 2;
	public static final double PERIOD = 0.005;
	public static final double DEFAULT_KV = 0.5;
	public static final double DEFAULT_KK = 0;
	public static final double DEFAULT_KA = 0.1;
	public static final double DEFAULT_KP = 20.0;
	public static final double RIGHT_JOYSTICK_THRESHOLD = 0.3;
	public static final double LEFT_JOYSTICK_THRESHOLD = 0.3;
	public static final double ROBOT_WIDTH = 0.70485; // The width of the robot in meters.
	public static final int N_POINTS = 50; // number of steps the motions is divided into. i.e. greater = more accurate
	public static final boolean IS_DEBUG = true; // shows the debug SmartDashboard keys

	public static final String PATH_NETWORKTABLE = "SmartDashboard";

//    public static final int LEFT_ENCODER_PORT = ;
//    public static final int RIGHT_ENCODER_PORT = ;
//    public static final int LEFT_MOTOR_PORT = 1;
//    public static final int RIGHT_MOTOR_PORT = 0;

	private RobotConfiguration() {
	}
}
