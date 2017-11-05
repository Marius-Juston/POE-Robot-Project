package org.usfirst.frc.team2974.robot;

public class SmartDashboardManager {

  public static <T extends Number> SmartDashboardKey<T> createBindKey(String KEY, T value,
      T defaultValue) {

    // TODO make it add a listener to value and if he value changes update the smartdashboard value.
    // Either force the programmer to pass in a lamda expression as the value so that when the
    // returned value changes update?
    return new SmartDashboardKey<T>(KEY, value, defaultValue);
  }

  // TODO create a method that allows you to bind a value. i.e updates the value on smartdashboard
  // when it changes without the programmer having to create an update smartdashboard method. Extend
  // the Talon class to make it extend Observable? What is the Decorator pattern. Can it be used in
  // this context?
}
