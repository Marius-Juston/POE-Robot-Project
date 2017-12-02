package org.usfirst.frc.team2974.robot.controllers;

import static org.usfirst.frc.team2974.robot.RobotConfiguration.N_POINTS;

import edu.wpi.first.wpilibj.Timer;
import java.util.TimerTask;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;
import org.usfirst.frc.team2974.robot.manager.SubsystemManager;
import org.usfirst.frc.team2974.robot.subsystem.DriveTrain;

public class MotionProfileController {

  private final BlockingDeque<MotionProvider> motions = new LinkedBlockingDeque<>(); // motions that we want to do
  private final PoseProvider poseProvider; // wheel positions and pose
  private final java.util.Timer controller; // schedules all of the motions to be run, TIMER
  // PID values
  private double kV; // what we want to change the voltage by to get the motion we want
  private double kK; // up/down translation
  private double kA; // FIXME?? WHAT IS THIS
  private double kP; // what we want to change the power by to get the motion we want
  private Kinematics currentKinematics; // does all the math
  private KinematicPose staticKinematicPose; // has the vectors that we use in the calculations
  private boolean isEnabled; // yeah, this

  public MotionProfileController(final PoseProvider poseProvider, final double period) {

    this.poseProvider = poseProvider;

    this.controller = new java.util.Timer();

    this.controller.schedule(new MPCTask(), 0L, (long) (period * 1000.0));

    this.staticKinematicPose = Kinematics
        .staticPose(poseProvider.getPose(), poseProvider.getWheelPositions(),
            Timer.getFPGATimestamp());
  }

  /**
   * Stops the motion
   */
  public final void free() {
    this.controller.cancel();
    //RobotLoggerManager.setFileHandlerInstance("robot.controller").info("MPCTask is destroyed.");
  }

  public final synchronized void addMotion(final MotionProvider motion) {
    this.motions.addLast(motion);
    System.out.println("added motion " + motion);
  }

  public final synchronized void enable() {
    System.out
        .println(String.format("kK=%f, kV=%f, kA=%f, kP=%f", this.kK, this.kV, this.kA, this.kP));
    final MotionProvider newMotion = this.motions.poll();
    if (newMotion != null) {
//			System.out.println("starting new motion:" + currentKinematics.toString());
      this.currentKinematics = new Kinematics(newMotion, this.poseProvider.getWheelPositions(),
          Timer.getFPGATimestamp(), 0, 0, N_POINTS);
      this.isEnabled = true;
    }
  }

  /**
   * Hard stop
   */
  public final synchronized void cancel() {
    this.isEnabled = false;
    this.currentKinematics = null;
    this.motions.clear();
    SubsystemManager.getSubsystem(DriveTrain.class).setPowers(0, 0);
  }

  public boolean isEnabled() {
    return this.isEnabled;
  }

  public final synchronized double getKV() {
    return this.kV;
  }

  public final synchronized void setKV(final double kV) {
    this.kV = kV;
  }

  public final synchronized double getKK() {
    return this.kK;
  }

  public final synchronized void setKK(final double kK) {
    this.kK = kK;
  }

  public final synchronized double getKA() {
    return this.kA;
  }

  public final synchronized void setKA(final double kA) {
    this.kA = kA;
  }

  public final synchronized double getKP() {
    return this.kP;
  }

  public final synchronized void setKP(final double kP) {
    this.kP = kP;
  }

  /**
   * This is the PID loop, accounts for distance error.
   */
  private void calculate() {

    double leftPower = 0;
    double rightPower = 0;
    final boolean enabled;

    synchronized (this) {
      enabled = isEnabled;
    }

    if (enabled) {

      final double time = Timer.getFPGATimestamp();

      final RobotPair wheelPositions = this.poseProvider.getWheelPositions();

      final KinematicPose kinematicPose;
      kinematicPose = this.currentKinematics != null ? this.currentKinematics.interpolatePose(time)
          : this.staticKinematicPose;

//			System.out.println("time:" + time+ " " + wheelPositions + " " + kinematicPose);
      synchronized (this) {
        //feed forward
        leftPower += ((this.kV * kinematicPose.left.velocity) + this.kK)
            + (this.kA * kinematicPose.left.acceleration);
        rightPower +=
            ((this.kV * kinematicPose.right.velocity) + this.kK)
                + (this.kA * kinematicPose.right.acceleration);
        //feed back
        leftPower += this.kP * (kinematicPose.left.length - wheelPositions.left);
        rightPower += this.kP * (kinematicPose.right.length - wheelPositions.right);

      }

      leftPower = Math.max(-1.0, Math.min(1.0, leftPower));
      rightPower = Math.max(-1.0, Math.min(1.0, rightPower));

//			System.out.println(String.format("LP=%f,RP=%f, err_l=%f, err_r=%f", leftPower,rightPower,
//					kinematicPose.left.l - wheelPositions.left,
//					kinematicPose.right.l - wheelPositions.right));

      SubsystemManager.getSubsystem(DriveTrain.class).setPowers(leftPower, rightPower);

      if (kinematicPose.isFinished) {
        final MotionProvider newMotion = this.motions.pollFirst();
        if (newMotion != null) {
          this.currentKinematics = new Kinematics(newMotion,
              this.currentKinematics.getWheelPositions(),
              this.currentKinematics.getTime(), 0, 0, N_POINTS);
//					System.out.println("starting new motion:" + currentKinematics.toString());
        } else {
          this.staticKinematicPose = Kinematics
              .staticPose(this.currentKinematics.getPose(),
                  this.currentKinematics.getWheelPositions(),
                  this.currentKinematics.getTime());
          this.currentKinematics = null;
        }
      }
    }
  }

  public final boolean isCurrentMotionFinished(final double time) {
    final KinematicPose kinematicPose;
    kinematicPose = this.currentKinematics != null ? this.currentKinematics.interpolatePose(time)
        : this.staticKinematicPose;

    return kinematicPose.isFinished;
  }

  public final boolean isFinished() {
    return this.currentKinematics == null;
  }

  public final MotionProvider getCurrentMotion() {
    return currentKinematics.getMotion();
  }

  @Override
  public final String toString() {
    return "MotionProfileController{" +
        "motions=" + motions +
        ", kV=" + kV +
        ", kK=" + kK +
        ", kA=" + kA +
        ", kP=" + kP +
        ", currentKinematics=" + currentKinematics +
        ", isEnabled=" + isEnabled +
        '}';
  }

  private class MPCTask extends TimerTask {

    @Override
    public final void run() {
      MotionProfileController.this.calculate();
    }

  }
}