package org.usfirst.frc.team2974.robot.controllers;

/**
 * What feeds the vectors into the kinematics
 */
public class Motion {
  // kK = 0.1
  // kV = 0.4

  private final RobotPair position;
  private final RobotPair velocity;
  private final RobotPair accel;

  public Motion(double posLeft, double velLeft, double accLeft, double posRight,
      double velRight, double accRight) {

    this.position = new RobotPair(posLeft, posRight);
    this.velocity = new RobotPair(velLeft, velRight);
    this.accel = new RobotPair(accLeft, accRight);

    //		RobotLoggerManager.setFileHandlerInstance("robot.controllers").info("");
  }

  @Override
  public String toString() {
    return String.format("position=%s; velocity=%s; accel=%s", this.position, this.velocity, this.accel);
  }


}
