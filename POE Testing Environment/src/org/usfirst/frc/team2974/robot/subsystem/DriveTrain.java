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
import org.usfirst.frc.team2974.robot.RobotMap;
import org.usfirst.frc.team2974.robot.command.DriveCommand;
import org.usfirst.frc.team2974.robot.controllers.MotionProfileController;
import org.usfirst.frc.team2974.robot.controllers.MotionProvider;
import org.usfirst.frc.team2974.robot.controllers.Point2D;
import org.usfirst.frc.team2974.robot.controllers.Pose;
import org.usfirst.frc.team2974.robot.controllers.PoseProvider;
import org.usfirst.frc.team2974.robot.controllers.RobotPair;

public class DriveTrain extends Subsystem implements PoseProvider {


  private final MotionProfileController motionProfileController;

  private final Talon rightMotor;
  private final Talon leftMotor;

  private final Encoder rightEncoder;
  private final Encoder leftEncoder;

  private final Solenoid shifter;

//  public final PIDController rightController;
//  public final PIDController leftController;

  public DriveTrain() {
    this.rightMotor = RobotMap.rightMotor;
    this.leftMotor = RobotMap.leftMotor;

    this.rightEncoder = RobotMap.rightEncoder;
    this.leftEncoder = RobotMap.leftEncoder;

    this.shifter = RobotMap.pneumaticsShifter;

//    rightController = new PIDController(1, 0, 0, 0, rightEncoder, rightMotor);
//    leftController = new PIDController(1, 0, 0, 0, leftEncoder, leftMotor);
//
//    rightController.enable();
//    leftController.enable();

    this.motionProfileController = new MotionProfileController(this, PERIOD);
    this.setConstants();
  }

  @Override
  protected final void initDefaultCommand() {
    this.setDefaultCommand(new DriveCommand());
  }

  /**
   * Sets the power of the wheels, pointing forward.
   *
   * @param left left motor power
   * @param right right motor power
   */
  public final synchronized void setPowers(final double left, final double right) {
    this.leftMotor.set(left);
    this.rightMotor.set(right);
  }

  /**
   * Gets the speed for the left motor
   *
   * @return left motor speed
   */
  public final double getLeftMotorPower() {
    return this.leftMotor.get();
  }

  /**
   * Gets the speed for the right motor
   *
   * @return right motor speed
   */
  public final double getRightMotorPower() {
    return this.rightMotor.get();
  }

  /**
   * Uses the encoder paired with the left motor to get the velocity of the left wheels.
   *
   * @return left wheel velocity in m/s
   */
  public final double getLeftWheelVelocity() {
    return this.leftEncoder.getRate();
  }

  /**
   * Uses the encoder paired with the right motor to get the velocity of the right wheels.
   *
   * @return right wheel velocity in m/s
   */
  public final double getRightWheelVelocity() {
    return this.rightEncoder.getRate();
  }

  @Override
  public final Pose getPose() {
    return new Pose(new Point2D(0, 0), 0);
  }

  public final RobotPair getWheelPositions() {
    return new RobotPair(this.leftEncoder.getDistance(), this.rightEncoder.getDistance());
  }

  public final boolean getControllerStatus() {
    return this.motionProfileController.isEnabled();
  }

  public final void cancelMotion() {
    this.motionProfileController.cancel();
  }

  public final void startMotion() {
    this.motionProfileController.enable();
  }

  public final void addControllerMotion(final MotionProvider motion) {
    this.motionProfileController.addMotion(motion);
  }

  public final boolean isControllerEmpty() {
    return this.motionProfileController.isFinished();
  }

  public final boolean isCurrentMotionFinished() {
    return this.motionProfileController.isCurrentMotionFinished(Timer.getFPGATimestamp());
  }

  public final MotionProvider getCurrentMotion() {
    return this.motionProfileController.getCurrentMotion();
  }


  public final void shiftUp() {
    if (this.shifter.get()) {
      this.shifter.set(false);
    }
  }

  public final void shiftDown() {
    if (!this.shifter.get()) {
      this.shifter.set(true);
    }
  }

  public final void setConstants() {
    final Preferences pref = Preferences.getInstance();
    final double kV = pref.getDouble("drivetrain.kV", DEFAULT_KV);
    final double kK = pref.getDouble("drivetrain.kK", DEFAULT_KK);
    final double kA = pref.getDouble("drivetrain.kA", DEFAULT_KA);
    final double kP = pref.getDouble("drivetrain.kP", DEFAULT_KP);
    System.out.println(String.format("kV=%f, kK=%f, kA=%f, kP=%f", kV, kK, kA, kP));
    this.motionProfileController.setKV(kV);
    this.motionProfileController.setKK(kK);
    this.motionProfileController.setKA(kA);
    this.motionProfileController.setKP(kP);
  }
}
