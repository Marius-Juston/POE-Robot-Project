package org.usfirst.frc.team2974.robot.io;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.Button;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import org.usfirst.frc.team2974.robot.exception.RobotRuntimeException;
import org.usfirst.frc.team2974.robot.io.logitech.GamepadButton;

public abstract class Driver {

  public static final Driver DEFAULT_DRIVER = new Driver("Default Driver") {
    @Override
    public void initButtons() {
    }
  };

  private final HashMap<String, ButtonMap> buttons;
  private final HashMap<String, Joystick> joysticks;
  private final String driverName;

  public Driver(String driverName) {
    this.driverName = driverName;
    this.joysticks = new HashMap<>();
    this.buttons = new HashMap<>();

    this.initButtons();
  }

  /**
   * Method used to initialize the button map. Assigning the button tasks.
   */
  public abstract void initButtons();

  /**
   * Creates a button assigned to the given gamepad for the given Joystick button
   *
   * @param gamepadName name of the gamepad that you want to use. Gamepad must have been added using
   * the addJoystick method
   * @param buttonName name the button will be assigned
   * @param gamepadButton the key to map the button to on the gamepad
   * @return the created button
   * @throws RobotRuntimeException throws exception when the button name has already been used
   * @throws RobotRuntimeException throws exception when a button has already been assigned to the
   * button key
   */
  public Button createAndAddGamepadButton(String gamepadName, String buttonName,
      GamepadButton gamepadButton) {
    if (this.buttons.containsKey(buttonName)) {
      throw new RobotRuntimeException("The button name " + buttonName + " has already been used");
    }

    boolean alreadyAssigned = this.buttons.entrySet().stream().anyMatch(
        stringButtonMapEntry -> stringButtonMapEntry.getValue().getAssignedKey()
            .equals(gamepadButton));

    if (alreadyAssigned) { //checks if the buttons HashMap contains the button
      throw new RobotRuntimeException(
          "A Button has already been assigned the " + gamepadButton.name()
              + " key. Be sure to add to the HashMap using the addJoystick method");
    }

    Button button = new edu.wpi.first.wpilibj.buttons.JoystickButton(this.joysticks.get(gamepadName),
        gamepadButton.getIndex());
    this.buttons.put(buttonName, new ButtonMap(gamepadButton, button));

    return button;
  }

  /**
   * Adds a Joystick to the list of Joysticks and attaches it on the specific usb port.
   *
   * @param joystickName The name to give the Joystick
   * @param usbPort the usb port to attach the usb port onto
   * @throws RobotRuntimeException throws an exception if the port is already used by another
   * joystick
   * @throws RobotRuntimeException throws exception if the joystick name has already been used
   */
  public void addJoystick(String joystickName, int usbPort) {
    if (!this.joysticks.containsKey(joystickName)) {
      String[] joystickNames = this.joysticks.entrySet()
          .stream()
          .filter(stringJoystickEntry -> stringJoystickEntry.getValue().getPort() == usbPort)
          .map(Entry::getKey)
          .toArray(String[]::new);

      if (joystickNames.length > 1) {
        throw new RobotRuntimeException(
            "The Joystick " + joystickName + " tried using the port " + usbPort
                + " which is already used by " + Arrays.toString(joystickNames));
      } else {
        this.joysticks.put(joystickName, new Joystick(usbPort));
      }
    } else {
      throw new RobotRuntimeException("The joystick " + joystickName
          + " is already defined in the list of available joysticks.");
    }
  }

  /**
   * Creates a button assigned to the given joystick for the given Joystick button
   *
   * @param joystickName name of the joystick that you want to use. Joystick must have been added
   * using the addJoystick method
   * @param buttonName name the button will be assigned
   * @param joystickButton the key to map the button to on the joystick
   * @return the created button
   * @throws RobotRuntimeException throws exception when the button name has already been used
   * @throws RobotRuntimeException throws exception when a button has already been assigned to the
   * button key
   */
  public Button createAndAddJoystickButton(String joystickName, String buttonName,
      JoystickButton joystickButton) {
    if (this.buttons.containsKey(buttonName)) {
      throw new RobotRuntimeException("The button name " + buttonName + " has already been used");
    }

    boolean alreadyAssigned = this.buttons.entrySet().stream().anyMatch(
        stringButtonMapEntry -> stringButtonMapEntry.getValue().getAssignedKey()
            .equals(joystickButton));

    if (alreadyAssigned) { //checks if the buttons HashMap contains the button
      throw new RobotRuntimeException(
          "A Button has already been assigned the " + joystickButton.name()
              + " key. Be sure to add to the HashMap using the addJoystick method");
    }

    Button button = new edu.wpi.first.wpilibj.buttons.JoystickButton(this.joysticks.get(joystickName),
        joystickButton
            .getIndex());
    this.buttons.put(buttonName, new ButtonMap(joystickButton, button));

    return button;
  }


  /**
   * Gets the button given its assigned name when created through the createGamepadButton and
   * createJoystickButton methods
   *
   * @param buttonName the name of the button that you want returned
   * @return The button given its button name
   * @throws RobotRuntimeException throws an exception if the button is not present inside of the
   * buttons HashMap
   */
  public Button getButton(String buttonName) {
    if (!this.buttons
        .containsKey(buttonName)) // checks if button name exists inside of the buttons HashMap
    {
      throw new RobotRuntimeException("The button " + buttonName
          + " does not exist in the buttons HashMap. Be sure to create it using the createGamepadButton or createJoystickButton methods");
    }

    return this.buttons.get(buttonName).getButton(); // returns the button given its assigned name
  }

  public String getDriverName() {
    return this.driverName;
  }
}

