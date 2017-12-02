package org.usfirst.frc.team2974.robot.manager;

import edu.wpi.first.wpilibj.networktables.NetworkTable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;
import org.usfirst.frc.team2974.robot.exception.RobotRuntimeException;

public final class SmartDashboardManager {

  public static final NetworkTable TABLE = NetworkTable.getTable("SmartDashboard");
  public static final boolean isDebug = true;
  private static final List<SmartDashboardProperty> PROPERTIES = new ArrayList<>(
      10); // Properties list where all the SmartDashboard  Properties are stored

  private SmartDashboardManager() {
  }

  /**
   * @param defaultValue {}
   */
  public static <T> SmartDashboardProperty<T> addBind(final String key, final T defaultValue) {
    return addBind(key, defaultValue, null);
  }

  /**
   * <p>Creates a SmartDashboard Property that will update automatically when the update method of
   * SmartDashboardManager is called.</p>
   *
   * <p>
   * Example: <pre>{@code
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
  public static <T> SmartDashboardProperty<T> addBind(final String key, final T defaultValue,
      final Supplier<T> valueSupplier) {
    final SmartDashboardProperty<T> prop = new SmartDashboardProperty<>(key, defaultValue,
        valueSupplier);

    SmartDashboardManager.PROPERTIES.add(prop);

    return prop;
  }

  /**
   * Removes all binds (should be one) with key key from SmartDashboard and the manager.
   *
   * @param key SmartDashboard key
   */
  public static void removeBind(final String key) {
    for (int i = 0; i < PROPERTIES.size(); i++) {
      if (PROPERTIES.get(i).getKey().equals(key)) {
        PROPERTIES.remove(i);
        i--;
        TABLE.delete(key);
      }
    }
  }

  /**
   * Returns the list of SmartDashboard Properties
   *
   * @return returns the PROPERTIES list
   */
  public static List<SmartDashboardProperty> getProperties() {
    return Collections.unmodifiableList(SmartDashboardManager.PROPERTIES);
  }

  /**
   * Retrieves the wanted property given it key.
   *
   * @param <T> the data type of value being updated to SmartDashboard
   * @param key the key of the property
   * @return the SmartDashboard property with the specified key
   * @throws RobotRuntimeException throws exception if the SmartDashboard property is not found
   */
  public static <T> SmartDashboardProperty getProperty(final String key) {
    final Optional<SmartDashboardProperty> smartDashboardProperty = SmartDashboardManager.PROPERTIES
        .stream()
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
    SmartDashboardManager.PROPERTIES.forEach(SmartDashboardProperty::update);
  }
}
