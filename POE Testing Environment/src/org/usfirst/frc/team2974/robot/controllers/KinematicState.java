package org.usfirst.frc.team2974.robot.controllers;

public class KinematicState {

  // The length of
  public final double length;

  public final double velocity;
  public final double acceleration;

  public KinematicState(final double l, final double v, final double a) {
    length = l;
    velocity = v;
    acceleration = a;
  }

  public static KinematicState interpolate(final KinematicState state0, final double p,
      final KinematicState state1,
      final double q) {
    return new KinematicState((p * state0.length) + (q * state1.length), state1.velocity,
        state1.acceleration);
  }

  public final String toString() {
    return String.format("length=%f, velocity=%f, acceleration=%f", this.length, this.velocity,
        this.acceleration);
  }
}
