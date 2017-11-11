package org.usfirst.frc.team2974.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.Button;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import org.usfirst.frc.team2974.robot.exceptions.RobotRuntimeException;

public abstract class Driver {

  private final HashMap<String, ButtonMap> buttons;
  private final HashMap<String, Joystick> joysticks;
  private final String driverName;

  public Driver(String driverName) {
    this.driverName = driverName;
    joysticks = new HashMap<>();
    buttons = new HashMap<>();
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
   * @param gamepadButtonKey the key to map the button to on the gamepad
   * @return the created button
   * @throws RobotRuntimeException throws exception when the button name has already been used
   * @throws RobotRuntimeException throws exception when a button has already been assigned to the
   * button key
   */
  public Button createAndAddGamepadButton(String gamepadName, String buttonName,
      GamepadButtonKey gamepadButtonKey) {
    if (buttons.containsKey(buttonName)) {
      throw new RobotRuntimeException("The button name " + buttonName + " has already been used");
    }

    boolean alreadyAssigned = buttons.entrySet().stream().anyMatch(
        stringButtonMapEntry -> stringButtonMapEntry.getValue().getAssignedKey()
            .equals(gamepadButtonKey));

    if (alreadyAssigned) { //checks if the buttons HashMap contains the button
      throw new RobotRuntimeException(
          "A Button has already been assigned the " + gamepadButtonKey.name()
              + " key. Be sure to add to the HashMap using the addJoystick method");
    }

    Button button = new JoystickButton(joysticks.get(gamepadName), gamepadButtonKey.getIndex());
    buttons.put(buttonName, new ButtonMap(gamepadButtonKey, button));

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
    if (!joysticks.containsKey(joystickName)) {
      String[] joystickNames = joysticks.entrySet()
          .stream()
          .filter(stringJoystickEntry -> stringJoystickEntry.getValue().getPort() == usbPort)
          .map(Map.Entry::getKey)
          .toArray(String[]::new);

      if (joystickNames.length > 1) {
        throw new RobotRuntimeException(
            "The Joystick " + joystickName + " tried using the port " + usbPort
                + " which is already used by " + Arrays.toString(joystickNames));
      } else {
        joysticks.put(joystickName, new Joystick(usbPort));
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
   * @param joystickButtonKey the key to map the button to on the joystick
   * @return the created button
   * @throws RobotRuntimeException throws exception when the button name has already been used
   * @throws RobotRuntimeException throws exception when a button has already been assigned to the
   * button key
   */
  public Button createAndAddJoystickButton(String joystickName, String buttonName,
      JoystickButtonKey joystickButtonKey) {
    if (buttons.containsKey(buttonName)) {
      throw new RobotRuntimeException("The button name " + buttonName + " has already been used");
    }

    boolean alreadyAssigned = buttons.entrySet().stream().anyMatch(
        stringButtonMapEntry -> stringButtonMapEntry.getValue().getAssignedKey()
            .equals(joystickButtonKey));

    if (alreadyAssigned) { //checks if the buttons HashMap contains the button
      throw new RobotRuntimeException(
          "A Button has already been assigned the " + joystickButtonKey.name()
              + " key. Be sure to add to the HashMap using the addJoystick method");
    }

    Button button = new JoystickButton(joysticks.get(joystickName), joystickButtonKey.getIndex());
    buttons.put(buttonName, new ButtonMap(joystickButtonKey, button));

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
    if (!buttons
        .containsKey(buttonName)) // checks if button name exists inside of the buttons HashMap
    {
      throw new RobotRuntimeException("The button " + buttonName
          + " does not exist in the buttons HashMap. Be sure to create it using the createGamepadButton or createJoystickButton methods");
    }

    return buttons.get(buttonName).getButton(); // returns the button given its assigned name
  }

  public String getDriverName() {
    return driverName;
  }
}

