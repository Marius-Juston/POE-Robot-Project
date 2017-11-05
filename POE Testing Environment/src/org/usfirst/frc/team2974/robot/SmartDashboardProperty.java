package org.usfirst.frc.team2974.robot;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class SmartDashboardProperty<T> {

  private final String key;
  private T value;
  private final T defaultValue;

  private Runnable onValueChange;
  
  public SmartDashboardProperty(String key, T defaultValue) {
    this.key = key;
    this.value = defaultValue;
    this.defaultValue = defaultValue;

    onValueChange = () -> {
      if(value instanceof String)
        SmartDashboard.putString(key, (String) value);
      else if(value instanceof Number)
        SmartDashboard.putNumber(key, (double) value);
      else if(value instanceof Boolean)
        SmartDashboard.putBoolean(key, (boolean) value);
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

  public Runnable getOnValueChange() {
    return onValueChange;
  }

  public void setValue(T value) {
    this.value = value;

    onValueChange.run();
  }

  public void setOnValueChange(Runnable onValueChange) {
    this.onValueChange = onValueChange;
  }

}
