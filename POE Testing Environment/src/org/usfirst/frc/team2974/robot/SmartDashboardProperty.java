package org.usfirst.frc.team2974.robot;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import java.util.function.Supplier;

public class SmartDashboardProperty<T> {

  private final String key;
  private T value;
  private final T defaultValue;

  private Supplier<T> valueSupplier;
  private Runnable onValueChange; // runs when value changes
  
  public SmartDashboardProperty(String key, T defaultValue, Supplier<T> valueSupplier) {
    this.key = key;
    this.value = defaultValue;
    this.defaultValue = defaultValue;

    this.valueSupplier = valueSupplier;

    onValueChange = () -> {
      if(value instanceof Number)
        SmartDashboard.putNumber(key, (double) value);
      else if(value instanceof Boolean)
        SmartDashboard.putBoolean(key, (boolean) value);
      else 
        SmartDashboard.putString(key, value.toString());
    };
  }

  public String getKey() {
    return key;
  }

  public T getValue() {
    return value;
  }

  public T getDefaultValue() {
    return defaultValue;
  }

  public Supplier<T> getValueSupplier() {
    return valueSupplier;
  }

  public Runnable getOnValueChange() {
    return onValueChange;
  }

  public void setValue(T value) {
    this.value = value;

    onValueChange.run();
  }

  public void setValueSupplier(Supplier<T> valueSupplier) {
    this.valueSupplier = valueSupplier;
  }

  public void setOnValueChange(Runnable onValueChange) {
    this.onValueChange = onValueChange;
  }

  /**
   * Runs in the SmartDashboardManager update method, which also runs in the robot update method.
   */
  public void update() {
    setValue( valueSupplier.get() );
  }
}
