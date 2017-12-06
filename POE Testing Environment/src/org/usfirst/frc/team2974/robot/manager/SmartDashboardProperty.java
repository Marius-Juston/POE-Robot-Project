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
     * Used when you want a static value to be on SmartDashboard.
     *
     * @param key the key that SmartDashboard will take in to find or update the value
     * @param defaultValue the default value that SmartDashboard will take in to retrieve a value
     */
    public SmartDashboardProperty(String key, T defaultValue) {
        this(key, defaultValue, null);
    }

    /**
     * Initializes variables and sets up the onValueChange Runnable object.
     *
     * @param key the key that SmartDashboard will take in to find or update the value
     * @param defaultValue the default value that SmartDashboard will take in to retrieve a value
     * @param valueSupplier the value that you wish to place into SmartDashboard, can be null for a
     * static value
     */
    public SmartDashboardProperty(String key, T defaultValue, Supplier<T> valueSupplier) {
        this.key = key;
        value = defaultValue;
        this.defaultValue = defaultValue;

        this.valueSupplier = valueSupplier;

//  onValueChange is run if the value (the value that you want to put into SmartDashboard) changes.
        onValueChange = () -> {
        };

        update(); //TODO check if this helps fix the issue with static variables.
        updateSmartDashboard();
    }

    /**
     * Returns the key to retrieve the SmartDashboard value
     *
     * @return the key meant to be used to retrieved the SmartDashboard value
     */
    public final String getKey() {
        return key;
    }

    /**
     * Gets the current value of the valueSupplier
     *
     * @return returns the current value
     */
    public final T getValue() {
        return value;
    }

    /**
     * Sets the value and updates SmartDashboard by running the onValueChange Runnable object run
     * method
     *
     * @param value the value you want value to be
     */
    public final void setValue(T value) {
        if (!value.equals(this.value)) { // will update SmartDashboard value if the value changes
            this.value = value;

            updateSmartDashboard();
            if (onValueChange != null) {
                onValueChange.run();
            }
        }
    }

    /**
     * Returns the default value.
     *
     * @return returns the default value
     */
    public final T getDefaultValue() {
        return defaultValue;
    }

    /**
     * Returns the value supplier. This is used to determine the value that is meant to be on
     * SmartDashboard
     *
     * @return returns valueSupplier
     */
    public final Supplier<T> getValueSupplier() {
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
    public final Runnable getOnValueChange() {
        return onValueChange;
    }

    /**
     * Sets onValueChange
     *
     * @param onValueChange the new onValueChange to say how SmartDashboard will be updated
     */
    public final void setOnValueChange(Runnable onValueChange) {
        this.onValueChange = onValueChange;
    }

    protected void updateSmartDashboard() {
        if (value == null) {
            return;
        }

        if (value instanceof Number) { // if the value you are going to put in is a number (double, float, int, byte, etc.)
            SmartDashboard.putNumber(key, ((Number) value).doubleValue());
        } else if (value instanceof Boolean) { // if the value is a boolean
            SmartDashboard.putBoolean(key, (Boolean) value);
        } else if (value instanceof Sendable) { // if the value is a Sendable object (SendableChooser, etc...)
            SmartDashboard.putData(key, (Sendable) value);
        } else {
            // if it is something else it uses its toSting method to display it on SmartDashboard
            SmartDashboard.putString(key, value.toString());
        }
    }

    /**
     * This method retrieves the latest value from the value Supplier and if the value changes it will
     * use the onValueChange Runnable object to update SmartDashboard
     */
    public void update() {
        //FIXME this will not run if you have a static variable since the valueSupplier is always null causing it to never send the value to SmartDashboard

        if (valueSupplier != null) {
            setValue(valueSupplier.get());
        }
    }


    @Override
    public String toString() {
        return String.format("SmartDashboardProperty{key='%s', defaultValue=%s, value=%s}", key,
            defaultValue, value);
    }
}
