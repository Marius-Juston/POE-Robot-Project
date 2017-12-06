package org.usfirst.frc.team2974.robot.manager;

import java.util.function.Supplier;

import static org.usfirst.frc.team2974.robot.RobotConfiguration.isDebug;

public class DebugSmartDashboardProperty<T> extends SmartDashboardProperty<T> {

    /**
     * This creates a SmartDashboard value to show only when SmartDashboardManager.isDebug is true.
     */
    public DebugSmartDashboardProperty(String key, T defaultValue, Supplier<T> valueSupplier) {
        super(key, defaultValue, valueSupplier);
    }

    @Override
    protected final void updateSmartDashboard() {
        if (isDebug) {
            super.updateSmartDashboard();
        } else if (SmartDashboardManager.containsBind(getKey())) {
            // why dost thou do this to me smartdashboard
            SmartDashboardManager.removeBind(getKey()); // TODO: fix so it actually deletes
        }
    }
}
