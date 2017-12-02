package org.usfirst.frc.team2974.robot.manager;

import java.util.function.Supplier;

/**
 *
 */
public class DebugSmartDashboardProperty<T> extends SmartDashboardProperty<T> {

  /**
   * This creates a SmartDashboard value to show only when SmartDashboardManager.isDebug is true.
   * @param key {@inheritDoc}
   * @param defaultValue {@inheritDoc}
   * @param valueSupplier {@inheritDoc}
   */
  public DebugSmartDashboardProperty(String key, T defaultValue, Supplier<T> valueSupplier) {
    super(key, defaultValue, valueSupplier);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected final void updateSmartDashboard() {
    if(SmartDashboardManager.isDebug)
      super.updateSmartDashboard();
    else if(SmartDashboardManager.TABLE.containsKey(getKey())) // why dost thou do this to me smartdashboard
      SmartDashboardManager.TABLE.delete(getKey());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void update() {
    super.update();
  }
}
