package org.usfirst.frc.team2974.robot;

import edu.wpi.first.wpilibj.buttons.Button;


/**
 * This class is used to associate the button object with a button index
 */
public class ButtonMap {

  private final Enum assignedKey;
  private final Button button;

  public <T extends Enum> ButtonMap(T assignedKey, Button button) {

    this.assignedKey = assignedKey;
    this.button = button;
  }

  public Enum getAssignedKey() {
    return assignedKey;
  }

  public Button getButton() {
    return button;
  }
}
