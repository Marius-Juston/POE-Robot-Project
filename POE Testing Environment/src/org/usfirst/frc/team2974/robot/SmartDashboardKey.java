package org.usfirst.frc.team2974.robot;

public class SmartDashboardKey<T> {
  private final String KEY;
  private final T value;
  private final T defaultValue;
  
  public SmartDashboardKey(String key, T value,T defaultValue)
  {
    KEY = key;
    this.value = value;
    this.defaultValue = defaultValue; 
  }
  

  public T getValue() {
    return value;
  }

  public String getKEY() {
    return KEY;
  }

  public T getDefaultValue() {
    return defaultValue;
  } 
}
