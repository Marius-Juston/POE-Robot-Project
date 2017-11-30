package org.usfirst.frc.team2974.robot.manager;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;
import org.usfirst.frc.team2974.robot.exception.RobotRuntimeException;
import org.usfirst.frc.team2974.robot.subsystem.DriveTrain;

public class SmartDashboardManager {

  private static final List<SmartDashboardProperty> PROPERTIES = new ArrayList<>(); // Properties list where all the SmartDashboard  Properties are stored

  private static boolean isDebug = true;

  /**
   * <p>Creates a SmartDashboard Property that will update automatically when the update method of
   * SmartDashboardManager is called.</p>
   *
   * <p>
   *   Example: <pre>{@code
   *   addBind("Left Motor Power", 0, () -> SubsystemManager.getSubsystem(DriveTrain.class).getLeftMotorPower());
   *   }</pre>
   * </p>
   *
   * @param key SmartDashboard key
   * @param defaultValue Default value that SmartDashboard will returns if it cannot find the value
   * @param valueSupplier Supplier used to get the updating value
   * @param <T> the data type you want SmartDashboard to display
   * @return The SmartDashboard property created
   */
  public static <T> SmartDashboardProperty<T> addBind(String key, T defaultValue,
      Supplier<T> valueSupplier) {
    SmartDashboardProperty<T> prop = new SmartDashboardProperty<>(key, defaultValue, valueSupplier);

    PROPERTIES.add(prop);

    return prop;
  }

  /**
   * Returns the list of SmartDashboard Properties
   *
   * @return returns the PROPERTIES list
   */
  public static List<SmartDashboardProperty> getProperties() {
    return PROPERTIES;
  }

  /**
   * Retrieves the wanted property given it key.
   *
   * @param key the key of the property
   * @param <T> the data type of value being updated to SmartDashboard
   * @return the SmartDashboard property with the specified key
   * @throws RobotRuntimeException throws exception if the SmartDashboard property is not found
   */
  @SuppressWarnings("unchecked")
  public static <T> SmartDashboardProperty<T> getProperty(String key) {
    Optional<SmartDashboardProperty> smartDashboardProperty = PROPERTIES.stream()
        .filter(p -> p.getKey()
            .equals(key)) // gts the properties with the same key as the one searching for
        .findFirst();  /// gets the first SmartDashboard property

    if (smartDashboardProperty.isPresent()) // if there is a SmartDashboard property
    {
      return smartDashboardProperty.get(); // returns the SmartDashboard property
    }

    throw new RobotRuntimeException("Property " + key
        + " does not exist. Did you forget to add it?"); // if it did not find the SmartDashboard property throw error
  }

  /**
   * Iterates through the PROPERTIES list and calls the update method for each individual property.
   * The update method in the property uses its supplier to get the latest value and if the value
   * changes it will update the value by doing whatever the Runnable object onValueChange tells it
   * to do
   */
  public static void update() {
    PROPERTIES.forEach(SmartDashboardProperty::update);
  }
}
