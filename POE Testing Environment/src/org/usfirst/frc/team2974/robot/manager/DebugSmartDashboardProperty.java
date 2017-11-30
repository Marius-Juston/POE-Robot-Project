package org.usfirst.frc.team2974.robot.manager;

import java.util.function.Supplier;

/**
 * @author Evan Tichenor (evan.tichenor@gmail.com)
 * @version 1.0, 11/28/2017
 */
public class DebugSmartDashboardProperty<T> extends SmartDashboardProperty<T> {

  public DebugSmartDashboardProperty(String key, T defaultValue, Supplier<T> valueSupplier) {
    super(key, defaultValue, valueSupplier);
  }
}
