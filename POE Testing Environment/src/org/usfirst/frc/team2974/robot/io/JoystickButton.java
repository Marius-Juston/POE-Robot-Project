package org.usfirst.frc.team2974.robot.io;


/**
 * This enum is used to map what the button number is mapped to what on the joysticks all the while
 * giving them meaningful names instead of numbers. Note this is for Logitech Attack 3
 */
public enum JoystickButton {
  TRIGGER(1), _2(2), _3(3), _4(4), _5(5), _6(6), _7(7), _8(8), _9(9), _10(10);

  private final int index;

  /**
   * Initializes the variables
   *
   * @param index index of the button as it depicted on the driver station
   */
  JoystickButton(int index) {
    this.index = index;
  }

  public int getIndex() {
    return index;
  }

  @Override
  public String toString() {
    return "JoystickButton{" +
        "index=" + index +
        '}';
  }
}
