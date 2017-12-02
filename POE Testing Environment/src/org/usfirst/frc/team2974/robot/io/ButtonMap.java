package org.usfirst.frc.team2974.robot.io;

import edu.wpi.first.wpilibj.buttons.Button;


/**
 * This class is used to associate the button object with a button index
 */
public class ButtonMap {

  private final Enum assignedKey;
  private final Button button;

  public <T extends Enum> ButtonMap(final T assignedKey, final Button button) {

    this.assignedKey = assignedKey;
    this.button = button;
  }

  public final Enum getAssignedKey() {
    return this.assignedKey;
  }

  public final Button getButton() {
    return this.button;
  }

  @Override
  public final String toString() {
    return "ButtonMap{" +
        "assignedKey=" + assignedKey +
        ", button=" + button +
        '}';
  }
}
