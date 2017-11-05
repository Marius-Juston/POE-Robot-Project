package org.usfirst.frc.team2974.robot;

import org.usfirst.frc.team2974.robot.subsystems.DriveTrain;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class SmartDashboardManager {

  private static final List<SmartDashboardProperty> PROPERTIES = new ArrayList<>();

  static {
    addBind(
            "Left Motor Power",
            0,
            () -> SubsystemManager.getSubsystem(DriveTrain.class).getLeftMotorPower()
    );
  }

  public static <T> SmartDashboardProperty<T> addBind(String key, T defaultValue, Supplier<T> valueSupplier) {
    SmartDashboardProperty<T> prop = new SmartDashboardProperty<>(key, defaultValue, valueSupplier);

    PROPERTIES.add(prop);

    return prop;
  }

  public static List<SmartDashboardProperty> getProperties() {
    return PROPERTIES;
  }

  /**
   * Property getter.
   * @param key the key of the property
   * @param <T> the property type
   * @return the property with the specified key
   */
  @SuppressWarnings("unchecked")
  public static <T> SmartDashboardProperty<T> getProperty(String key) {
    try {
      return PROPERTIES.stream()
              .filter(p -> p.getKey().equals(key))
              .findFirst()
              .get();
    } catch (Exception e) {
      System.err.println("Property " + key + " does not exist. Did you forget to add it?");
    }

    return null;
  }

  // TODO create a method that allows you to bind a value. i.e updates the value on smartdashboard
  // when it changes without the programmer having to create an update smartdashboard method. Extend
  // the Talon class to make it extend Observable? What is the Decorator pattern. Can it be used in
  // this context?

  public static void update() {
    PROPERTIES.forEach(SmartDashboardProperty::update);
  }
}
