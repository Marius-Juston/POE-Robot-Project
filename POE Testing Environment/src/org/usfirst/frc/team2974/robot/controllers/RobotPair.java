package org.usfirst.frc.team2974.robot.controllers;

/**
 * This is the pair of the left and right wheels. DOUBLE VALUES
 */
public class RobotPair {

  public final double left; // location of left side
  public final double right; // ^^ right ^

  public RobotPair(final double left, final double right) {
    this.left = left;
    this.right = right;
  }

  // location of center
  public final double mean() {
    return (left + right) / 2.0;
  }

  @Override
  public final String toString() {
    return String.format("%f, %f", this.left, this.right);
  }

}
