package org.usfirst.frc.team2974.robot;


/**
 * This enum is used to map what the button number is mapped to what on the joysticks all the while
 * giving them meaningful names instead of numbers.
 */
public enum JoystickButtonKey {
  A(1), B(2), X(3), Y(4), L(5), R(6), BACK(7), START(
      8); //FIXME Add rest of the buttons and verify that these are correct

  private int index;

  /**
   * Initializes the variables
   *
   * @param index index of the button as it depicted on the driver station
   */
  JoystickButtonKey(int index) {
    this.index = index;
  }

  public int getIndex() {
    return index;
  }
}
