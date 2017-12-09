package org.usfirst.frc.team2974.robot.subsystem;

import static org.usfirst.frc.team2974.robot.RobotConfiguration.DEFAULT_KA;
import static org.usfirst.frc.team2974.robot.RobotConfiguration.DEFAULT_KK;
import static org.usfirst.frc.team2974.robot.RobotConfiguration.DEFAULT_KP;
import static org.usfirst.frc.team2974.robot.RobotConfiguration.DEFAULT_KV;
import static org.usfirst.frc.team2974.robot.RobotConfiguration.PERIOD;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Subsystem;
import org.usfirst.frc.team2974.robot.MotionProfileController;
import org.usfirst.frc.team2974.robot.RobotMap;
import org.usfirst.frc.team2974.robot.command.DriveCommand;
import org.usfirst.frc.team2974.robot.util.MotionProvider;
import org.usfirst.frc.team2974.robot.util.Point2D;
import org.usfirst.frc.team2974.robot.util.Pose;
import org.usfirst.frc.team2974.robot.util.PoseProvider;
import org.usfirst.frc.team2974.robot.util.RobotPair;

public class DriveTrain extends Subsystem implements PoseProvider {

    private final MotionProfileController motionProfileController;

    private final Talon rightMotor;
    private final Talon leftMotor;

    private final Encoder rightEncoder;
    private final Encoder leftEncoder;

    private final Solenoid shifter;

    public DriveTrain() {
        rightMotor = RobotMap.rightMotor;
        leftMotor = RobotMap.leftMotor;

        rightEncoder = RobotMap.rightEncoder;
        leftEncoder = RobotMap.leftEncoder;

        shifter = RobotMap.pneumaticsShifter;

        motionProfileController = new MotionProfileController(this, PERIOD);
        setConstants();
    }

    @Override
    protected final void initDefaultCommand() {
        setDefaultCommand(new DriveCommand());
    }

    /**
     * Sets the power of the wheels, pointing forward.
     *
     * @param left left motor power
     * @param right right motor power
     */
    public final synchronized void setPowers(double left, double right) {
        leftMotor.set(left);
        rightMotor.set(right);
    }

    /**
     * Gets the speed for the left motor
     *
     * @return left motor speed
     */
    public final double getLeftMotorPower() {
        return leftMotor.get();
    }

    /**
     * Gets the speed for the right motor
     *
     * @return right motor speed
     */
    public final double getRightMotorPower() {
        return rightMotor.get();
    }

    /**
     * Uses the encoder paired with the left motor to get the velocity of the left wheels.
     *
     * @return left wheel velocity in m/s
     */
    public final double getLeftWheelVelocity() {
        return leftEncoder.getRate();
    }

    /**
     * Uses the encoder paired with the right motor to get the velocity of the right wheels.
     *
     * @return right wheel velocity in m/s
     */
    public final double getRightWheelVelocity() {
        return rightEncoder.getRate();
    }

    /**
     * Returns the position of the robot
     *
     * @return position of the robot
     */
    @Override
    public final Pose getPose() {
        return new Pose(new Point2D(0, 0),
            0); //TODO make it so that it actually returns an estimated position of the robot
    }

    @Override
    public final RobotPair getWheelPositions() {
        return new RobotPair(leftEncoder.getDistance(), rightEncoder.getDistance());
    }

    /**
     * Checks if the motion profile controller is enabled
     *
     * @return true if motion profile controller has been enabled false otherwise
     */
    public final boolean getControllerStatus() {
        return motionProfileController.isEnabled();
    }

    /**
     * Stop running the motions in the motion profile cnotroller by removing all the motions and setting speeds to 0
     */
    public final void cancelMotion() {
        motionProfileController.cancel();
    }

    /**
     * Start to run the motions in the motion profile controller
     */
    public final void startMotion() {
        motionProfileController.enable();
    }

    /**
     * Adds the motion to the motion profile controller to run
     *
     * @param motion motion to add to the controller
     */
    public final void addControllerMotion(final MotionProvider motion) {
        motionProfileController.addMotion(motion);
    }

    /**
     * Checks if the motion controller has no more motions left to execute
     *
     * @return true if there are no more motions left false otherwise
     */
    public final boolean isControllerFinished() {
        return motionProfileController.isFinished();
    }

    /**
     * Checks if the current motion has been finished.
     *
     * @return true if the motion has finished false otherwise
     */
    public final boolean isCurrentMotionFinished() {
        return motionProfileController.isCurrentMotionFinished(Timer.getFPGATimestamp());
    }

    /**
     * Gets the current motion being run from the current kinematic
     *
     * @return current motion
     */
    public final MotionProvider getCurrentMotion() {
        return motionProfileController.getCurrentMotion();
    }

    /**
     * Shifts down (will make the robot slower but more accurate)
     */
    public final void shiftUp() {
        if (shifter.get()) {
            shifter.set(false);
        }
    }

    /**
     * Shifts up (will make the robot faster but less accurate)
     */
    public final void shiftDown() {
        if (!shifter.get()) {
            shifter.set(true);
        }
    }

    /**
     * Sets the PID constants by retrieving the kV, kK, kA, kP values from the Preference table
     */
    public final void setConstants() {
        final Preferences pref = Preferences.getInstance();
        final double kV = pref.getDouble("drivetrain.kV", DEFAULT_KV);
        final double kK = pref.getDouble("drivetrain.kK", DEFAULT_KK);
        final double kA = pref.getDouble("drivetrain.kA", DEFAULT_KA);
        final double kP = pref.getDouble("drivetrain.kP", DEFAULT_KP);
        System.out.println(String.format("kV=%f, kK=%f, kA=%f, kP=%f", kV, kK, kA, kP));
        motionProfileController.setKV(kV);
        motionProfileController.setKK(kK);
        motionProfileController.setKA(kA);
        motionProfileController.setKP(kP);
    }
}
