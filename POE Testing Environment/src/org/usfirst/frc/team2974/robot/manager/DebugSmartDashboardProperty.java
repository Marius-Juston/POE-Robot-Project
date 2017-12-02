package org.usfirst.frc.team2974.robot.manager;

import java.util.function.Supplier;

/**
 *
 */
public class DebugSmartDashboardProperty<T> extends SmartDashboardProperty<T> {

  /**
   * This creates a SmartDashboard value to show only when SmartDashboardManager.isDebug is true.
   */
  public DebugSmartDashboardProperty(final String key, final T defaultValue,
      final Supplier<T> valueSupplier) {
    super(key, defaultValue, valueSupplier);
  }

  @Override
  protected final void updateSmartDashboard() {
    if (SmartDashboardManager.isDebug) {
      super.updateSmartDashboard();
    } else if (SmartDashboardManager.TABLE
        .containsKey(getKey())) // why dost thou do this to me smartdashboard
    {
      SmartDashboardManager.TABLE.delete(getKey());
    }
  }

  @Override
  public final void update() {
    super.update();
  }
}
