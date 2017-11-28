package org.usfirst.frc.team2974.robot.subsystem;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Talon;
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

  private static final double PERIOD = .005;
  private static final double DEFAULTKV = 0.5;
  private static final double DEFAULTKK = 0;
  private static final double DEFAULTKA = 0.1;
  private static final double DEFAULTKP = 20;

  private final MotionProfileController motionProfileController;

  private final Talon rightMotor;
  private final Talon leftMotor;

  private final Encoder rightEncoder;
  private final Encoder leftEncoder;

  private final Solenoid shifter;

//  public final PIDController rightController;
//  public final PIDController leftController;

  public DriveTrain() {
    rightMotor = RobotMap.rightMotor;
    leftMotor = RobotMap.leftMotor;

    rightEncoder = RobotMap.rightEncoder;
    leftEncoder = RobotMap.leftEncoder;

    shifter = RobotMap.pneumaticsShifter;

//    rightController = new PIDController(1, 0, 0, 0, rightEncoder, rightMotor);
//    leftController = new PIDController(1, 0, 0, 0, leftEncoder, leftMotor);
//
//    rightController.enable();
//    leftController.enable();

    motionProfileController = new MotionProfileController(this, PERIOD);
    setConstants();
  }

  @Override
  protected void initDefaultCommand() {
    setDefaultCommand(new DriveCommand());
  }

  /**
   * Sets the power of the wheels, pointing forward.
   *
   * @param left left motor power
   * @param right right motor power
   */
  public synchronized void setPowers(double left, double right) {
    leftMotor.set(left);
    rightMotor.set(right);
  }

  /**
   * Gets the speed for the left motor
   *
   * @return left motor speed
   */
  public double getLeftMotorPower() {
    return leftMotor.get();
  }

  /**
   * Gets the speed for the right motor
   *
   * @return right motor speed
   */
  public double getRightMotorPower() {
    return rightMotor.get();
  }

  /**
   * Uses the encoder paired with the left motor to get the velocity of the left wheels.
   * @return left wheel velocity in m/s
   */
  public double getLeftWheelVelocity() {
    return leftEncoder.getRate();
  }

  /**
   * Uses the encoder paired with the right motor to get the velocity of the right wheels.
   * @return right wheel velocity in m/s
   */
  public double getRightWheelVelocity() {
    return rightEncoder.getRate();
  }

  @Override
  public Pose getPose() {
    return new Pose(new Point2D(0, 0), 0);
  }

  public synchronized RobotPair getWheelPositions() {
    return new RobotPair(leftEncoder.getDistance(), rightEncoder.getDistance());
  }

  public boolean getControllerStatus() {
    return motionProfileController.getEnabled();
  }

  public void cancelMotion() {
    motionProfileController.cancel();
  }

  public void startMotion() {
    motionProfileController.enable();
  }

  public void addControllerMotion(MotionProvider motion) {
    motionProfileController.addMotion(motion);
  }

  public boolean isControllerFinished() {
    return motionProfileController.isFinished();
  }

  public void shiftUp() {
    if(shifter.get()) {
      shifter.set(false);
    }
  }

  public void shiftDown() {
    if(!shifter.get()) {
      shifter.set(true);
    }
  }

  public void setConstants() {
    Preferences pref = Preferences.getInstance();
    double kV = pref.getDouble("drivetrain.kV", DEFAULTKV);
    double kK = pref.getDouble("drivetrain.kK", DEFAULTKK);
    double kA = pref.getDouble("drivetrain.kA", DEFAULTKA);
    double kP = pref.getDouble("drivetrain.kP", DEFAULTKP);
    System.out.println(String.format("kV=%f, kK=%f, kA=%f, kP=%f", kV, kK, kA, kP));
    motionProfileController.setKV(kV);
    motionProfileController.setKK(kK);
    motionProfileController.setKA(kA);
    motionProfileController.setKP(kP);
  }
}
