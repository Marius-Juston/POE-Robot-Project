package org.usfirst.frc.team2974.robot;

/**
 * This enum is used to map what the button number is mapped to what on the gamepad all the while
 * giving them meaningful names instead of numbers. Note: This is not for a XBox controll //FIXME
 * look at what controller is usually used
 */
public enum GamepadButtonKey {
  A(1), B(2), X(3), Y(4), L(5), R(6), BACK(7), START(
      8); //FIXME Add rest of the buttons and verify that these are correct

  private int index;

  /**
   * Initializes the variables
   *
   * @param index index of the button as it depicted on the driver station
   */
  GamepadButtonKey(int index) {
    this.index = index;
  }

  /**
   * Returns the index of the button.
   *
   * @return returns the index of the button
   */
  public int getIndex() {
    return index;
  }
}




