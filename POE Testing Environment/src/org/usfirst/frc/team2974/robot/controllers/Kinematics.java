package org.usfirst.frc.team2974.robot.controllers;

import static org.usfirst.frc.team2974.robot.RobotConfiguration.ROBOT_WIDTH;

class Kinematics {

  private final MotionProvider motion;
  private final double v0;
  private final double v1;
  private final int nPoints;
  private KinematicPose lastPose;
  private KinematicPose nextPose;
  private double s;
  private double l0;

  public Kinematics(MotionProvider motion, RobotPair wheelPosition, double t, double v0, double v1,
      int nPoints) {
    this.motion = motion;
    s = 0.0;
    this.v0 = v0;
    this.v1 = v1;
    this.nPoints = nPoints;

    this.evaluateFirstPose(wheelPosition, t);
    this.evaluateNextPose(1.0 / nPoints);
  }

  public static KinematicPose staticPose(Pose pose, RobotPair wheelPosition, double t) {
    KinematicState left = new KinematicState(wheelPosition.left, 0, 0);
    KinematicState right = new KinematicState(wheelPosition.right, 0, 0);
    return new KinematicPose(pose, left, right, t, false);
  }

  private void evaluateFirstPose(RobotPair wheelPosition, double t) {
    Pose pose = this.motion.evaluatePose(0);
    KinematicState left = new KinematicState(wheelPosition.left, this.v0, 0);
    KinematicState right = new KinematicState(wheelPosition.right, this.v0, 0);

    this.lastPose = this.nextPose = new KinematicPose(pose, left, right, t, false);
    this.l0 = this.lastPose.getLCenter();
    System.out.println("First Pose: " + this.lastPose);
  }

  private void evaluateLastPose() {
    Pose pose = this.motion.evaluatePose(1.0);
    KinematicState left = new KinematicState(this.lastPose.left.length, this.v1, 0);
    KinematicState right = new KinematicState(this.lastPose.right.length, this.v1, 0);

    this.lastPose = this.nextPose = new KinematicPose(pose, left, right, this.lastPose.t, true);
    System.out.println("Last Pose: " + this.lastPose);
  }

  private void evaluateNextPose(double ds) {
    this.lastPose = this.nextPose;

    if (this.lastPose.isFinished) {
      this.evaluateLastPose();
      return;
    }

    boolean isFinished = false;
    if (this.s + ds > 1.0) {
      this.s = 1.0;
      isFinished = true;
    } else {
      this.s += ds;
    }

    //calculate the next pose from given motion
    Pose pose = this.motion.evaluatePose(this.s);
    double direction = Math.signum(this.motion.getLength());

    //estimate angle to turn s
    double dl = this.lastPose.point.distance(pose.point) * direction;
    double dAngle = MotionProvider.boundAngle(pose.angle - this.lastPose.angle);

    //estimate lengths each wheel will turn
    double dlLeft = (dl - dAngle * ROBOT_WIDTH / 2);
    double dlRight = (dl + dAngle * ROBOT_WIDTH / 2);

    //assuming one of the wheels will limit motion, calculate time this step will take
    double dt = Math.max(Math.abs(dlLeft), Math.abs(dlRight)) / this.motion.vCruise;
    double a = 0.0; //acceleration doesn't matter if following steady motion
    //System.out.println(String.format("s=%f, dl=%f, dlLeft=%f, dlRight=%f, dt=%f, direction=%f", s, dl, dlLeft, dlRight, dt, direction));
    //bound time steps for initial/final acceleration
    switch (this.motion.getLimitMode()) {
      case LimitLinearAcceleration:
        //assuming constant linear acceleration from initial/to final speeds
        double v = Math.abs(dl) / dt;
        double lMidpoint = this.lastPose.getLCenter() + .5 * dl - this.l0;

        double vAccel = Math.sqrt(this.v0 * this.v0 + this.motion.aMax * Math.abs(lMidpoint));
        double vDecel = Math
            .sqrt(this.v1 * this.v1 + this.motion.aMax * (Math
                .abs(this.motion.getLength() - lMidpoint)));

        if (vAccel < v && vAccel < vDecel) {
          a = this.motion.aMax;
          dt = Math.abs(dl) / vAccel;
        }

        if (vDecel < v && vDecel < vAccel) {
          a = -this.motion.aMax;
          dt = Math.abs(dl) / vDecel;
        }

        //System.out.println(String.format("v=%f, vaccel=%f, vdecel=%f", v, vAccel, vDecel));
        break;
      case LimitRotationalAcceleration:
        //assuming constant angular acceleration from/to zero angular speed
        double omega = Math.abs(dlRight - dlLeft) / dt / ROBOT_WIDTH;
        double thetaMidpoint = this.lastPose.angle + .5 * dAngle;

        double omegaAccel = Math.sqrt(this.motion.aMax * Math
            .abs(MotionProvider.boundAngle(thetaMidpoint - this.motion.getInitialTheta())));
        double omegaDecel = Math.sqrt(this.motion.aMax * Math
            .abs(MotionProvider.boundAngle(thetaMidpoint - this.motion.getFinalTheta())));
        //	  System.out.println("OmegaAccel=" + omegaAccel);
        if (omegaAccel < omega && omegaAccel < omegaDecel) {
          dt = Math.abs(dlRight - dlLeft) / omegaAccel / ROBOT_WIDTH;
        }

        //	  System.out.println("OmegaDecel=" + omegaDecel);
        if (omegaDecel < omega && omegaDecel < omegaAccel) {
          dt = Math.abs(dlRight - dlLeft) / omegaDecel / ROBOT_WIDTH;
        }
        break;
    }

    //create new kinematic state. Old state is retained to interpolate positions, new state contains estimate for speed and accel
    KinematicState left = new KinematicState(this.lastPose.left.length + dlLeft, dlLeft / dt,
        a * direction);
    KinematicState right = new KinematicState(this.lastPose.right.length + dlRight, dlRight / dt,
        a * direction);

    this.nextPose = new KinematicPose(pose, left, right, this.lastPose.t + dt, isFinished);
    System.out.println("Next Pose " + this.nextPose);
  }

  public MotionProvider getMotion() {
    return motion;
  }

  //calculates the path to follow within a particular distance
  public KinematicPose interpolatePose(double t) {

    //System.out.println(String.format("Last t=%3.1f Next t=%3.1f Now t=%3.1f", lastPose.t, nextPose.t, t));

    if (t <= this.lastPose.t) {
      return this.lastPose;
    }

    if (this.lastPose.isFinished) {
      return this.lastPose;
    }

    while (t > this.nextPose.t) {
      this.evaluateNextPose(1.0 / this.nPoints);
      if (this.lastPose.isFinished) {
        return this.lastPose;
      }
    }

    double dt = this.nextPose.t - this.lastPose.t;
    double p = (this.nextPose.t - t) / dt;
    double q = (t - this.lastPose.t) / dt;

    return KinematicPose.interpolate(this.lastPose, p, this.nextPose, q);
  }

  public Pose getPose() {
    return new Pose(this.nextPose.point, this.nextPose.angle);
  }

  public RobotPair getWheelPositions() {
    return new RobotPair(this.nextPose.left.length, this.nextPose.right.length);
  }

  public double getTime() {
    return this.nextPose.t;
  }

  public String toString() {
    return String.format("Last pose = %s Next pose = %s", this.lastPose, this.nextPose);
  }
}
