package org.usfirst.frc.team2974.robot.io.logitech;

/**
 * @author Evan Tichenor (evan.tichenor@gmail.com)
 * @version 1.0, 11/13/2017
 */
public enum POVButton {
  N(0), S(180), E(90), W(270), NE(45), SE(135), NW(315), SW(225), CENTER(0);

  private int angle;

  POVButton(int angle) {
    this.angle = angle;
  }

  boolean getPressed(Gamepad g) {
    return g.getPOV() == angle;
  }
}
