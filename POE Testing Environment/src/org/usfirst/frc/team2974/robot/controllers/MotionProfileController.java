package org.usfirst.frc.team2974.robot.controllers;

import edu.wpi.first.wpilibj.Timer;
import java.util.TimerTask;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;
import org.usfirst.frc.team2974.robot.manager.SubsystemManager;
import org.usfirst.frc.team2974.robot.subsystem.DriveTrain;

public class MotionProfileController {

  private final int nPoints = 50; // number of motions
  private final BlockingDeque<MotionProvider> motions = new LinkedBlockingDeque<>(); // motions that we want to do
  private final PoseProvider poseProvider; // wheel positions and pose
  private final java.util.Timer controller; // schedules all of the motions to be run, TIMER
  // PID values
  private double kV, // what we want to change the voltage by to get the motion we want
      kK, // up/down translation
      kA, // FIXME?? WHAT IS THIS
      kP; // what we want to change the power by to get the moton we want
  private Kinematics currentKinematics; // does all the math
  private KinematicPose staticKinematicPose; // has the vectors that we use in the calculations
  private boolean isEnabled; // yeah, this

  public MotionProfileController(PoseProvider poseProvider, double period) {

    this.poseProvider = poseProvider;

    this.controller = new java.util.Timer();

    this.controller.schedule(new MPCTask(), 0L, (long) (period * 1000));

    this.staticKinematicPose = Kinematics
        .staticPose(poseProvider.getPose(), poseProvider.getWheelPositions(),
            Timer.getFPGATimestamp());
  }

  /**
   * Stops the motion
   */
  public void free() {
    this.controller.cancel();
    //RobotLoggerManager.setFileHandlerInstance("robot.controller").info("MPCTask is destroyed.");
  }

  public synchronized void addMotion(MotionProvider motion) {
    this.motions.addLast(motion);
    System.out.println("added motion" + motion);
  }

  public synchronized void enable() {
    System.out.println(String.format("kK=%f, kV=%f, kA=%f, kP=%f", this.kK, this.kV, this.kA, this.kP));
    MotionProvider newMotion = this.motions.poll();
    if (newMotion != null) {
      this.currentKinematics = new Kinematics(newMotion, this.poseProvider.getWheelPositions(),
          Timer.getFPGATimestamp(), 0, 0, this.nPoints);
//			System.out.println("starting new motion:" + currentKinematics.toString());
      this.isEnabled = true;
    }
  }

  /**
   * Hard stop
   */
  public synchronized void cancel() {
    this.isEnabled = false;
    this.currentKinematics = null;
    this.motions.clear();
    SubsystemManager.getSubsystem(DriveTrain.class).setPowers(0, 0);
  }

  public boolean getEnabled() {
    return this.isEnabled;
  }

  public synchronized double getKV() {
    return this.kV;
  }

  public synchronized void setKV(double kV) {
    this.kV = kV;
  }

  public synchronized double getKK() {
    return this.kK;
  }

  public synchronized void setKK(double kK) {
    this.kK = kK;
  }

  public synchronized double getKA() {
    return this.kA;
  }

  public synchronized void setKA(double kA) {
    this.kA = kA;
  }

  public synchronized double getKP() {
    return this.kP;
  }

  public synchronized void setKP(double kP) {
    this.kP = kP;
  }

  /**
   * This is the PID loop, accounts for distance error.
   */
  private void calculate() {

    double leftPower = 0;
    double rightPower = 0;
    boolean enabled;

    synchronized (this) {
      enabled = isEnabled;
    }

    if (enabled) {

      double time = Timer.getFPGATimestamp();

      RobotPair wheelPositions = this.poseProvider.getWheelPositions();

      KinematicPose kinematicPose;
      if (this.currentKinematics != null) {
        kinematicPose = this.currentKinematics.interpolatePose(time);
      } else {
        kinematicPose = this.staticKinematicPose;
      }

//			System.out.println("time:" + time+ " " + wheelPositions + " " + kinematicPose);
      synchronized (this) {
        //feed forward
        leftPower += (this.kV * kinematicPose.left.velocity + this.kK) + this.kA * kinematicPose.left.acceleration;
        rightPower +=
            (this.kV * kinematicPose.right.velocity + this.kK) + this.kA * kinematicPose.right.acceleration;
        //feed back
        leftPower += this.kP * (kinematicPose.left.length - wheelPositions.left);
        rightPower += this.kP * (kinematicPose.right.length - wheelPositions.right);

      }

      leftPower = Math.max(-1, Math.min(1, leftPower));
      rightPower = Math.max(-1, Math.min(1, rightPower));

//			System.out.println(String.format("LP=%f,RP=%f, err_l=%f, err_r=%f", leftPower,rightPower,
//					kinematicPose.left.l - wheelPositions.left,
//					kinematicPose.right.l - wheelPositions.right));

      SubsystemManager.getSubsystem(DriveTrain.class).setPowers(leftPower, rightPower);

      if (kinematicPose.isFinished) {
        MotionProvider newMotion = this.motions.pollFirst();
        if (newMotion != null) {
          this.currentKinematics = new Kinematics(newMotion, this.currentKinematics.getWheelPositions(),
              this.currentKinematics.getTime(), 0, 0, this.nPoints);
//					System.out.println("starting new motion:" + currentKinematics.toString());
        } else {
          this.staticKinematicPose = Kinematics
              .staticPose(this.currentKinematics.getPose(), this.currentKinematics.getWheelPositions(),
                  this.currentKinematics.getTime());
          this.currentKinematics = null;
        }
      }
    }
  }

  public synchronized boolean isFinished() {
    return this.currentKinematics == null;
  }

  private class MPCTask extends TimerTask {

    @Override
    public void run() {
      MotionProfileController.this.calculate();
    }

  }
}