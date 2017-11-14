package org.usfirst.frc.team2974.robot.manager;

import edu.wpi.first.wpilibj.Sendable;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import java.util.function.Supplier;

/**
 * This is a Readonly Property meant to facilitate updating values on SmartDashboard.
 */
public class SmartDashboardProperty<T> {

  private final String key; // SmartDashboard key
  private final T defaultValue; // Default value
  private T value; // Current value
  private Supplier<T> valueSupplier; // Returns the value to be on SmartDashboard
  private Runnable onValueChange; // Runs when value changes

  /**
   * Initializes variables and sets up the onValueChange Runnable object.
   *
   * @param key the key that SmartDashboard will take in to find or update the value
   * @param defaultValue the default value that SmartDashboard will take in to retrieve a value
   * @param valueSupplier the value that you wish to place into SmartDashboard
   */
  public SmartDashboardProperty(String key, T defaultValue, Supplier<T> valueSupplier) {
    this.key = key;
    this.value = defaultValue;
    this.defaultValue = defaultValue;

    this.valueSupplier = valueSupplier;

//  onValueChange is the interface that is run if the value
//  (the value that you want to put into SmartDashboard) changes.
    onValueChange = () -> {};
  }


  /**
   * Returns the key to retrieve the SmartDashboard value
   *
   * @return the key meant to be used to retrieved the SmartDashboard value
   */
  public String getKey() {
    return key;
  }

  /**
   * Gets the current value of the valueSupplier
   *
   * @return returns the current value
   */
  public T getValue() {
    return value;
  }

  /**
   * Sets the value and updates SmartDashboard by running the onValueChange Runnable object run
   * method
   *
   * @param value the value you want value to be
   */
  public void setValue(T value) {
    if (!value.equals(this.value)) { // will update SmartDashboard value if the value changes
      this.value = value;

      updateSmartDashboard();
      if(onValueChange != null) {
        onValueChange.run();
      }
    }
  }

  /**
   * Returns the default value.
   *
   * @return returns the default value
   */
  public T getDefaultValue() {
    return defaultValue;
  }

  /**
   * Returns the value supplier. This is used to determine the value that is meant to be on
   * SmartDashboard
   *
   * @return returns valueSupplier
   */
  public Supplier<T> getValueSupplier() {
    return valueSupplier;
  }

  /**
   * Sets valueSupplier
   *
   * @param valueSupplier the new valueSupplier that should return what value should be placed in
   * SmartDashboard
   */
  public void setValueSupplier(Supplier<T> valueSupplier) {
    this.valueSupplier = valueSupplier;
  }

  /**
   * Returns onValueChange. This tells the how the program should update the SmartDashboard value
   *
   * @return returns onValueChange
   */
  public Runnable getOnValueChange() {
    return onValueChange;
  }

  /**
   * Sets onValueChange
   *
   * @param onValueChange the new onValueChange to say how SmartDashboard will be updated
   */
  public void setOnValueChange(Runnable onValueChange) {
    this.onValueChange = onValueChange;
  }

  public final void updateSmartDashboard() {
    if (value instanceof Number) { // if the value you are going to put in is a number (double, float, int, byte, etc.)
      SmartDashboard.putNumber(key, ((Number)value).doubleValue());
    } else if (value instanceof Boolean) { // if the value is a boolean
      SmartDashboard.putBoolean(key, (Boolean) value);
    } else if (value instanceof Sendable) { // if the value is a Sendable object (SendableChooser, etc...)
      SmartDashboard.putData(key, (Sendable) value);
    } else {
      SmartDashboard.putString(key, value
              .toString()); // if it is something else it uses its toSting method to display it on SmartDashboard
    }
  }

  /**
   * This method retrieves the latest value from the value Supplier and if the value changes it will
   * use the onValueChange Runnable object to update SmartDashboard
   */
  public void update() {
    setValue(valueSupplier.get());
  }
}
